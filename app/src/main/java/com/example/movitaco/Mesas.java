package com.example.movitaco;

public class Mesas {
    private Integer IdMesa;
    private String nombreMesa;

    public Mesas() {
    }

    public Mesas(Integer idMesa, String nombreMesa) {
        IdMesa = idMesa;
        this.nombreMesa = nombreMesa;
    }

    public Integer getIdMesa() {
        return IdMesa;
    }

    public void setIdMesa(Integer idMesa) {
        IdMesa = idMesa;
    }

    public String getNombreMesa() {
        return nombreMesa;
    }

    public void setNombreMesa(String nombreMesa) {
        this.nombreMesa = nombreMesa;
    }
}
