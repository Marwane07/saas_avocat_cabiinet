package com.jure.common.persistant.dto;

import com.jure.common.persistant.enums.Genre;

public class JwtAuthenticationResponse {
    private String token;
    private String email;
    private String nomComplet;
    private String telephone;
    private Genre genre;
    private String role;
    private Long id;
    private String tokenType;

    public JwtAuthenticationResponse() {
    }

    // Private constructor for builder
    private JwtAuthenticationResponse(Builder builder) {
        this.token = builder.token;
        this.email = builder.email;
        this.nomComplet = builder.nomComplet;
        this.telephone = builder.telephone;
        this.genre = builder.genre;
        this.role = builder.role;
        this.id = builder.id;
        this.tokenType = builder.tokenType;
    }

    // Existing constructors
    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponse(String token, String email, String nomComplet, 
                                    String telephone, Genre genre, String role) {
        this.token = token;
        this.email = email;
        this.nomComplet = nomComplet;
        this.telephone = telephone;
        this.genre = genre;
        this.role = role;
    }

    // Static builder method
    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private String token;
        private String email;
        private String nomComplet;
        private String telephone;
        private Genre genre;
        private String role;
        private Long id;
        private String tokenType;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder nomComplet(String nomComplet) {
            this.nomComplet = nomComplet;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public JwtAuthenticationResponse build() {
            return new JwtAuthenticationResponse(this);
        }
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Genre getGenre() {
        return genre;
    }
    
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}