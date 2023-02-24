package com.game.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalMethodsAndAttributesTest {

    @Test
    void isPositiveIntegerTest() {
        GlobalMethodsAndAttributes globalMA = new GlobalMethodsAndAttributes();
        boolean positiveInteger = globalMA.isPositiveInteger("-10");
        assertEquals(false, positiveInteger);
    }

    @Test
    void isPositiveIntegerTestNull() {
        GlobalMethodsAndAttributes globalMA = new GlobalMethodsAndAttributes();
        boolean positiveInteger = globalMA.isPositiveInteger("");
        assertEquals(false, positiveInteger);
    }

}