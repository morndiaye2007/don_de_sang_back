package com.groupeisi.com.dondesang_sn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires très simples pour valider le pipeline
 */
class BasicTest {

    @Test
    void testBasicAssertion() {
        assertTrue(true);
    }

    @Test
    void testStringEquals() {
        String expected = "test";
        String actual = "test";
        assertEquals(expected, actual);
    }

    @Test
    void testNumberAddition() {
        int result = 2 + 3;
        assertEquals(5, result);
    }

    @Test
    void testNotNull() {
        String value = "not null";
        assertNotNull(value);
    }

    @Test
    void testArrayLength() {
        int[] numbers = {1, 2, 3};
        assertEquals(3, numbers.length);
    }
}
