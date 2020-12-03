package com.example.dfoptimizerapp;

import android.app.Instrumentation;
import android.content.Intent;
import android.widget.Button;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;
import org.junit.runner.RunWith;

import org.junit.Rule;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class DisplayLineupsTest {

    private ArrayList<String> array = new ArrayList<>();
    @Rule
    public ActivityTestRule<DisplayLineups> mActivityRule =
            new ActivityTestRule<DisplayLineups>(DisplayLineups.class){

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra("selectedPlayers",(Serializable) array);
                    intent.putExtra("siteChoice", 2);
                    intent.putExtra("sportChoice", 2);
                    return intent;
                }
    };
    @Test
    public void spinnerVisibility_gone() {
        final ViewInteraction lineupSpinner = onView(withId(R.id.lineup1));
        lineupSpinner.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void spinnerVisibility_visible() throws InterruptedException {
        onView(withId(R.id.httpBut)).perform(ViewActions.click());
        final ViewInteraction lineupSpinner = onView(withId(R.id.lineup1));
        lineupSpinner.check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void checkSavedLineups() {

        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(SavedLineups.class.getName(), null, false);
        onView(withId(R.id.save)).perform(ViewActions.click());
        SavedLineups savedLineups = (SavedLineups) activityMonitor.waitForActivityWithTimeout(2000);
        assertNotNull(savedLineups);

    }
}