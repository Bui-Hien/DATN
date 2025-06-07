package com.buihien.datn.service.impl;

import com.buihien.datn.domain.FileDescription;
import com.buihien.datn.dto.FileDescriptionDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.FileDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileDescriptionServiceImpl implements FileDescriptionService {
    private static final Logger logger = LoggerFactory.getLogger(FileDescriptionServiceImpl.class);
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    @Autowired
    protected JpaRepository<FileDescription, UUID> repository;

    @Override
    public Boolean deleteById(UUID id) {
        try {
            FileDescription entity = repository.findById(id).orElse(null);
            if (entity == null) {
                throw new ResourceNotFoundException("Not Found FileDescription with id: " + id);
            }

            // Xóa file vật lý
            if (entity.getFilePath() != null) {
                File file = new File(UPLOAD_DIR + entity.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }

            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while deleting fileDescription with id: " + id);
        }
    }

    @Override
    public FileDescriptionDto saveFile(MultipartFile file) {
        FileDescription entity = new FileDescription();

        if (file != null && !file.isEmpty()) {
            // Xử lý upload file mới
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "." + extension;
            String filePath = UPLOAD_DIR + fileName;

            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                file.transferTo(new File(filePath));

                entity.setName(originalFileName);
                entity.setFilePath(fileName);
                entity.setExtension(extension);
                entity.setContentSize(file.getSize());
                entity.setContentType(file.getContentType());

                entity = repository.save(entity);
                return new FileDescriptionDto(entity);
            } catch (IOException e) {
                throw new RuntimeException("Error while uploading file", e);
            }
        } else {
            throw new ResourceNotFoundException("File is empty");
        }
    }

    @Override
    public FileDescriptionDto getById(UUID id) {
        logger.info("Fetching entity with ID: {}", id);
        Optional<FileDescription> entity = repository.findById(id);
        if (entity.isPresent()) {
            logger.info("Successfully fetched entity.");
            return new FileDescriptionDto(entity.get());
        } else {
            logger.warn("Entity not found with ID: {}", id);
            return null;
        }
    }

    @Override
    public FileDescription getEntityById(UUID id) {
        logger.info("Fetching entity with ID: {}", id);
        Optional<FileDescription> entity = repository.findById(id);
        if (entity.isPresent()) {
            logger.info("Successfully fetched entity.");
            return  entity.get();
        } else {
            logger.warn("Entity not found with ID: {}", id);
            return null;
        }    }
}
