package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Class that compresses images to reduce their size
 */
public class Compressor implements UtilsContracts.ImageCompressor {

    /**
     * The image to compress
     */
    private Bitmap image;

    public Compressor(Bitmap image) {
        this.image = image;
    }

    /**
     * Image compressing method
     * @param px the image maximum size
     * @return the byte array of compressed image
     */
    @Override
    public byte[] getCompressedByteData(int px) {
        float height = image.getHeight();
        float width = image.getWidth();
        int nWidth, nHeight;
        if (width > height) {
            nWidth = px;
            nHeight = (int) (height * (px / width));
        } else {
            nHeight = px;
            nWidth = (int) (width * (px / height));
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, nWidth, nHeight, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        return stream.toByteArray();
    }
}
