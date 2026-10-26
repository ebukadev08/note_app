package com.ebuka.notesapi.dto.req;

public class KycRequest {

    private String documentType;
    private String documentNumber;
    private String issuingCountry;

    public KycRequest() {
    }

    public KycRequest(
            String documentType,
            String documentNumber,
            String issuingCountry
    ) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.issuingCountry = issuingCountry;
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
}