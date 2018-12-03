package com.example.administrator.myzhihuproject;

public class News {
    private  String Title;
    private String Imageid;

    public News(String title, String imageid) {
        Title = title;
        Imageid = imageid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageid() {
        return Imageid;
    }

    public void setImageid(String imageid) {
        Imageid = imageid;
    }
}
