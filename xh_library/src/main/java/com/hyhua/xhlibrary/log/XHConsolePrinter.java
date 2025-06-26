package com.hyhua.xhlibrary.log;

import static com.hyhua.xhlibrary.log.XHLogConfig.MAX_LEN;

import android.util.Log;

import androidx.annotation.NonNull;

public class XHConsolePrinter implements IXHLogPrinter {
    @Override
    public void print(@NonNull XHLogConfig config,@XHLogType.TYPE int level, String tag, @NonNull String printString) {
        int len = printString.length();
        // 获取行数
        int countOfSub = len / MAX_LEN;
        // 逐行打印
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(level, tag, printString.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {
                Log.println(level, tag, printString.substring(index, len));
            }
        } else {
            Log.println(level, tag, printString);
        }
    }
}
