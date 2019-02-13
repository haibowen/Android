package com.example.wenhaibo.androidstudy_drawerlayout;

public class News {

    private String title;
    private String imageid;



    public News(String name, String imageid) {
        this.title = name;
        this.imageid = imageid;
    }


    public String getTitle() {
        return title;
    }

    public String getImageid() {
        return imageid;
    }
}
