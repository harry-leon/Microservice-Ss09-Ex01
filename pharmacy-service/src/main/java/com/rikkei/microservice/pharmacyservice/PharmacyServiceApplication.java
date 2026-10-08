package com.rikkei.microservice.pharmacyservice;

import com.rikkei.microservice.pharmacyservice.entity.Medicine;
import com.rikkei.microservice.pharmacyservice.repository.MedicineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class PharmacyServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(PharmacyServiceApplication.class);

    @Value("${app.branch-name:Unknown}")
    private String branchName;

    @Value("${app.hotline:Unknown}")
    private String hotline;

    @Value("${app.address:Unknown}")
    private String address;

    @Value("${spring.datasource.url:Unknown}")
    private String datasourceUrl;

    public static void main(String[] args) {
        SpringApplication.run(PharmacyServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(MedicineRepository medicineRepository) {
        return args -> {
            log.info("=========================================================");
            log.info("PHARMACY SERVICE HAS STARTED SUCCESSFULLY");
            log.info("Loaded Branch Name   : {}", branchName);
            log.info("Loaded Hotline       : {}", hotline);
            log.info("Loaded Address       : {}", address);
            log.info("Loaded Datasource Url: {}", datasourceUrl);
            log.info("=========================================================");

            if (medicineRepository.count() == 0) {
                medicineRepository.save(Medicine.builder()
                        .name("Paracetamol 500mg")
                        .activeIngredient("Paracetamol")
                        .quantity(100)
                        .price(new BigDecimal("25000"))
                        .manufacturer("Dược Hậu Giang")
                        .build());

                medicineRepository.save(Medicine.builder()
                        .name("Amoxicillin 500mg")
                        .activeIngredient("Amoxicillin")
                        .quantity(50)
                        .price(new BigDecimal("45000"))
                        .manufacturer("Dược phẩm TW1")
                        .build());

                log.info("Initialized sample medicine data in Database successfully.");
            }
        };
    }
}
