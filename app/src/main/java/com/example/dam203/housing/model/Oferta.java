package com.example.dam203.housing.model;

import java.util.Date;

/**
 * Created by dalei on 11/06/2018.
 */

public class Oferta {
    String id_residencia;
    String id_usuario;
    String id_cliente;
    Date dia_inicio;
    Date dia_fin;
    int puntuacion;
    String comentario;

    public Oferta(String id_residencia, String id_usuario, String id_cliente, Date dia_inicio, Date dia_fin, int puntuacion, String comentario){
        this.id_residencia = id_residencia;
        this.id_usuario = id_usuario;
        this.id_cliente = id_cliente;
        this.dia_inicio = dia_inicio;
        this.dia_fin = dia_fin;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    public Oferta() {}

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
}
