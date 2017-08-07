package com.funlisten.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;

/**
 * Created by ZY on 17/3/18.
 */

public class ZYFileUtils {

    public static String getFileName(String urlstr) {
        if (urlstr == null) {
            return null;
        }

        int pos = urlstr.lastIndexOf("/");
        if (pos == -1) {
            return null;
        }

        return urlstr.substring(pos + 1);
    }

    /**
     * 写入文件
     *
     * @param file
     * @throws IOException
     */
    public static void writeResponseBodyCache(ResponseBody responseBody, File file, long current, long total) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (total == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = total;
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                current, allLength - current);
        byte[] buffer = new byte[1024 * 20];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    /**
     * 在SD卡上创建文件
     */
    @Nullable
    public static File createSDFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    /**
     * 文件拷贝迁移,但不删除源文件
     */
    public static boolean copy(String filePath, String fromPath) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fromPath)) {
            return false;
        }

        File file = createSDFile(filePath);
        if (file == null) {
            return false;
        }

        OutputStream outStream = null;
        FileInputStream inputStream = null;

        try {
            File fromFile = new File(fromPath);
            inputStream = new FileInputStream(fromFile);
            outStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (inputStream.read(buffer) != -1) {
                outStream.write(buffer);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    /**
     * Delete file(file or folder).
     *
     * @param filePath
     */
    public static void delete(String filePath) {
        File file = new File(filePath);
        delete(file, true);
    }

    /**
     * Delete file(file or folder).
     *
     * @param file
     */
    public static void delete(File file) {
        delete(file, true);
    }

    /**
     * Delete file(file or folder).
     *
     * @param filePath
     * @param deleteRoot - 是否删除根目录
     */
    public static void delete(String filePath, boolean deleteRoot) {
        File file = new File(filePath);
        delete(file, deleteRoot);
    }

    /**
     * Delete file(file or folder).
     *
     * @param file
     * @param deleteRoot - 是否删除根目录
     */
    public static void delete(File file, boolean deleteRoot) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                delete(f, true);
            } else {
                f.delete();
            }
        }

        if (deleteRoot) {
            file.delete();
        }
    }

    /**
     * 重命名文件
     *
     * @param path
     * @param oldname
     * @param newname
     */
    public static void renameFile(String path, String oldname, String newname) {
        renameFile(path, oldname, newname, false);
    }

    /**
     * 重命名文件
     *
     * @param path
     * @param oldname
     * @param newname
     * @param overWrite 是否覆盖
     */
    public static void renameFile(String path, String oldname, String newname, boolean overWrite) {
        if (!oldname.equals(newname)) {
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;
            }
            if (newfile.exists() && !overWrite) {
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
        }
    }

    // 递归
    public static long getDirWholeFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirWholeFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String formatFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1) {
            fileSizeString = 0 + "B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static void delFolder(String folderPath) {
        try {
            File file = new File(folderPath);
            if(!file.exists()){
                return;
            }
            delete(file); // 删除完里面所有内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
