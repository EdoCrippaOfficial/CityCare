package inc.elevati.imycity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/** Class that resizes and compresses images to reduce their size */
public class Compressor implements Runnable {

    /** Constant representing the wanted thumbnail quality, used during compression */
    private static final int QUALITY_THUMBNAIL = 40;

    /** Constant representing the wanted full image quality, used during compression */
    private static final int QUALITY_FULL = 80;

    /** Constant representing the max size of the full image, used during resizing */
    private static final int MAX_SIZE_FULL = 1280;

    /** Constant representing the scale (referred to MAX_SIZE_FULL) of the thumbnail size, used during resizing */
    private static final int SCALE_THUMBNAIL = 4;

    /** The listener which receives data when it's ready or gets notified of an error */
    private UtilsContracts.CompressorListener listener;

    /** Context needed by Glide to load the image from Uri */
    private Context appContext;

    /** The image Uri */
    private Uri imageUri;

    /**
     * Private constructor, called by startCompressing
     * @param listener the listener which receives data when it's ready or gets notified of an error
     * @param appContext context needed by Glide to load the image from Uri
     * @param imageUri the image Uri
     */
    private Compressor(UtilsContracts.CompressorListener listener, Context appContext, Uri imageUri) {
        this.listener = listener;
        this.appContext = appContext;
        this.imageUri = imageUri;
    }

    /**
     * Method that starts compressing precess creating a new thread
     * @param listener The listener which receives data when ready or gets notified of errors
     * @param appContext context needed by Glide to load the image from Uri
     * @param imageUri the image Uri
     */
    public static void startCompressing(UtilsContracts.CompressorListener listener, Context appContext, Uri imageUri) {
        new Thread(new Compressor(listener, appContext, imageUri)).start();
    }

    /** Called when thread is started, implements resizing and compressing tasks */
    @Override
    public void run() {
        try {
            // Let Glide load the bitmap, fitCenter() ensures that MAX_SIZE_FULL will be the max bitmap length (width or height)
            Bitmap image = GlideApp.with(appContext).asBitmap().load(imageUri).override(MAX_SIZE_FULL).fitCenter().submit().get();

            // Get byte array of the full image
            byte[] fullData = getByteArray(image, QUALITY_FULL);

            // Scale the bitmap for thumbnail version
            int thumbWidth = image.getWidth() / SCALE_THUMBNAIL;
            int thumbHeight = image.getHeight() / SCALE_THUMBNAIL;
            Bitmap thumbnail = Bitmap.createScaledBitmap(image, thumbWidth, thumbHeight, false);

            // Get byte array of the thumbnail image
            byte[] thumbData = getByteArray(thumbnail, QUALITY_THUMBNAIL);

            // Notify listener that task has succeeded
            listener.onCompressed(fullData, thumbData);
        } catch (ExecutionException e) {

            // Notify listener that error has occurred
            listener.onErrorOccurred();
        } catch (InterruptedException ignored) {}
    }

    /**
     * Method called to retrieve compressed data from the given image
     * @param image the source image
     * @param quality the wanted compression quality
     * @return a byte array containing the compressed image data
     */
    private byte[] getByteArray(Bitmap image, int quality) {
        // Compress bitmap and convert it to a byte array
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, byteStream);
        return byteStream.toByteArray();
    }
}
