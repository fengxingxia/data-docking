package com.data.docking;

import com.data.docking.util.SpringApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DataDockingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DataDockingApplication.class, args);
        SpringApplicationContext.setApplicationContext(applicationContext);
    }

}
