package com.groupeisi.com.dondesang_sn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.groupeisi.com.dondesang_sn.*"})
public class DonDeSangSnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonDeSangSnApplication.class, args);
    }

}
