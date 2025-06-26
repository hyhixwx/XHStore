package com.hyhua.xhlibrary.log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件打印器，线程安全单例，用于将日志写入文件
 */
public class XHFilePrinter implements IXHLogPrinter {
    // 使用单线程线程池来处理任务，保证单一线程消费队列，避免并发竞争问题，提高稳定性
    // newSingleThreadExecutor意外终止时，线程池会自动创建一个新的线程继续执行后续任务，如果使用Thread()，将会彻底终止
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    // 保存路径
    private final String logPath;
    // 有效期，单位毫秒
    private final long retentionTime;
    // 日志写入器
    private final LogWriter writer;
    /*
     * 日志打印工作线程
     * 它可能被多个线程访问，但它的修改只发生在构造器中，且存在于单例模式中，只会初始化一次，所以不需要volatile修饰
     * volatile: 使变量的修改对其他线程是可见的。
     */
    private final PrinterWorker worker;

    private static XHFilePrinter instance;

    public static synchronized XHFilePrinter getInstance() {
        return instance;
    }

    /**
     * 获取文件打印器实例
     *
     * @param logPath       打印路径
     * @param retentionTime 文件有效期，单位毫秒
     * @return 文件打印器实例
     */
    public static synchronized XHFilePrinter getInstance(String logPath, long retentionTime) {
        if (instance == null) {
            instance = new XHFilePrinter(logPath, retentionTime);
        }
        return instance;
    }

    private XHFilePrinter(String logPath, long retentionTime) {
        this.logPath = logPath;
        this.retentionTime = retentionTime;
        this.writer = new LogWriter();
        this.worker = new PrinterWorker();
        // 清除过期日志
        cleanExpiredLog();
    }

    @Override
    public void print(@NonNull XHLogConfig config, int level, String tag, @NonNull String printString) {
        long timeMillis = System.currentTimeMillis();
        // 如果工作线程没在运行，就启用它
        if (!worker.isRunning()) {
            worker.start();
        }
        // 将日志放入工作线程的队列中
        worker.put(new XHLogModel(timeMillis, level, tag, printString));
    }

    /**
     * 执行日志打印
     *
     * @param logModel 日志对象
     */
    private void doPrint(XHLogModel logModel) {
        // 当前文件过大或上次使用的文件名为空，表示需要生成新的文件名
        if (writer.getLastFileName() == null) {
            String newFileName = genFileName();
            // 重置写入器
            if (writer.isReady()) {
                writer.close();
            }
            // 准备写入
            if (!writer.ready(newFileName)) {
                return;
            }
        }
        // 把日志内容写入文件
        writer.append(logModel.flattenedLog());
    }

    /**
     * 生成日志文件名，格式为yyyy-MM-dd
     *
     * @return 生成的日志文件名
     */
    private String genFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 清除过期日志文件
     */
    private void cleanExpiredLog() {
        // 有效期小于等于0，表示日志永久有效，不清除
        if (retentionTime <= 0) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        File logDir = new File(logPath);
        File[] files = logDir.listFiles();
        if (files == null) {
            return;
        }
        // 遍历目录下所有文件，删除过期的文件
        for (File file : files) {
            if (currentTimeMillis - file.lastModified() > retentionTime) {
                file.delete();
            }
        }
    }

    public void shutdown() {
        if (worker != null) {
            worker.shutdown();
        }
    }

    /**
     * 日志打印工作线程，负责从队列中取出日志并写入文件
     */
    private class PrinterWorker implements Runnable {
        // 日志队列，用于存放待打印的日志
        /*
         * 为何使用LinkedBlockingQueue？
         * 1. 线程安全：LinkedBlockingQueue内部使用了锁机制，保证了多线程下的线程安全性
         * 2. 阻塞特性：当队列为空时，take操作会阻塞，直到队列中有新的元素；当队列满时，put操作会阻塞，直到队列有空闲空间。
         *    这种特性非常适合生产者-消费者模式。
         * 3. 无界队列：在这里声明的时无界，容量为Integer.MAX_VALUE，因此不会因为队列满而导致日志文件丢失。
         * 4. 高性能：LinkedBlockingQueue基于链表实现，适合高并发场景，尤其是在生产者和消费者速度不一致的情况下。
         * 相比之下，其他如ArrayBlockingQueue需要指定容量，ConcurrentLinkedQueue不支持阻塞操作。
         */
        private final BlockingQueue<XHLogModel> logs = new LinkedBlockingQueue<>();
        // 标记工作线程是否正在运行
        private final AtomicBoolean running = new AtomicBoolean(false);

        /**
         * 将日志放入打印队列
         */
        void put(XHLogModel log) {
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断工作线程是否运行中
         */
        boolean isRunning() {
            return running.get();
        }

        /**
         * 启用工作线程
         */
        void start() {
            // 如果当前是false，就设置为true
            if (running.compareAndSet(false, true)) {
                EXECUTOR.execute(this);
            }
        }


        @Override
        public void run() {
            XHLogModel log;
            try {
                while (running.get() || !logs.isEmpty()) {
                    // 从队列中取出日志
                    log = logs.poll(1, TimeUnit.SECONDS);
                    // 执行打印操作
                    if (log != null) {
                        doPrint(log);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                running.compareAndSet(true, false);
            }
        }

        void shutdown() {
            EXECUTOR.execute(() -> {
                running.compareAndSet(true, false);
                while (!logs.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                writer.close();
            });
        }
    }

    /**
     * 基于BufferedWriter将日志写入文件
     */
    private class LogWriter {
        // 上一次使用的文件名
        private String lastFileName;
        // 日志文件
        private File logFile;
        // 用于写入文件的BufferedWriter
        private BufferedWriter bufferedWriter;

        /**
         * 判断写入器是否已准备好
         */
        boolean isReady() {
            return bufferedWriter != null;
        }

        /**
         * 获取上次使用的文件名
         */
        String getLastFileName() {
            return lastFileName;
        }

        /**
         * 日志写入前的准备操作
         *
         * @param newFileName 要保存的日志文件名
         * @return true 表示准备就绪
         */
        boolean ready(String newFileName) {
            lastFileName = newFileName;
            logFile = new File(logPath, newFileName);
            // 当日志文件不存在时创建日志文件
            if (!logFile.exists()) {
                try {
                    File parent = logFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdir();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    lastFileName = null;
                    logFile = null;
                    return false;
                }
            }

            try {
                // 初始化BufferedWriter
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (Exception e) {
                e.printStackTrace();
                lastFileName = null;
                logFile = null;
                return false;
            }
            return true;
        }

        /**
         * 关闭bufferedWriter
         */
        void close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();// 可能阻塞
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bufferedWriter = null;
                    lastFileName = null;
                    logFile = null;
                }
            }
        }

        /**
         * 将日志写入文件
         */
        void append(String flattenedLog) {
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                // 打印异常日志
                System.err.println("Failed to write log: " + e.getMessage());
                // 尝试恢复
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    bufferedWriter = null;
                    ready(lastFileName);
                }
            }
        }
    }
}
