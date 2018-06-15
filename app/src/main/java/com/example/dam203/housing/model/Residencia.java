package com.example.dam203.housing.model;


import android.graphics.Bitmap;

import java.util.Date;

public class Residencia {
    String usuario;
    String titulo;
    String descripcion;
    String provincia;
    String cp;
    String direccion;
    float precio;
    int personas;
    int puntuacion;
    int num_personas;

    public Residencia(String usuario, String titulo, String descripcion, String provincia,
                      String cp, String direccion, float precio, int personas, int puntuacion, int num_personas) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.provincia = provincia;
        this.cp = cp;
        this.direccion = direccion;
        this.precio = precio;
        this.personas = personas;
        this.puntuacion = puntuacion;
        this.num_personas = num_personas;
    }

    public Residencia() {};

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getNum_personas() {
        return num_personas;
    }

    public void setNum_personas(int num_personas) {
        this.num_personas = num_personas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
