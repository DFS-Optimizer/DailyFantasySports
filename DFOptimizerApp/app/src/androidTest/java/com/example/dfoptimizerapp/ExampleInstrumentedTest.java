package com.example.dfoptimizerapp;

import android.content.Context;
import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.reflect.Executable;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.dfoptimizerapp", appContext.getPackageName());
    }



    @Rule
    public ActivityScenarioRule<Register> activityRule =
            new ActivityScenarioRule<>(Register.class);


    //Password greater than or equal to 6 characters?
@Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void passwordMin() {
        exception.expect(Exception.class);
        exception.expectMessage("Password must be greater than or equal to 6 characters");

        onView(withId(R.id.fullName_register)).perform(typeText("test1"));
        onView(withId(R.id.email_register)).perform(typeText("test1@email.com"));
        onView(withId(R.id.password_register)).perform(typeText("test"));
        onView(withId(R.id.confirmPass_register)).perform(typeText("test"));
        onView(withId(R.id.registerBtn)).perform(click());
    }

@Test
    public void checkConfirm() {
    final EditText superpass = findViewById(R.id.password_register);
    final EditText superconfirm = findViewById(R.id.confirmPass_register);
    onView(withId(R.id.fullName_register)).perform(typeText("test1"));
    onView(withId(R.id.email_register)).perform(typeText("test1@email.com"));
    onView(withId(R.id.password_register)).perform(typeText("testcase"));
    onView(withId(R.id.confirmPass_register)).perform(typeText("testcase"));
    onView(withId(R.id.registerBtn)).perform(click());
    String pass = superpass.getText().toString();
    String confirm = superconfirm.getText().toString();

    assertEquals(pass,confirm);

}
}