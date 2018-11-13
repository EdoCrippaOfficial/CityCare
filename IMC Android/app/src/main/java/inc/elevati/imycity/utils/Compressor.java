package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Compressor implements UtilsInterface.ImageCompressor {

    private Bitmap image;

    public Compressor(Bitmap image) {
        this.image = image;
    }

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        return baos.toByteArray();
    }
}
