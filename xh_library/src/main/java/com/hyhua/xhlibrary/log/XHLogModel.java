package com.hyhua.xhlibrary.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class XHLogModel {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public long timeMills;
    @XHLogType.TYPE
    public int level;
    public String tag;
    public String log;

    public XHLogModel(long timeMills, @XHLogType.TYPE int level, String tag, String log) {
        this.timeMills = timeMills;
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public String flattenedLog() {
        return getFlattenedPrefix() + "\n" + log;
    }

    public String getFlattenedPrefix() {
        return format(timeMills) + " | " + getLevelStr(level) + " | " + tag + " |:";
    }

    public String format(long timeMills) {
        return sdf.format(timeMills);
    }

    public String getLevelStr(@XHLogType.TYPE int level) {
        String levelStr;
        switch (level) {
            case XHLogType.V:
                levelStr = "Verbose";
                break;
            case XHLogType.D:
                levelStr = "Debug";
                break;
            case XHLogType.I:
                levelStr = "Info";
                break;
            case XHLogType.W:
                levelStr = "Warn";
                break;
            case XHLogType.E:
                levelStr = "Error";
                break;
            default:
                levelStr = "Assert";
                break;
        }
        return levelStr;
    }
}
