package com.park61.teacherhelper.module.dict;

/**
 * 专门提供给startActivityForResult使用的返回码
 * Created by zhangchi on 2017/8/22.
 */
public enum RequestCode {

    REQUET_CODE_EDIT_CLAZZ(10001),
    REQUET_CODE_ADD_CLAZZ(10002),
    REQUET_CODE_QR_CAPTURE(10003)
    ;

    private int code;

    private RequestCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
