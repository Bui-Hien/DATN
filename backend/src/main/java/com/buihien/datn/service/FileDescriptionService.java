package com.buihien.datn.service;

import com.buihien.datn.domain.FileDescription;
import com.buihien.datn.dto.FileDescriptionDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileDescriptionService {
    Boolean deleteById(UUID id);

    FileDescriptionDto saveFile(MultipartFile file);

    FileDescriptionDto getById(UUID id);

    FileDescription getEntityById(UUID id);

}
