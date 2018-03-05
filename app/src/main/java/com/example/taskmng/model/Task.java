package com.example.taskmng.model;

/**
 * Created by paco
 */

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String name;
    private String desc;
    private String imp;
    private String link;
    private String img;
    private String date;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getImp() {
        return imp;
    }
    public void setImp(String imp) {
        this.imp = imp;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Task(String name, String desc, String imp, String fecha, String enlace, String imagen) {
        this.name = name;
        this.desc = desc;
        this.imp = imp;
        this.date = fecha;
        this.link = enlace;
        this.img = imagen;
    }

    public Task(int id, String name, String desc, String imp, String fecha, String enlace, String imagen) {
        super();
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imp = imp;
        this.date = fecha;
        this.link = enlace;
        this.img = imagen;
    }

    public Task() {}

    @Override
    public String toString() {
        return  name + '\n' + imp;
    }
}
