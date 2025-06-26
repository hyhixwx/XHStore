package com.hyhua.xhlibrary.log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * 打印堆栈信息、File输出、模拟控制台
 */
public class XHLog {
    private static final String XH_LOG_PACKAGE;

    static {
        String className = XHLog.class.getName();
        XH_LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    public static void v(Object... contents) {
        log(XHLogType.V, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(XHLogType.V, tag, contents);
    }

    public static void d(Object... contents) {
        log(XHLogType.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(XHLogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(XHLogType.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(XHLogType.I, tag, contents);
    }

    public static void w(Object... contents) {
        log(XHLogType.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(XHLogType.W, tag, contents);
    }

    public static void e(Object... contents) {
        log(XHLogType.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(XHLogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(XHLogType.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(XHLogType.A, tag, contents);
    }

    public static void log(@XHLogType.TYPE int type, Object... contents) {
        log(type, XHLogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    public static void log(@XHLogType.TYPE int type, @NonNull String tag, Object... contents) {
        log(XHLogManager.getInstance().getConfig(), type, tag, contents);
    }

    public static void log(@NonNull XHLogConfig config, @XHLogType.TYPE int type, @NonNull String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = XHLogConfig.XH_THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTrace = XHLogConfig.XH_STACK_TRACE_FORMATTER.format(XHStackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(), XH_LOG_PACKAGE, config.stackTraceDepth()));
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(contents, config);
        sb.append(body);
        List<IXHLogPrinter> printers = config.printers() != null ? Arrays.asList(config.printers()) : XHLogManager.getInstance().getPrinters();
        if (printers == null) {
            return;
        }
        for (IXHLogPrinter printer : printers) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, @NonNull XHLogConfig config) {
        if (config.injectJsonParser() != null) {
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        // 删除最后一个分号
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
