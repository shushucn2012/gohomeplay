/**
 *
 */
package com.park61.teacherhelper.common.tool;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.lzy.okserver.OkDownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 文件管理
 *
 * @author super
 */
public class FilesManager {
    /**
     * 图片下载文件夹名称
     */
    public static final String imageFileName = "imageFile";

    /**
     * 创建图片目录文件夹
     */
    public static void openFile(Context context) {

        File temp = getDownImageFolder(context);
        if (!temp.exists()) {
            temp.mkdir();
        }
    }

    /**
     * 获取下载图片存放路径文件夹file
     */
    public static File getDownImageFolder(Context context) {
        return new File(getDownLoadImageFolderName(context));
    }

    /**
     * 获取下载图片文件夹存放路径
     */
    public static String getDownLoadImageFolderName(Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator
                + imageFileName;
    }

    /**
     * 获取下载图片本地硬缓存地址全路径（data/data目录下）
     *
     * @param url 图片url
     */
    public static String getDownLoadImageDir(Context context, String url) {
        int index = url.lastIndexOf("/");
//		return context.getFilesDir().getAbsolutePath() + File.separator
//				+ imageFileName + File.separator
//				+ url.substring(index + 1, url.length());
        return context.getFilesDir().getAbsolutePath() + File.separator
                + url.substring(index + 1, url.length());

    }

    /**
     * 下载网络文件
     *
     * @param fileUrl 网络文件地址
     */
    public static void downLoadNetFile(String fileUrl) {
        String newFilename = Environment.getExternalStorageDirectory()
                .getPath() + "/GHPCacheFolder/firstpage.zip";
        File file = new File(newFilename);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(fileUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(newFilename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     *
     * @throws Exception
     */
    public static int upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        Log.e("upZipFile", "12313131313132131313");
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.e("upZipFile", "ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                Log.e("upZipFile", "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.e("upZipFile", "ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        Log.e("upZipFile", "finishssssssssssssssssssss");
        return 0;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                    Log.e("getRealFileName", "create dir = " + (lastDir + "/" + dirs[i]));
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            Log.e("upZipFile", "2ret = " + ret);
            return ret;
        } else {
            return new File(baseDir, absFileName);
        }
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static void DeleteFileInFloder(File floder) {
        if (floder.exists() == false) {
            return;
        } else {
            if (floder.isDirectory()) {
                File[] childFile = floder.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
            }
        }
    }

    /**
     * 读取assets文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(String path) throws Exception {
        File file = new File(path);
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    public static void copyFile(File oldFile, String newName) {
        String path = OkDownload.getInstance().getFolder();
        File temFile = new File(path, newName);
        if (temFile.exists()) {
            temFile.delete();
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(path + newName);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copyFile2(File oldFile, String newName) {
        File temFile = new File(oldFile.getParent(), newName);
        if (temFile.exists()) {
            temFile.delete();
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(temFile);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

}
