package com.example.movitaco;

public class Meseros {

    private Integer idMesero;
    private String nombreUsuario;
    private String telefonoMesero;
    private String contrasenaMesero;

    public Meseros() {
    }

    public Meseros(Integer idMesero, String nombreUsuario, String telefonoMesero, String contrasenaMesero) {
        this.idMesero = idMesero;
        this.nombreUsuario = nombreUsuario;
        this.telefonoMesero = telefonoMesero;
        this.contrasenaMesero = contrasenaMesero;
    }

    public Integer getIdMesero() {
        return idMesero;
    }

    public void setIdMesero(Integer idMesero) {
        this.idMesero = idMesero;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTelefonoMesero() {
        return telefonoMesero;
    }

    public void setTelefonoMesero(String telefonoMesero) {
        this.telefonoMesero = telefonoMesero;
    }
    public String getContrasenaMesero() {
        return contrasenaMesero;
    }

    public void setContrasenaMesero(String contrasenaMesero) {
        this.contrasenaMesero = contrasenaMesero;
    }
}
