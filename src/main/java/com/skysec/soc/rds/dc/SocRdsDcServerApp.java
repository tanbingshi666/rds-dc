package com.skysec.soc.rds.dc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class SocRdsDcServerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocRdsDcServerApp.class);

    public static void main(String[] args) {
        try {
            SpringApplication sa = new SpringApplication(SocRdsDcServerApp.class);
            sa.run(args);
            LOGGER.info("Soc-Rds-Dc started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
