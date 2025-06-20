package com.jure.common.persistant.dto;

import com.jure.common.persistant.enums.Genre;
import java.util.Date;

public class AuthCabinetResponse {
    private String token;
    private String nomCabinet;
    private Long cabinetId;
    private String tokenType;
    private Date expirationDate;

    public AuthCabinetResponse() {
    }

    // Private constructor for builder
    private AuthCabinetResponse(Builder builder) {
        this.token = builder.token;
        this.nomCabinet = builder.nomCabinet;
        this.cabinetId = builder.cabinetId;
        this.tokenType = builder.tokenType;
        this.expirationDate = builder.expirationDate;
    }

    // Existing constructors
    public AuthCabinetResponse(String token) {
        this.token = token;
    }

    public AuthCabinetResponse(String token, String nomCabinet) {
        this.token = token;
        this.nomCabinet = nomCabinet;
    }

    // Static builder method
    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private String token;
        private String nomCabinet;
        private Long cabinetId;
        private String tokenType;
        private Date expirationDate;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder nomCabinet(String nomCabinet) {
            this.nomCabinet = nomCabinet;
            return this;
        }

        public Builder cabinetId(Long cabinetId) {
            this.cabinetId = cabinetId;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder expirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public AuthCabinetResponse build() {
            return new AuthCabinetResponse(this);
        }
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomCabinet() {
        return nomCabinet;
    }

    public void setNomCabinet(String nomCabinet) {
        this.nomCabinet = nomCabinet;
    }

    public Long getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(Long cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    // Helper method to check if token is expired
    public boolean isExpired() {
        return expirationDate != null && expirationDate.before(new Date());
    }

    // Helper method to check if token is valid (not expired)
    public boolean isValid() {
        return !isExpired();
    }
}
