package inc.elevati.imycity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

public class CustomMatchers {

    public static BoundedMatcher<View, ImageView> isNotDrawing(final int resourceId) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {

            @Override
            protected boolean matchesSafely(ImageView imageView) {
                Drawable givenDrawable = ContextCompat.getDrawable(imageView.getContext(), resourceId);
                Drawable actualDrawable = imageView.getDrawable();

                if (actualDrawable == null) return true;

                if (givenDrawable instanceof VectorDrawable) {
                    if (!(actualDrawable instanceof VectorDrawable)) return true;
                    return !vectorToBitmap((VectorDrawable) givenDrawable).sameAs(vectorToBitmap((VectorDrawable) actualDrawable));
                }

                if (givenDrawable instanceof BitmapDrawable) {
                    if (!(actualDrawable instanceof BitmapDrawable)) return true;
                    return !((BitmapDrawable) givenDrawable).getBitmap().sameAs(((BitmapDrawable) actualDrawable).getBitmap());
                }

                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with drawable id: ").appendValue(resourceId);
            }

            private Bitmap vectorToBitmap(VectorDrawable vectorDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                vectorDrawable.draw(canvas);
                return bitmap;
            }
        };
    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    public static BoundedMatcher<View, TextInputLayout> hasErrorText(final int expectedErrorId) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {

            @Override
            public boolean matchesSafely(TextInputLayout textInputLayout) {
                String expectedErrorText = textInputLayout.getContext().getString(expectedErrorId);
                CharSequence actualError = textInputLayout.getError();
                if (actualError == null) return false;
                return expectedErrorText.equals(actualError.toString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with error text");
            }
        };
    }

    public static BoundedMatcher<View, RecyclerView> hasItems() {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) return false;
                return adapter.getItemCount() > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with items");
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> withColorFilter(final ColorFilter colorFilter) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public boolean matchesSafely(ImageView image) {
                return colorFilter.equals(image.getColorFilter());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with color filter: ");
            }
        };
    }
}
