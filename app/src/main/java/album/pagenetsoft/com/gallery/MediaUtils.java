package album.pagenetsoft.com.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Sergey on 14.02.2018.
 */

@Slf4j
public class MediaUtils {

    private MediaUtils() {
    }

    public static Bitmap getResizedBitmap(String imagePath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int scaleFactor = Math.min(bmOptions.outWidth / targetW, bmOptions.outHeight / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }

    public static byte[] reduceBitmap( Bitmap bitmap ) {
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream() ) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            return bos.toByteArray();
        }catch (Exception e) {
            log.error("{}", e);
            return new byte[0];
        }
    }

    public static String getFileChecksum(String filePath) {

        try (FileInputStream fis = new FileInputStream(filePath)) {

            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] byteArray = new byte[4096];

            int bytesCount = 0;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            return getHexString( digest.digest() );

        } catch (Exception e) {
            log.error("{}", e);
        }
        return "";
    }

    public static String getHexString( byte[] bytes ) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
