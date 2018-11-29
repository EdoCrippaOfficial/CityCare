package inc.elevati.imycity.newreport;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static inc.elevati.imycity.CustomMatchers.hasDrawable;
import static inc.elevati.imycity.CustomMatchers.isNotDrawing;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class NewReportViewAndroidTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void prepareGalleryIntent() {
        Intent resultData = new Intent();

        // Get uri from a random dummy image
        Uri uri = Uri.parse("android.resource://inc.elevati.imycity/" + R.drawable.ic_sort);
        resultData.setData(uri);

        // Mock result to forward to activity
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);

        // Respond to ACTION_CHOOSER intent
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(result);
    }

    @Test
    public void selectImageTest() {
        // Go to the target fragment
        onView(withId(R.id.view_pager)).perform(swipeLeft());

        // Click on the empty imageView
        onView(withId(R.id.iv_new_report)).perform(click());

        // Click on the pick image option
        onView(withId(R.id.bn_gallery)).perform(click());

        // Check if the imageView drawable has been changed (is not R.drawable.ic_add_image anymore)
        onView(withId(R.id.iv_new_report)).check(matches(allOf(isDisplayed(), hasDrawable(), isNotDrawing(R.drawable.ic_add_image))));
    }

    @Test
    public void takePhotoTest() {
        // Go to the target fragment
        onView(withId(R.id.view_pager)).perform(swipeLeft());

        // Click on the empty imageView
        onView(withId(R.id.iv_new_report)).perform(click());

        // Click on the take photo option
        onView(withId(R.id.bn_camera)).perform(click());

        // Check that the ACTION_IMAGE_CAPTURE intent has been sent
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }
}