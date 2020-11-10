package com.example.dfoptimizerapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import static org.junit.Assert.*;




public class RegisterTest {

    /*@Rule
    public ActivityScenarioRule<Register> activityRule =
            new ActivityScenarioRule<>(Register.class);*/
    @Test
    public void checkLengthShort() {
       boolean check = Register.passMoreThanSix("test");
       assertFalse(check);

    }

    @Test
    public void checkLengthLong() {
        boolean check = Register.passMoreThanSix("testcase2");
        assertTrue(check);

    }

    

/*
    @Test
    public void checkLoginButton() throws Throwable {
        onView(withId(R.id.fullName_register)).perform(typeText("test1"));
        onView(withId(R.id.email_register)).perform(typeText("test1@email.com"));
        onView(withId(R.id.password_register)).perform(typeText("testcase"));
        onView(withId(R.id.confirmPass_register)).perform(typeText("testcase"));
        onView(withId(R.id.registerBtn)).perform(click());
        assertTrue();
    }
*/
    //Test if firebase succeeds
}
