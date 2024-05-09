package by.tms.onlinerclone26onl.util;

import java.util.Base64;

public class ImageUtils {
    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}


