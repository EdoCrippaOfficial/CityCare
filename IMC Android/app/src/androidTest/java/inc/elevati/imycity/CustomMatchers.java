package inc.elevati.imycity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;

import androidx.core.content.ContextCompat;
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
}
