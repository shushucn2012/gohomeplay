package com.park61.teacherhelper.module.okdownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenlie on 2018/2/2.
 *
 * 下载完成存本地数据库
 */

public class DownloadDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "park61_download.db";
    public static final String DOWNLOAD_TABLE = "download_task";

    public DownloadDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String taskSQL = "create table if not exists " + DOWNLOAD_TABLE +
                                " (task_icon varchar, task_name varchar, task_vid varchar, task_sid varchar, contentId varchar, task_status integer, " +
                                  "task_type integer, task_size integer, task_time DATE, task_filePath text, task_extra1 varchar, task_extra2 varchar)";
        db.execSQL(taskSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
