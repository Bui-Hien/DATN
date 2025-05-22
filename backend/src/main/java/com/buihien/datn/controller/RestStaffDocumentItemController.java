package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.StaffDocumentItemDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.AdministrativeUnitService;
import com.buihien.datn.service.StaffDocumentItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/staff-document-item")
@RestController
public class RestStaffDocumentItemController extends GenericApi<StaffDocumentItemDto, SearchDto> {

    public RestStaffDocumentItemController(GenericService<StaffDocumentItemDto, SearchDto> genericService) {
        super(StaffDocumentItemDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/list-staff-document-item-by-staff/{staffId}")
    public ResponseData<List<StaffDocumentItemDto>> getStaffDocumentItemByDocumentTemplate(@PathVariable("staffId") UUID staffId) {
        List<StaffDocumentItemDto> result = ((StaffDocumentItemService) genericService).getStaffDocumentItemByDocumentTemplate(staffId);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }

}
