package com.park61.teacherhelper.module.home.bean;

import com.park61.teacherhelper.module.home.bean.CommentItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/10/16.
 */

public class CommentListBean implements Serializable {

    private int offset;
    private String order;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private List<CommentItemBean> rows;
    private String sort;
    private int total;


    public int getOffset() {
        return offset;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public List<CommentItemBean> getRows() {
        return rows;
    }

    public String getOrder() {
        return order;
    }

    public String getSort() {
        return sort;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRows(List<CommentItemBean> rows) {
        this.rows = rows;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
