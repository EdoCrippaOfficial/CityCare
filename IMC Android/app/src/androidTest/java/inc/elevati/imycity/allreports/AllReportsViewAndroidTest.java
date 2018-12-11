package inc.elevati.imycity.allreports;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.main.ReportsAdapter;
import inc.elevati.imycity.utils.EspressoIdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static inc.elevati.imycity.CustomMatchers.hasItems;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class AllReportsViewAndroidTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setupIdlingResource() {
        // Register IdlingResource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void sortDialogTest() {

        // Click on sort button
        onView(withId(R.id.bn_sort)).perform(click());

        // Check if dialog is displayed by checking one of its contents
        onView(withText(R.string.sort_report_title)).inRoot(isDialog()).check(matches(isDisplayed()));

        // Newest first
        onView(withId(R.id.bn_newest)).perform(click());

        // Get first and last report timestamp for validation
        RecyclerView recyclerView = intentsRule.getActivity().findViewById(R.id.recycler_all_reports);
        ReportsAdapter adapter = (ReportsAdapter) recyclerView.getAdapter();
        assert adapter != null;
        long time1 = adapter.getItemAt(0).getTimestamp();
        int last = adapter.getItemCount() - 1;
        long time2 = adapter.getItemAt(last).getTimestamp();
        assertTrue(time1 > time2);

        // Click again on sort button
        onView(withId(R.id.bn_sort)).perform(click());

        // Oldest first
        onView(withId(R.id.bn_oldest)).perform(click());

        // Get first and last report timestamp for validation
        time1 = adapter.getItemAt(0).getTimestamp();
        last = adapter.getItemCount() - 1;
        time2 = adapter.getItemAt(last).getTimestamp();
        assertTrue(time1 < time2);
    }

    @Test
    public void loadReportsTest() {

        // Update list
        onView(withId(R.id.refresher)).perform(swipeDown());

        // Check if list is not empty
        onView(withId(R.id.recycler_all_reports)).check(matches(hasItems()));
    }

    @Test
    public void reportDialogTest() {

        // Click on first report
        onView(withId(R.id.recycler_all_reports)).perform(actionOnItemAtPosition(0, click()));

        // Get first report title for validation
        RecyclerView recyclerView = intentsRule.getActivity().findViewById(R.id.recycler_all_reports);
        ReportsAdapter adapter = (ReportsAdapter) recyclerView.getAdapter();
        assert adapter != null;
        String title = adapter.getItemAt(0).getTitle();

        // Check if the right title is displayed in dialog
        onView(withText(title)).inRoot(isDialog()).check(matches(isDisplayed()));
    }
}
