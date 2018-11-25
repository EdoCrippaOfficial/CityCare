package inc.elevati.imycity.newreport;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class NewReportViewAndroidTest {

    @Rule
    public ActivityTestRule<MainActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectImageTest() {

        // Go to the target fragment
        onView(withId(R.id.view_pager)).perform(swipeLeft());

        // Click on the empty imageView
        onView(withId(R.id.iv_new_report)).perform(click());

        // Check if the dialog is displayed by checking if one of its child element is displayed
        onView(withId(R.id.bn_camera)).check(matches(isDisplayed()));
    }
}