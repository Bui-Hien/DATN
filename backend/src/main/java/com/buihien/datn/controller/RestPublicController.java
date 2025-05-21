package com.buihien.datn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Validated
public class RestPublicController {
    private static final Logger log = LoggerFactory.getLogger(RestPublicController.class);
}
