package inc.elevati.imycity.allreports;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import inc.elevati.imycity.main.MainActivity;

@RunWith(AndroidJUnit4.class)

public class AllReportsViewAndroidTest {

    @Rule
    public ActivityTestRule<MainActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
}
