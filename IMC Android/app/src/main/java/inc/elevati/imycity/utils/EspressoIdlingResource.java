package inc.elevati.imycity.utils;

import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    private static CountingIdlingResource idlingResource = new CountingIdlingResource("IdlingResource");

    public static void increment() {
        idlingResource.increment();
    }

    public static void decrement() {
        idlingResource.decrement();
    }

    @VisibleForTesting
    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
