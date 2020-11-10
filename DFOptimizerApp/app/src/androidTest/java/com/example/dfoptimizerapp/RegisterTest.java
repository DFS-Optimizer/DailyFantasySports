package com.example.dfoptimizerapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Test;


import static org.junit.Assert.*;


public class RegisterTest {

    @Test
    public void checkLengthShort() {
       boolean check = Register.passMoreEqualThanSix("test");
       assertFalse(check);

    }

    @Test
    public void checkLengthLong() {
        boolean check = Register.passMoreEqualThanSix("testcase2");
        assertTrue(check);

    }

    @Test
    public void checkLengthEquals() {
        boolean check = Register.passMoreEqualThanSix("test12");
        assertTrue(check);

    }


}
