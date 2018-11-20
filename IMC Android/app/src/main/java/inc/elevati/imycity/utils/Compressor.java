package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Class that compresses images to reduce their size
 */
public class Compressor {

    private Compressor(){}

    /**
     * Singleton
     */
    private static Compressor instance;

    /**
     * Private constants that define quality and max size of the two possible image types
     */
    private static final int QUALITY_THUMBNAIL = 40;
    private static final int QUALITY_FULL = 80;
    private static final int MAX_SIZE_THUMBNAIL = 320;
    private static final int MAX_SIZE_FULL = 1280;

    /**
     * Constant representing the THUMBNAIL image type
     */
    public static final int TYPE_THUMBNAIL = 1;

    /**
     * Constant representing the FULL image type
     */
    public static final int TYPE_FULL = 2;


    public static Compressor getInstance() {
        if (instance == null) instance = new Compressor();
        return instance;
    }

    /**
     * Image compressing method
     * @param image the image to be compressed
     * @param imageType the image type requested (TYPE_THUMBNAIL or TYPE_FULL)
     * @return the byte array of compressed image
     */
    public byte[] getCompressedByteData(Bitmap image, int imageType) {
        int quality, maxSize;
        if (imageType == TYPE_THUMBNAIL) {
            quality = QUALITY_THUMBNAIL;
            maxSize = MAX_SIZE_THUMBNAIL;
        } else {
            quality = QUALITY_FULL;
            maxSize = MAX_SIZE_FULL;
        }
        float height = image.getHeight();
        float width = image.getWidth();
        int nWidth, nHeight;
        if (width > height) {
            nWidth = maxSize;
            nHeight = (int) (height * (maxSize / width));
        } else {
            nHeight = maxSize;
            nWidth = (int) (width * (maxSize / height));
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, nWidth, nHeight, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}
