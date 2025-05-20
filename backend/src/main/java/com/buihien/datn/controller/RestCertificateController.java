package com.buihien.datn.controller;

import com.buihien.datn.dto.CertificateDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/certificate")
@RestController
public class RestCertificateController extends GenericApi<CertificateDto, SearchDto> {

    public RestCertificateController(GenericService<CertificateDto, SearchDto> genericService) {
        super(CertificateDto.class, genericService);
    }
}
