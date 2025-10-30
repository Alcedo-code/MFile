package com.angel.minio.util.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Author yangb
 * @Date 2022/9/21 17:20
 * @Version 1.0
 */
public class Base64Utils {

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String base64Encode(String text) {
        try {
            byte[] textByte = text.getBytes("UTF-8");
            String encodedText = encoder.encodeToString(textByte);
            return encodedText;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
        }
        return "error";
    }

    public static String base64Decode(String encodedText) {
        try {
            String text = new String(decoder.decode(encodedText), "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
        }
        return "error";
    }

}
