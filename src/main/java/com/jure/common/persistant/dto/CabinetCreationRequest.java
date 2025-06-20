package com.jure.common.persistant.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CabinetCreationRequest {

    @NotBlank(message = "Le nom du cabinet est obligatoire")
    @Size(min = 3, max = 100, message = "Le nom du cabinet doit contenir entre 3 et 100 caractères")
    private String nomCabinet;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    // Getters et Setters
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
