package com.park61.teacherhelper.common.tool;

/**
 * @作者: TJ
 * @时间: 2018/8/20 14:36
 * @描述: EventBus事件
 */
public class MessageEvent {

    private String message;
    private int    arg;
    private Object object;

    public MessageEvent() {

    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, int arg) {
        this.message = message;
        this.arg = arg;
    }

    public MessageEvent(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public MessageEvent(String message, int arg, Object object) {
        this.message = message;
        this.arg = arg;
        this.object = object;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getArg() {
        return arg;
    }

    public void setArg(int arg) {
        this.arg = arg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


}
