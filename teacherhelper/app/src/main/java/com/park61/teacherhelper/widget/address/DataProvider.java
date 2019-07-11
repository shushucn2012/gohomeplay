package com.park61.teacherhelper.widget.address;

import java.util.List;

/**
 * Created by dun on 17/2/9.
 */

public interface DataProvider {
    void provideData(int currentDeep, long preId, String preName, DataReceiver receiver);


    interface DataReceiver {
        void send(List<ISelectAble> data);
    }
}
