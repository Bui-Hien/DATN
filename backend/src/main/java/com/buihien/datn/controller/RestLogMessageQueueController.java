package com.buihien.datn.controller;

import com.buihien.datn.dto.LogMessageQueueDto;
import com.buihien.datn.dto.search.LogMessageQueueSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/log-message-queue")
@RestController
public class RestLogMessageQueueController extends GenericApi<LogMessageQueueDto, LogMessageQueueSearchDto> {

    public RestLogMessageQueueController(GenericService<LogMessageQueueDto, LogMessageQueueSearchDto> genericService) {
        super(LogMessageQueueDto.class, genericService);
    }
}
