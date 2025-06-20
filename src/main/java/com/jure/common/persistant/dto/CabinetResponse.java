package com.jure.common.persistant.dto;


public class CabinetResponse {

    private Long id;
    private String nomCabinet;
    private Long tenantId;
    private long nombreUtilisateurs;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCabinet() {
        return nomCabinet;
    }

    public void setNomCabinet(String nomCabinet) {
        this.nomCabinet = nomCabinet;
    }

    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public long getNombreUtilisateurs() {
        return nombreUtilisateurs;
    }

    public void setNombreUtilisateurs(long nombreUtilisateurs) {
        this.nombreUtilisateurs = nombreUtilisateurs;
    }
}