package com.park61.teacherhelper.module.umeng;

import java.io.Serializable;
import java.util.Map;

/**
 * 友盟消息格式
 * Created by zhangchi on 2017/9/20.
 */

public class UmengResponse implements Serializable {
    private String displayType;
    private Map<String, String> extra;
    private String msgId;

    private Body body;

    public class Body {
        private String afterOpen;
        private String activity;

        public String getAfterOpen() {
            return afterOpen;
        }

        public void setAfterOpen(String afterOpen) {
            this.afterOpen = afterOpen;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "afterOpen='" + afterOpen + '\'' +
                    ", activity='" + activity + '\'' +
                    '}';
        }
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "UmengResponse{" +
                "displayType='" + displayType + '\'' +
                ", extra=" + extra +
                ", msgId='" + msgId + '\'' +
                ", body=" + body +
                '}';
    }
}
