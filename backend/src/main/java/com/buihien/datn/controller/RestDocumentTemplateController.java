package com.buihien.datn.controller;

import com.buihien.datn.dto.DocumentTemplateDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/document-template")
@RestController
public class RestDocumentTemplateController extends GenericApi<DocumentTemplateDto, SearchDto> {

    public RestDocumentTemplateController(GenericService<DocumentTemplateDto, SearchDto> genericService) {
        super(DocumentTemplateDto.class, genericService);
    }
}
