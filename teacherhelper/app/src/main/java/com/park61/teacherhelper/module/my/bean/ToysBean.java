package com.park61.teacherhelper.module.my.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlie on 2017/12/27.
 * <p>
 * 教师活动列表bean
 */

public class ToysBean implements Parcelable{

    /**
     * "currentUnifyPrice": 188,
     * "id": 66,
     * "marketPrice": 198,
     * "productCname": "北极熊女生礼物可爱",
     * "productPicUrl": "http://ghpprod.oss-cn-qingdao.aliyuncs.com/product/20170604152222899_224.png?x-oss-process=style/compress_nologo"
     * "productSize": "120mm*150mm"
     * "productColor": "蓝色",
     */

    //商品id
    private int id;
    private Double currentUnifyPrice;
    private Double marketPrice;
    private String productCname;
    private String productColor;
    private String productPicUrl;
    private String productSize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getCurrentUnifyPrice() {
        return currentUnifyPrice;
    }

    public void setCurrentUnifyPrice(Double currentUnifyPrice) {
        this.currentUnifyPrice = currentUnifyPrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getProductCname() {
        return productCname;
    }

    public void setProductCname(String productCname) {
        this.productCname = productCname;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeValue(this.currentUnifyPrice);
        dest.writeValue(this.marketPrice);
        dest.writeString(this.productCname);
        dest.writeString(this.productColor);
        dest.writeString(this.productPicUrl);
        dest.writeString(this.productSize);
    }

    public ToysBean() {
    }

    protected ToysBean(Parcel in) {
        this.id = in.readInt();
        this.currentUnifyPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.marketPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.productCname = in.readString();
        this.productColor = in.readString();
        this.productPicUrl = in.readString();
        this.productSize = in.readString();
    }

    public static final Creator<ToysBean> CREATOR = new Creator<ToysBean>() {
        @Override
        public ToysBean createFromParcel(Parcel source) {
            return new ToysBean(source);
        }

        @Override
        public ToysBean[] newArray(int size) {
            return new ToysBean[size];
        }
    };
}
