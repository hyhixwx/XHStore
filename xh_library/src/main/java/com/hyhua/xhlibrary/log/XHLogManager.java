package com.hyhua.xhlibrary.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XHLogManager {
    private final XHLogConfig config;
    private static XHLogManager instance;
    private List<IXHLogPrinter> printers = new ArrayList<>();

    private XHLogManager(XHLogConfig config, IXHLogPrinter[] printers) {
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static XHLogManager getInstance() {
        return instance;
    }

    public static void init(@NonNull XHLogConfig config, IXHLogPrinter... printers) {
        instance = new XHLogManager(config, printers);
    }

    public XHLogConfig getConfig() {
        return config;
    }

    public List<IXHLogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinter(IXHLogPrinter printer) {
        printers.add(printer);
    }

    public void removePrinter(IXHLogPrinter printer) {
        if (printers != null) {
            printers.remove(printer);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (XHFilePrinter.getInstance() != null) {
            XHFilePrinter.getInstance().shutdown();
        }
    }
}
