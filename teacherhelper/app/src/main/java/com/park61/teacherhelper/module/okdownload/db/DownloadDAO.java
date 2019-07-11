package com.park61.teacherhelper.module.okdownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.park61.teacherhelper.common.set.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/2/2.
 *
 * 下载任务db操作
 */

public class DownloadDAO {

    private static final String TAG = "Download_DB";
    private DownloadDB dbHelper;

    private DownloadDAO(Context context){
        dbHelper = new DownloadDB(context);
    }

    private static DownloadDAO dao ;
    public static DownloadDAO getInstance(){
        return dao;
    }

    public static void init(Context context){
        if (dao == null ){
            synchronized (DownloadDAO.class){
                if(dao == null){
                    dao = new DownloadDAO(context);
                }
            }
        }
    }

    /**
     * 插入一条数据
     */
    public void insertOrUpdate(DownloadTask task){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("task_icon", task.getTask_icon());
        cv.put("task_name", task.getTask_name());
        cv.put("task_vid", task.getTask_vid());
        cv.put("task_sid", task.getSourceId());
        cv.put("contentId", task.getContentId());
        cv.put("task_status", task.getTask_status());
        cv.put("task_type", task.getTask_type());
        cv.put("task_size", task.getTask_size());
        cv.put("task_time", task.getTask_time());
        cv.put("task_filePath", task.getTask_filePath());

        if(hasTask(task.getTask_vid())){
            //update
            int u = db.update(DownloadDB.DOWNLOAD_TABLE, cv, "task_vid = ?", new String[]{task.getTask_vid()});
            Log.e(TAG, u > 0 ? "更新成功" : "更新失败");
        }else{
            //insert
            long i = db.insert(DownloadDB.DOWNLOAD_TABLE, null, cv);
            Log.e(TAG, i == -1 ? "插入失败" : "插入成功");
        }

        db.close();
    }

    /**
     * 根据vid删除一条数据
     */
    public void delete(String vid){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(DownloadDB.DOWNLOAD_TABLE, "task_vid = ?", new String[]{vid});

        db.close();

        Log.e(TAG, result > 0 ? "删除成功" : "删除失败");
    }

    /**
     * 更新一条数据
     *
     */
    public void update(String vid, int status, String filePath, int size){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("task_status", status);
        cv.put("task_filePath", filePath);
        cv.put("task_size", size);
        db.update(DownloadDB.DOWNLOAD_TABLE, cv, "task_vid = ?", new String[]{vid});
        db.close();
    }

    /**
     * 更新一条数据 下载状态
     *
     */
    public void update(String vid, int status){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("task_status", status);
        db.update(DownloadDB.DOWNLOAD_TABLE, cv, "task_vid = ?", new String[]{vid});
        db.close();
    }

    public boolean hasTask(String vid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select count(*) from "+DownloadDB.DOWNLOAD_TABLE +" where task_vid = ?";
        long l = 0;
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{vid});
            cursor.moveToFirst();
            l = cursor.getLong(0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l > 0;
    }

    /**
     * 根据 vid 查询 sourceId
     */
    public String selectSid(String vid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select task_sid from "+DownloadDB.DOWNLOAD_TABLE +" where task_vid = ?";
        String sid = null;
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{vid});
            cursor.moveToFirst();
            sid = cursor.getString(0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sid;
    }

    public DownloadTask selectTask(String vid){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select task_icon,task_sid,task_name,contentId,task_type,task_status from "+DownloadDB.DOWNLOAD_TABLE +" where task_vid = ?";
        DownloadTask t = null;
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{vid});
            cursor.moveToFirst();
            t = new DownloadTask();
            t.setTask_icon(cursor.getString(0));
            t.setSourceId(cursor.getString(1));
            t.setTask_name(cursor.getString(2));
            t.setContentId(cursor.getString(3));
            t.setTask_type(cursor.getInt(4));
            t.setTask_status(cursor.getInt(5));
            t.setTask_vid(vid);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 根据contentId查询 列表中已下载完成的任务
     */
    public List<DownloadTask> selectContentList(String contentId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DownloadDB.DOWNLOAD_TABLE, new String[]{"task_sid", "task_status", "task_filePath"},
                "contentId = ?", new String[]{contentId}, null, null, null);
        if(cursor.getCount() > 0){
            List<DownloadTask> tasks = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                DownloadTask task = new DownloadTask();
                task.setSourceId(cursor.getString(0));
                task.setTask_status(cursor.getInt(1));
                task.setTask_filePath(cursor.getString(2));
                tasks.add(task);
            }
            cursor.close();
            return tasks;
        }else{
            cursor.close();
            return new ArrayList<DownloadTask>();
        }
    }

    /**
     * 查询 type 为1 已完成的任务
     *
     */
    public List<DownloadTask> selectAllComplete(){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //"where datetime(record_date) > ? and datetime(record_date) < ?"
        Cursor cursor = db.query(DownloadDB.DOWNLOAD_TABLE, new String[]{"task_icon", "task_name", "task_time","task_vid", "task_size", "task_type", "task_filePath", "contentId"},
                "task_status = ?", new String[]{"1"}, null, null, "task_time desc");

        if (cursor.getCount() > 0) {
            List<DownloadTask> tasks = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                DownloadTask task = new DownloadTask();
                task.setTask_icon(cursor.getString(0));
                task.setTask_name(cursor.getString(1));
                task.setTask_time(cursor.getString(2));
                task.setTask_vid(cursor.getString(3));
                task.setTask_size(cursor.getInt(4));
                task.setTask_type(cursor.getInt(5));
                task.setTask_filePath(cursor.getString(6));
                task.setContentId(cursor.getString(7));
                tasks.add(task);
            }
            cursor.close();
            return tasks;
        }else{
            cursor.close();
            return new ArrayList<DownloadTask>();
        }
    }

}
