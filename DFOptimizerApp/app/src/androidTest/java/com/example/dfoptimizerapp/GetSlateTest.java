package com.example.dfoptimizerapp;

import static com.example.dfoptimizerapp.GetSlate.checkValidURL;
import static org.junit.Assert.*;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Test;


import static org.junit.Assert.*;

public class GetSlateTest {
    @Test
    public void checkbadURL(){
        boolean check = checkValidURL("abcdefg");
        assertFalse(check);

    }
    @Test
    public void checkgoodURL(){
        boolean check = checkValidURL("http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/nfl/getslate");
        assertTrue(check);

    }

}



