package org.springframework;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLog {
    private static MyLog instance = new MyLog();
    private FileOutputStream fos;
    private volatile boolean display = false;


    private MyLog() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String time = sdf.format(new Date());
        try {
            fos = new FileOutputStream("/tmp/log." + time + ".log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MyLog getInstance() {
        return instance;
    }

    public synchronized void write(String text) {
        try {
            fos.write(text.getBytes());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void open() {
        display = true;
    }

    public synchronized void close() {
        display = false;
    }

    public synchronized void write(byte[] bs) {
        try {
            fos.write(bs);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeOnOpen(byte[] bs) {
        if (!display) {
            return;
        }
        try {
            fos.write(bs);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
