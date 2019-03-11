package com.example.mygreendaotest;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id

    private String name;
    private int age;
    private long id;

    @Generated(hash = 891192377)
    public User(String name, int age, long id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
