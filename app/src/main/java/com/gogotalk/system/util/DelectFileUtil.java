package com.gogotalk.system.util;

import java.io.File;

/**
 * Created by fucc
 * Date: 2019-07-24 10:01
 */
public class DelectFileUtil {
    /**
     * 删除单个文件
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(File dirFile) {
        boolean flag = false;
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i]);
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i]);
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(file);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(file);
            }
        }
    }
}
