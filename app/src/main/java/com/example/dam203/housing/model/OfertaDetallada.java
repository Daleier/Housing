package com.example.dam203.housing.model;

import java.io.Serializable;
import java.util.Date;


public class OfertaDetallada implements Serializable{
    String id_residencia;
    String id_usuario;
    String id_cliente;
    Date dia_inicio;
    Date dia_fin;
    int puntuacion;
    String comentario;
    String titulo;
    String descripcion;
    String provincia;
    String cp;
    String direccion;
    float precio;
    int maximo_personas;
    int puntuacion_total;
    int num_total_personas;

    public OfertaDetallada(Oferta oferta, Residencia residencia) {
        this.id_residencia = oferta.id_residencia;
        this.id_usuario = oferta.id_usuario;
        this.id_cliente = oferta.id_cliente;
        this.dia_inicio = oferta.dia_inicio;
        this.dia_fin = oferta.dia_fin;
        this.puntuacion = oferta.puntuacion;
        this.comentario = oferta.comentario;
        this.titulo = residencia.titulo;
        this.descripcion = residencia.descripcion;
        this.provincia = residencia.provincia;
        this.cp = residencia.cp;
        this.direccion = residencia.direccion;
        this.precio = residencia.precio;
        this.maximo_personas = residencia.personas;
        this.puntuacion_total = residencia.puntuacion;
        this.num_total_personas = residencia.num_personas;
    }

    public OfertaDetallada() {}

    public String getId_residencia() {
        return id_residencia;
    }

    public void setId_residencia(String id_residencia) {
        this.id_residencia = id_residencia;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Date getDia_inicio() {
        return dia_inicio;
    }

    public void setDia_inicio(Date dia_inicio) {
        this.dia_inicio = dia_inicio;
    }

    public Date getDia_fin() {
        return dia_fin;
    }

    public void setDia_fin(Date dia_fin) {
        this.dia_fin = dia_fin;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getMaximo_personas() {
        return maximo_personas;
    }

    public void setMaximo_personas(int maximo_personas) {
        this.maximo_personas = maximo_personas;
    }

    public int getPuntuacion_total() {
        return puntuacion_total;
    }

    public void setPuntuacion_total(int puntuacion_total) {
        this.puntuacion_total = puntuacion_total;
    }

    public int getNum_total_personas() {
        return num_total_personas;
    }

    public void setNum_total_personas(int num_total_personas) {
        this.num_total_personas = num_total_personas;
    }
}
