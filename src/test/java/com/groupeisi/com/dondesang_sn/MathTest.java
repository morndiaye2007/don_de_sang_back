package com.groupeisi.com.dondesang_sn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathTest {

    @Test
    void addition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void subtraction() {
        assertEquals(0, 2 - 2);
    }

    @Test
    void multiplication() {
        assertEquals(6, 2 * 3);
    }

    @Test
    void division() {
        assertEquals(2, 6 / 3);
    }

    @Test
    void testTrue() {
        assertTrue(true);
    }
}
