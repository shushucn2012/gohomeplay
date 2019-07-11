package com.park61.teacherhelper.module.my.bean;

/**
 * Created by chenlie on 2018/1/8.
 */

public class ColorSizeBean {

    private String productColor;
    private String productSize;
    private boolean hasChecked;
    private boolean hasShow;

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public boolean isHasShow() {
        return hasShow;
    }

    public void setHasShow(boolean hasShow) {
        this.hasShow = hasShow;
    }
}
