package com.hyhua.xhlibrary.log;

public class XHThreadFormatter implements IXHLogFormatter<Thread> {

    @Override
    public String format(Thread data) {
        return "Thread: " + data.getName();
    }
}
