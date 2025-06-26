package com.hyhua.xhlibrary.log;

public class XHLogConfig {
    /**
     * 格式化后每一行的最大字符数
     */
    static int MAX_LEN = 512;
    // 懒汉式的单例
    static XHStackTraceFormatter XH_STACK_TRACE_FORMATTER = new XHStackTraceFormatter();
    static XHThreadFormatter XH_THREAD_FORMATTER = new XHThreadFormatter();


    public String getGlobalTag() {
        return "XHLog";
    }

    public boolean enable() {
        return true;
    }

    /**
     * 配置是否输出线程信息
     */
    public boolean includeThread() {
        return false;
    }

    /**
     * 配置输出堆栈信息深度
     */
    public int stackTraceDepth() {
        return 5;
    }

    /**
     * 注册打印器
     */
    public IXHLogPrinter[] printers() {
        return null;
    }

    /**
     * 注入序列化器
     */
    public JSONParser injectJsonParser() {
        return null;
    }

    /**
     * 提供一个序列化接口，由调用方实现
     */
    public interface JSONParser {
        String toJson(Object src);
    }
}
