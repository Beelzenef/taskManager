package com.example.taskmng.model;

/**
 * Created by paco
 */

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String nombre;
    private String desc;
    private String importancia;
    private String enlace;
    private String imagen;
    private String fecha;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getImportancia() {
        return importancia;
    }
    public void setImportancia(String importancia) {
        this.importancia = importancia;
    }
    public String getEnlace() {
        return enlace;
    }
    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Task(String nombre, String desc, String importancia, String fecha, String enlace, String imagen) {
        this.nombre = nombre;
        this.desc = desc;
        this.importancia = importancia;
        this.fecha = fecha;
        this.enlace = enlace;
        this.imagen = imagen;
    }

    public Task(int id, String nombre, String desc, String importancia, String fecha, String enlace, String imagen) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.desc = desc;
        this.importancia = importancia;
        this.fecha = fecha;
        this.enlace = enlace;
        this.imagen = imagen;
    }

    public Task() {}

    @Override
    public String toString() {
        return  nombre + '\n' + importancia;
    }
}
