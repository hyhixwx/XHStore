package com.hyhua.xhlibrary.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

private const val TAG = "XHExecutor"

/**
 * 支持按任务优先级执行
 *
 * 支持线程池暂停、恢复
 *
 * 支持异步结果主动回调主线程
 *
 * TODO 线程池能力监控，耗时任务检测，定时，延迟
 */
object XHExecutor {

    private var xhExecutor: ThreadPoolExecutor

    private val lock: ReentrantLock = ReentrantLock()

    private val pauseCondition: Condition = lock.newCondition()

    private var isPaused: Boolean = false

    // 用于切换主线程
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        /**
         * cpu可用处理器数量
         */
        val cpuCount = Runtime.getRuntime().availableProcessors()

        /**
         * 核心线程数
         */
        val corePoolSize = cpuCount + 1

        /**
         * 最大线程数
         */
        val maxPoolSize = cpuCount * 2 + 1

        /**
         * 有界队列（默认无界），支持按优先级排序任务
         */
        val blockingQueue: PriorityBlockingQueue<Runnable> = PriorityBlockingQueue()

        /**
         * 保活时长（秒）
         */
        val keepAliveTime = 30L

        val unit = TimeUnit.SECONDS

        val seq = AtomicLong()

        /**
         * 线程工厂
         */
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "xh-executor-" + seq.andIncrement
            return@ThreadFactory thread
        }

        xhExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue as BlockingQueue<Runnable>,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                // 需要暂停时，暂停线程池
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                // 监控线程池耗时任务、线程创建数量、正在运行的数量等指标
                Log.i(TAG, "Task Completed, Priority: ${(r as PriorityRunnable).priority}")
            }
        }
    }

    /**
     * 处理任务
     */
    @JvmOverloads // 用于自动生成重载方法，使Java调用者不必传递所有参数
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        xhExecutor.execute(PriorityRunnable(priority, runnable))
    }

    @JvmOverloads // 用于自动生成重载方法，使Java调用者不必传递所有参数
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Callable<*>) {
        xhExecutor.execute(PriorityRunnable(priority, runnable))
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            mainHandler.post {
                onPrepare()
            }
            val t = onBackground()
            mainHandler.post { onCompleted(t) }
        }

        open fun onPrepare() {}
        abstract fun onBackground(): T
        abstract fun onCompleted(result: T)
    }

    // PriorityBlockingQueue要求传递的任务必须实现Comparable接口，所以需要一个类将Runnable和Comparable封装
    class PriorityRunnable(val priority: Int, val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            // priority值越小，优先级越高
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }
    }

    /**
     * 暂停线程池
     */
    @Synchronized
    fun pause() {
        isPaused = true
        Log.i(TAG, "Executor is paused")
    }

    /**
     * 恢复线程池
     */
    @Synchronized
    fun resume() {
        isPaused = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        Log.i(TAG, "Executor is resumed")
    }
}