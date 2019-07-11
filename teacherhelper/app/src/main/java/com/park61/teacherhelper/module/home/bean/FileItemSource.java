package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2018/2/1.
 */

public class FileItemSource implements Serializable {


    /**
     * fileFormat : PDF
     * iconUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20180129141734122_531.png
     * id : 84
     * showFileSize : 813.00KB
     * sourceName : pdf.pdf
     * sourceUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20171018145553366_546.pdf
     * totalDownloadNum : 2000
     */

    private String fileFormat;
    private String iconUrl;
    private int id;
    private String showFileSize;
    private String sourceName;
    private String sourceUrl;
    private String totalDownloadNum;
    private int status;//下载状态：-1未下载，0未完成, 1完成， 2下载中

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowFileSize() {
        return showFileSize;
    }

    public void setShowFileSize(String showFileSize) {
        this.showFileSize = showFileSize;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTotalDownloadNum() {
        return totalDownloadNum;
    }

    public void setTotalDownloadNum(String totalDownloadNum) {
        this.totalDownloadNum = totalDownloadNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
