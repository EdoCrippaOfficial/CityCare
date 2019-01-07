package inc.elevati.imycity.login.signin;

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
public class SignInViewAndroidTest {

    @Rule
    public IntentsTestRule<LoginActivity> activityRule = new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {

        // Reset IdlingResource
        EspressoIdlingResource.reset();

        // Register IdlingResource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void newAccountTest() {

        // Click on login button
        onView(withId(R.id.bn_login)).perform(click());

        // Check if error message is displayed on email TextInputLayout
        onView(withId(R.id.text_input_layout_email))
                .check(matches(hasErrorText(R.string.login_no_email)));

        // Provide an email
        onView(withId(R.id.text_input_edit_email)).perform(replaceText("email@provider.domain"));

        // Click on login button
        onView(withId(R.id.bn_login)).perform(click());

        // Check if error message is displayed on password TextInputLayout
        onView(withId(R.id.text_input_layout_password))
                .check(matches(hasErrorText(R.string.login_no_password)));

        // Provide a password
        onView(withId(R.id.text_input_edit_password)).perform(replaceText("passw0rd"));

        // Click on login button
        onView(withId(R.id.bn_login)).perform(click());

        // Check if MainActivity is launched
        intended(hasComponent(MainActivity.class.getName()));
    }
}
