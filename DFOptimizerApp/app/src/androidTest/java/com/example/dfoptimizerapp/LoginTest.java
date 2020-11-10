package com.example.dfoptimizerapp;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginTest {

    @Rule
    public ActivityScenarioRule<Login> activityRule =
            new ActivityScenarioRule<Login>(Login.class);
    @Test
    public void checkPasswordLabelVisibility_WithText() {
        onView(withId(R.id.password_login)).perform(typeText("test1"));
        final ViewInteraction passwordLabel = onView(withId(R.id.loginPasswordLabel));

        passwordLabel.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Rule
    public ActivityScenarioRule<Login> activityRule2 =
            new ActivityScenarioRule<Login>(Login.class);
    @Test
    public void checkPasswordLabelVisibility_WithoutText() {
        final ViewInteraction passwordLabel = onView(withId(R.id.loginPasswordLabel));

        passwordLabel.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Rule
    public ActivityScenarioRule<Login> activityRule3 =
            new ActivityScenarioRule<Login>(Login.class);
    @Test
    public void checkEmailVisibility_WithText() {
        onView(withId(R.id.email_login)).perform(typeText("test2"));
        final ViewInteraction emailLabel = onView(withId(R.id.loginEmailLabel));

        emailLabel.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Rule
    public ActivityScenarioRule<Login> activityRule4 =
            new ActivityScenarioRule<Login>(Login.class);
    @Test
    public void checkEmailLabelVisibility_WithoutText() {
        final ViewInteraction emailLabel = onView(withId(R.id.loginEmailLabel));

        emailLabel.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

}