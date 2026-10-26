package com.ebuka.notesapi.model;

public class KycInfo {

    private String documentType;
    private String documentNumber;
    private String issuingCountry;
    private boolean submitted;

    public KycInfo() {
    }

    public KycInfo(
            String documentType,
            String documentNumber,
            String issuingCountry,
            boolean submitted
    ) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.issuingCountry = issuingCountry;
        this.submitted = submitted;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}