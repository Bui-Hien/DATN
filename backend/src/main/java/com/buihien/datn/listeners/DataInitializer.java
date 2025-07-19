package com.buihien.datn.listeners;

import com.buihien.datn.service.SetupDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner setUpRole(SetupDataService setupDateService) {
        return args -> {
            setupDateService.setupRoles();
            log.info("Set up roles successfully");
        };
    }
}