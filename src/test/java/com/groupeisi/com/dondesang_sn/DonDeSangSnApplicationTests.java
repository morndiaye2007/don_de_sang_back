package com.groupeisi.com.dondesang_sn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DonDeSangSnApplicationTests {

    @Test
    void contextLoads() {
        int num1 = 10;
        int num2 = 20;
        int resultat = num1 + num2;
        assertEquals(30, resultat);
    }

}
