package com.hyhua.xhlibrary.log;

public class XHStackTraceFormatter implements IXHLogFormatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        } else if (stackTrace.length == 1) {
            return "\t- " + stackTrace[0].toString();
        } else {
            for (int i = 0; i < stackTrace.length; i++) {
                if (i == 0) {
                    sb.append("stackTrace: \n");
                }
                if (i != stackTrace.length - 1) {
                    sb.append("\t├ ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t⌊ ");
                    sb.append(stackTrace[i].toString());
                }
            }
        }
        return sb.toString();
    }
}
