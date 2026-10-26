package com.ebuka.notesapi.mapper;

import com.ebuka.notesapi.dto.req.KycRequest;
import com.ebuka.notesapi.model.KycInfo;

public class KycMapper {

    public KycInfo toKycInfo(KycRequest request) {
        return new KycInfo(
                request.getDocumentType(),
                request.getDocumentNumber(),
                request.getIssuingCountry(),
                false
        );
    }
}