package com.park61.teacherhelper.module.dict;

/**
 * Created by zhangchi on 2017/8/22.
 */

public enum StatusCode {

    STATUS_CODE_SUCCESS("0", "请求成功"),
    STATUS_CODE_FAIL("0000000002", "请求失败");

    private String code;
    private String msg;

    private StatusCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
