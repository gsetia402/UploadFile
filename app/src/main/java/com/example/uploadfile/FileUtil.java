package com.example.uploadfile;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String convertFileToBase64(Context context, Uri fileUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            if (inputStream != null) {
                byte[] fileBytes = readBytes(inputStream);
                return Base64.encodeToString(fileBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
