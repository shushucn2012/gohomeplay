package com.park61.teacherhelper.module.dict;

/**
 * 专门提供给startActivityForResult使用的返回码
 * Created by zhangchi on 2017/8/22.
 */
public enum ResultCode {

    RESULT_CODE_SUCCESS(1, "操作成功"),
    RESULT_CODE_FAIL(2, "操作失败");

    private int code;
    private String msg;

    private ResultCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
