package org.springframework;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MyLog {
    private static MyLog instance=new MyLog();
    private FileOutputStream fos;

    private MyLog(){
        try {
            fos = new FileOutputStream("/tmp/log.log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MyLog getInstance(){
        return instance;
    }

    public synchronized void write(String text){
        try{
            fos.write(text.getBytes());
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void write(byte[] bs){
        try{
            fos.write(bs);
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
