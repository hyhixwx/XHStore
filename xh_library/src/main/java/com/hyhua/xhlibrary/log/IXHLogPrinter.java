package com.hyhua.xhlibrary.log;

import androidx.annotation.NonNull;

public interface IXHLogPrinter {
    void print(@NonNull XHLogConfig config,@XHLogType.TYPE int level, String tag, @NonNull String printString);
}
