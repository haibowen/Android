package com.example.administrator.mywifip2p.filemode;

import java.io.Serializable;

public class FileTransfer implements Serializable {

    //文件路径
    private String filepath;
    //文件大小
    private long filelength;
    //MD5校验
    private String md5;
   public  FileTransfer(String name,long filelength){

       this.filepath=name;
       this.filelength=filelength;

    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getFilelength() {
        return filelength;
    }

    public void setFilelength(long filelength) {
        this.filelength = filelength;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    @Override
    public String toString() {


        return "FileTransfer{" +
                "filePath= '" + filepath + '\'' +
                ",filelength=" + filepath +
                ",md5='" + md5 + '\'' +
                '}';

    }
}
