package inc.elevati.imycity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Class that compresses images to reduce their size
 */
public class Compressor implements Runnable {

    /** Private constants that define quality and max size of the two possible image types */
    private static final int QUALITY_THUMBNAIL = 40;
    private static final int QUALITY_FULL = 80;
    private static final int MAX_SIZE_THUMBNAIL = 320;
    private static final int MAX_SIZE_FULL = 1280;

    /** The listener which receives data when it's ready */
    private UtilsContracts.compressorListener listener;

    private Context appContext;

    private Uri imageUri;

    private Compressor(UtilsContracts.compressorListener listener, Context appContext, Uri imageUri) {
        this.listener = listener;
        this.appContext = appContext;
        this.imageUri = imageUri;
    }

    /**
     * Method that starts compressing precess creating a new thread
     * @param listener The listener which receives data when it's ready
     */
    public static void startCompressing(UtilsContracts.compressorListener listener, Context appContext, Uri imageUri) {
        new Thread(new Compressor(listener, appContext, imageUri)).start();
    }

    @Override
    public void run() {
        try {
            // TODO non caricare full size!!!
            Bitmap image = GlideApp.with(appContext).asBitmap().load(imageUri).submit().get();
            byte[] thumbData = compressImage(image, true);
            byte[] fullData = compressImage(image, false);
            listener.onCompressed(fullData, thumbData);
        } catch (ExecutionException e) {
            e.printStackTrace();
            listener.onErrorOccurred();
        } catch (InterruptedException ignored) {}
    }

    private byte[] compressImage(Bitmap image, boolean typeThumb) {
        float height = image.getHeight();
        float width = image.getWidth();
        int quality, maxSize;
        if (typeThumb) {
            quality = QUALITY_THUMBNAIL;
            maxSize = MAX_SIZE_THUMBNAIL;
        } else {
            quality = QUALITY_FULL;
            maxSize = MAX_SIZE_FULL;
        }
        int nWidth, nHeight;
        if (width > height) {
            nWidth = maxSize;
            nHeight = (int) (height * (maxSize / width));
        } else {
            nHeight = maxSize;
            nWidth = (int) (width * (maxSize / height));
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, nWidth, nHeight, false);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteStream);
        return byteStream.toByteArray();
    }
}
