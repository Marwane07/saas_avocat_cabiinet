package com.jure.jureApplication.persistent.dto;


import java.time.LocalDate;

public class CaseCriteria {
    private String reference;
    private String title;
    private String status;
    private LocalDate courtDateFrom;
    private LocalDate courtDateTo;
    private Long assignedUserId;
    private String clientName;

    // Constructeur par défaut
    public CaseCriteria() {
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCourtDateFrom() {
        return courtDateFrom;
    }

    public void setCourtDateFrom(LocalDate courtDateFrom) {
        this.courtDateFrom = courtDateFrom;
    }

    public LocalDate getCourtDateTo() {
        return courtDateTo;
    }

    public void setCourtDateTo(LocalDate courtDateTo) {
        this.courtDateTo = courtDateTo;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    // Builder class
    public static class Builder {
        private final CaseCriteria criteria = new CaseCriteria();

        public Builder reference(String reference) {
            criteria.reference = reference;
            return this;
        }

        public Builder title(String title) {
            criteria.title = title;
            return this;
        }

        public Builder status(String status) {
            criteria.status = status;
            return this;
        }

        public Builder courtDateFrom(LocalDate courtDateFrom) {
            criteria.courtDateFrom = courtDateFrom;
            return this;
        }

        public Builder courtDateTo(LocalDate courtDateTo) {
            criteria.courtDateTo = courtDateTo;
            return this;
        }

        public Builder assignedUserId(Long assignedUserId) {
            criteria.assignedUserId = assignedUserId;
            return this;
        }

        public Builder clientName(String clientName) {
            criteria.clientName = clientName;
            return this;
        }

        public CaseCriteria build() {
            return criteria;
        }
    }
}