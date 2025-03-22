package com.buihien.datn.generic;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.AuditableEntityDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
public class GenericApi<DTO extends AuditableEntityDto, S extends SearchDto> {

    @Autowired
    private GenericService<DTO, S> genericService;

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping(value = "/save")
    public ResponseEntity<DTO> saveOrUpdate(@Valid @RequestBody DTO dto) {
        DTO savedDto = genericService.saveOrUpdate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id) {
        boolean deleted = genericService.deleteById(id);
        if (!deleted) throw new ResourceNotFoundException("Entity with ID " + id + " not found");
        return ResponseEntity.noContent().build();
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/delete-multiple")
    public ResponseEntity<Integer> deleteMultipleByIds(@RequestBody @NotEmpty(message = "List of IDs cannot be empty") List<@NotNull @Min(1) Long> ids) {
        int deletedCount = genericService.deleteMultiple(ids);
        return ResponseEntity.ok(deletedCount);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id) {
        DTO dto = genericService.getById(id);
        if (dto == null) throw new ResourceNotFoundException("Entity with ID " + id + " not found");
        return ResponseEntity.ok(dto);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/paging")
    public ResponseEntity<Page<DTO>> paging(@RequestParam(name = "pageSize", defaultValue = "10") @Min(1) int pageSize, @RequestParam(name = "pageIndex", defaultValue = "0") @Min(0) int pageIndex) {
        Page<DTO> page = genericService.paging(pageIndex, pageSize);
        return ResponseEntity.ok(page);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging-search")
    public ResponseEntity<Page<DTO>> pagingSearch(@Valid @RequestBody S dto) {
        Page<DTO> page = genericService.pagingSearch(dto);
        return ResponseEntity.ok(page);
    }
}
