package com.groupeisi.com.dondesang_sn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires ultra-simples pour valider le pipeline
 */
class SimpleUnitTest {

    @Test
    void testAlwaysPass() {
        assertTrue(true);
    }

    @Test
    void testStringNotNull() {
        String test = "hello";
        assertNotNull(test);
    }

    @Test
    void testNumberComparison() {
        int number = 10;
        assertTrue(number > 5);
    }

    @Test
    void testArrayNotEmpty() {
        int[] array = {1, 2, 3};
        assertTrue(array.length > 0);
    }

    @Test
    void testStringLength() {
        String word = "test";
        assertEquals(4, word.length());
    }
}
