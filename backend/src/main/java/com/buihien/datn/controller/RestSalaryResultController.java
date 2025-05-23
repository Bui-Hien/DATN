package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.SalaryResultDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.SalaryResultService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/salary-result")
@RestController
public class RestSalaryResultController extends GenericApi<SalaryResultDto, SearchDto> {

    public RestSalaryResultController(GenericService<SalaryResultDto, SearchDto> genericService) {
        super(SalaryResultDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/recalculate-salary/{id}")
    public ResponseData<Boolean> recalculateSalary(@PathVariable UUID id) {
        Boolean result = ((SalaryResultService) genericService).getRecalculateSalary(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }
}
