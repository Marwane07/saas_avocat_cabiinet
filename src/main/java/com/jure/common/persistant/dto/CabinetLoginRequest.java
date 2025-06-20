package com.jure.common.persistant.dto;


public class CabinetLoginRequest {
    private String nomCabinet;
    private String password;

    public CabinetLoginRequest() {
    }

    public CabinetLoginRequest(String nomCabinet, String password) {
        this.nomCabinet = nomCabinet;
        this.password = password;
    }

    public String getNomCabinet() {
        return nomCabinet;
    }

    public void setNomCabinet(String nomCabinet) {
        this.nomCabinet = nomCabinet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
