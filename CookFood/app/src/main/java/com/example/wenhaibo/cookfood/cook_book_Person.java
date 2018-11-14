package com.example.wenhaibo.cookfood;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


public class cook_book_Person implements Serializable {

    private int id;
    private String preson_step;
    private Bitmap person_bitmap;
    private ArrayList list_step,list_image_url;

    public ArrayList getList_image_url() {
        return list_image_url;
    }

    public void setList_image_url(ArrayList list_image_url) {
        this.list_image_url = list_image_url;
    }

    public ArrayList getList_step() {
        return list_step;
    }

    public void setList_step(ArrayList list_step) {
        this.list_step = list_step;
    }

    public cook_book_Person(ArrayList list_step, ArrayList list_image_url) {
        this.list_step=list_step;
        this.list_image_url= list_image_url;
    }





    public cook_book_Person(String preson_step, Bitmap person_bitmap) {
        this.preson_step = preson_step;
        this.person_bitmap = person_bitmap;
    }


    public cook_book_Person(int id, String preson_step, Bitmap person_bitmap) {
        this.id = id;
        this.preson_step = preson_step;
        this.person_bitmap = person_bitmap;

    }


    public String getPreson_step() {
        return preson_step;
    }

    public void setPreson_step(String preson_step) {
        this.preson_step = preson_step;
    }

    public Bitmap getperson_bitmap() {
        return person_bitmap;
    }

    public void setperson_bitmap(Bitmap person_bitmap) {
        this.person_bitmap = person_bitmap;
    }



}
