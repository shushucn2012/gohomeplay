package com.park61.teacherhelper.module.okdownload.db;

/**
 * Created by chenlie on 2018/2/2.
 *
 * 下载任务存本地数据库
 */

public class DownloadTask {

    private String task_icon;
    private String task_name;
    // 0视频 1音频
    private int task_type = 0;
    // 文件大小字节数
    private int task_size;
    //1完成，0未完成, 2下载中
    private int task_status = 0;
    private String task_vid;
    // 后台是根据sourceId 获取 playAuth
    private String sourceId;
    //增加contentId
    private String contentId;
    private String task_time;
    //文件路径, 阿里云下载的为 vid_format_m3u8.mp4
    private String task_filePath;
    private String task_extra1;
    private String task_extra2;

    public String getTask_icon() {
        return task_icon;
    }

    public void setTask_icon(String task_icon) {
        this.task_icon = task_icon;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getTask_type() {
        return task_type;
    }

    /**
     * @param task_type 0视频 1音频
     */
    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public int getTask_size() {
        return task_size;
    }

    public void setTask_size(int task_size) {
        this.task_size = task_size;
    }

    public int getTask_status() {
        return task_status;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }

    public String getTask_vid() {
        return task_vid;
    }

    public void setTask_vid(String task_vid) {
        this.task_vid = task_vid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public String getTask_filePath() {
        return task_filePath;
    }

    public void setTask_filePath(String task_filePath) {
        this.task_filePath = task_filePath;
    }

    public String getTask_extra1() {
        return task_extra1;
    }

    public void setTask_extra1(String task_extra1) {
        this.task_extra1 = task_extra1;
    }

    public String getTask_extra2() {
        return task_extra2;
    }

    public void setTask_extra2(String task_extra2) {
        this.task_extra2 = task_extra2;
    }
}
