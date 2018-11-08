package inc.elevati.imycity.utils;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

public class Compressor implements UtilsInterface.ImageCompressor {

    private byte[] bitmapData;

    public Compressor(Bitmap bitmap){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int nWidth, nHeight;
        if(width > height){
            nWidth = 1280;
            nHeight = (int) ((float) height * (1280f / (float) width));
        }else{
            nHeight = 1280;
            nWidth = (int) ((float) width * 1280f / (float) height);
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, nWidth, nHeight, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        bitmapData = baos.toByteArray();
    }


    @Override
    public byte[] getCompressedByteData() {
        return bitmapData;
    }
}
