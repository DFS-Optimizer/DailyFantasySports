package com.example.dfoptimizerapp;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginTest {


    @Test
    public void checkChooseSiteVisibility() {
        onView(withId(R.id.password_login)).perform(typeText("test1"));
        final ViewInteraction passwordLabel = onView(withId(R.id.loginPasswordLabel));

        passwordLabel.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));



    }

}