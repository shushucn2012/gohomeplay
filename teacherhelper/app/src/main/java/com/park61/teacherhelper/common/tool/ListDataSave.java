package com.park61.teacherhelper.common.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.module.home.bean.AdvertsieBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/12/15.
 */


public class ListDataSave {

    public static String PREFERENCENAME_ADVERTISING = "ADVERTISING";
    public static String LIST_TAG_ADVERTISING = "ADVERTISING_LIST";
    public static String SAVE_TAG_ADVERTISING = "isShowAdvertising";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext) {
        preferences = mContext.getSharedPreferences(PREFERENCENAME_ADVERTISING, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setAdvertiseNoShow() {
        editor.putBoolean(SAVE_TAG_ADVERTISING, false);
        editor.commit();
    }

    public void setAdvertiseShow() {
        editor.putBoolean(SAVE_TAG_ADVERTISING, true);
        editor.commit();
    }

    public boolean getAdvertiseShow() {
        return preferences.getBoolean(SAVE_TAG_ADVERTISING, false);
    }

    /**
     * 保存List
     */
    public void setDataList(List<AdvertsieBean> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        //editor.clear();
        editor.putString(LIST_TAG_ADVERTISING, strJson);
        editor.commit();
    }

    /**
     * 获取List
     */
    public List<AdvertsieBean> getDataList() {
        List<AdvertsieBean> datalist = new ArrayList<AdvertsieBean>();
        String strJson = preferences.getString(LIST_TAG_ADVERTISING, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        datalist = gson.fromJson(strJson, new TypeToken<List<AdvertsieBean>>() {
        }.getType());
        return datalist;

    }
}
