package com.buihien.datn.listeners;

import com.buihien.datn.service.SetupDataService;
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
    private SetupDataService setupDateService;

    @Override
    public void run(String... args) {
        setupDateService.setupRoles();
        setupDateService.setupNationality();
        setupDateService.setupEthnics();
        setupDateService.setupReligion();
        setupDateService.setupProfession();
        setupDateService.setupFamilyRelationship();
        setupDateService.setupBank();
        log.info("Server started on " + port);
    }
}