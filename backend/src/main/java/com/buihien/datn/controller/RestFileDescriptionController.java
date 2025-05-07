package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.FileDescriptionDto;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.FileDescriptionService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping("/api/file-description")
@RestController
@Validated
public class RestFileDescriptionController {
    private final Logger log = LoggerFactory.getLogger(RestFileDescriptionController.class);
    @Autowired
    private FileDescriptionService fileDescriptionService;

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping(value = "/save-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<FileDescriptionDto> saveFile(@RequestParam("file") MultipartFile file) {
        FileDescriptionDto result = fileDescriptionService.saveFile(file);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Successfully saved", result);
    }



    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteById(@PathVariable @Min(1) UUID id) {
        fileDescriptionService.deleteById(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete success by id " + id);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/{id}")
    public ResponseData<FileDescriptionDto> getById(@PathVariable @Min(1) UUID id) {
        FileDescriptionDto result = fileDescriptionService.getById(id); // nếu không tồn tại sẽ throw ResourceNotFoundException
        return new ResponseData<>(HttpStatus.OK.value(), "Get success by id " + id, result);
    }
}
