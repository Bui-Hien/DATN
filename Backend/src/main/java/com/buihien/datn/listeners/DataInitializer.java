package com.buihien.datn.listeners;

import com.buihien.datn.service.SetupDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${server.port}")
    private int port;
    @Autowired
    private SetupDateService setupDateService;

    @Override
    public void run(String... args) {
        setupDateService.setupRoles();
        log.info("Server started on " + port);
    }
}