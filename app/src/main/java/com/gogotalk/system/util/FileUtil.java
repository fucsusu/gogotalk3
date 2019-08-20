package com.gogotalk.system.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gogotalk.system.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fucc
 * Date: 2019-08-20 11:08
 */
public class FileUtil {
    public static void saveTrophy(Context context, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        InputStream is = context.getResources().openRawResource(R.raw.trophy);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
