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

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.utils.EspressoIdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static inc.elevati.imycity.CustomMatchers.hasDrawable;
import static inc.elevati.imycity.CustomMatchers.hasErrorText;
import static inc.elevati.imycity.CustomMatchers.isNotDrawing;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void newReportTest() {

        // Register IdlingResource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        // Go to the target fragment
        onView(withId(R.id.view_pager)).perform(swipeLeft());

        // Click on send button
        onView(withId(R.id.bn_new_report_send)).perform(click());

        // Check if toast error is displayed
        onView(withText(R.string.new_report_no_picture))
                .inRoot(withDecorView(not(intentsRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        // Click on the empty imageView
        onView(withId(R.id.iv_new_report)).perform(click());

        // Click on the pick image option, our mock intent should be returned
        onView(withId(R.id.bn_gallery)).perform(click());

        // Click on send button
        onView(withId(R.id.bn_new_report_send)).perform(click());

        // Check if error message is displayed on title TextInputLayout
        onView(withId(R.id.text_input_layout_title))
                .check(matches(hasErrorText(R.string.new_report_no_title)));

        // Provide a title
        onView(withId(R.id.text_input_edit_title)).perform(replaceText("TestTitle"));

        // Click on send button
        onView(withId(R.id.bn_new_report_send)).perform(click());

        // Check if error message is displayed on title TextInputLayout
        onView(withId(R.id.text_input_layout_desc))
                .check(matches(hasErrorText(R.string.new_report_no_description)));

        // Provide a description
        onView(withId(R.id.text_input_edit_desc)).perform(replaceText("TestDesc"));

        // Click on send button
        onView(withId(R.id.bn_new_report_send)).perform(click());

        // Check if success toast is displayed
        onView(withText(R.string.new_report_ok))
                .inRoot(withDecorView(not(intentsRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
}