package inc.elevati.imycity.login.register;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginActivity;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.utils.EspressoIdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static inc.elevati.imycity.CustomMatchers.hasErrorText;

@RunWith(AndroidJUnit4.class)
public class RegisterViewAndroidTest {

    @Rule
    public IntentsTestRule<LoginActivity> activityRule = new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {

        // Reset IdlingResource
        EspressoIdlingResource.reset();

        // Register IdlingResource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        // Switch to target fragment
        onView(withId(R.id.tv_register)).perform(click());
    }

    @Test
    public void newAccountTest() {

        // Click on register button
        onView(withId(R.id.bn_register)).perform(click());

        // Check if error message is displayed on name TextInputLayout
        onView(withId(R.id.text_input_layout_name))
                .check(matches(hasErrorText(R.string.register_no_name)));

        // Provide a name
        onView(withId(R.id.text_input_edit_name)).perform(replaceText("TestName"));

        // Click on register button
        onView(withId(R.id.bn_register)).perform(click());

        // Check if error message is displayed on SSN TextInputLayout
        onView(withId(R.id.text_input_layout_ssn))
                .check(matches(hasErrorText(R.string.register_no_ssn)));

        // Provide a SSN
        onView(withId(R.id.text_input_edit_ssn)).perform(replaceText("PPPPPP33C03L378Q"));

        // Click on register button
        onView(withId(R.id.bn_register)).perform(click());

        // Check if error message is displayed on email TextInputLayout
        onView(withId(R.id.text_input_layout_email))
                .check(matches(hasErrorText(R.string.register_no_email)));

        // Provide an email
        onView(withId(R.id.text_input_edit_email)).perform(replaceText("email@provider.domain"));

        // Click on register button
        onView(withId(R.id.bn_register)).perform(click());

        // Check if error message is displayed on password TextInputLayout
        onView(withId(R.id.text_input_layout_password))
                .check(matches(hasErrorText(R.string.register_no_password)));

        // Provide a password
        onView(withId(R.id.text_input_edit_password)).perform(replaceText("passw0rd"));

        // Click on register button
        onView(withId(R.id.bn_register)).perform(click());

        // Check if MainActivity is launched
        intended(hasComponent(MainActivity.class.getName()));
    }
}
