package com.park61.teacherhelper.module.activity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubei on 2017/11/2.
 */

public class Double11TicketDetailsBean implements Parcelable{


    /**
     * createDate : 测试内容0w0h
     * id : 43104
     * payTime : 测试内容768o
     * signDate : 测试内容bgmy
     * signStatus : 10840
     * so : {"id":48734,"orderAmount":36710,"orderCancelDate":"测试内容9088","orderPaidByCoupon":51551,"productAmount":21715}
     * status : 18073
     * teachActivity : {"address":"测试内容v282","coverImg":"测试内容xt18","id":84257,"name":"测试内容hpxw","price":45833}
     * userMobile : 测试内容j1j9
     * userName : 测试内容5y4i
     */

    private String createDate;
    private int id;
    private String payTime;
    private String signDate;
    private int signStatus;
    private SoBean mySoVo;
    private int status;
    private TeachActivityBean teachActivityVO;
    private String userMobile;
    private String userName;
    private int comboStatus;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public int getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getComboStatus() {
        return comboStatus;
    }

    public void setComboStatus(int comboStatus) {
        this.comboStatus = comboStatus;
    }

    public TeachActivityBean getTeachActivityVO() {
        return teachActivityVO;
    }

    public void setTeachActivityVO(TeachActivityBean teachActivityVO) {
        this.teachActivityVO = teachActivityVO;
    }

    public SoBean getMySoVo() {
        return mySoVo;
    }

    public void setMySoVo(SoBean mySoVo) {
        this.mySoVo = mySoVo;
    }


    public static class SoBean implements Parcelable{
        /**
         * id : 48734
         * orderAmount : 36710
         * orderCancelDate : 测试内容9088
         * orderPaidByCoupon : 51551
         * productAmount : 21715
         */

        private long id;
        private String orderAmount;
        private String orderCancelDate;
        private String orderPaidByCoupon;
        private String productAmount;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderCancelDate() {
            return orderCancelDate;
        }

        public void setOrderCancelDate(String orderCancelDate) {
            this.orderCancelDate = orderCancelDate;
        }

        public String getOrderPaidByCoupon() {
            return orderPaidByCoupon;
        }

        public void setOrderPaidByCoupon(String orderPaidByCoupon) {
            this.orderPaidByCoupon = orderPaidByCoupon;
        }

        public String getProductAmount() {
            return productAmount;
        }

        public void setProductAmount(String productAmount) {
            this.productAmount = productAmount;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.orderAmount);
            dest.writeString(this.orderCancelDate);
            dest.writeString(this.orderPaidByCoupon);
            dest.writeString(this.productAmount);
        }

        public SoBean() {
        }

        protected SoBean(Parcel in) {
            this.id = in.readLong();
            this.orderAmount = in.readString();
            this.orderCancelDate = in.readString();
            this.orderPaidByCoupon = in.readString();
            this.productAmount = in.readString();
        }

        public static final Creator<SoBean> CREATOR = new Creator<SoBean>() {
            @Override
            public SoBean createFromParcel(Parcel source) {
                return new SoBean(source);
            }

            @Override
            public SoBean[] newArray(int size) {
                return new SoBean[size];
            }
        };
    }

    public static class TeachActivityBean implements Parcelable{
        /**
         * address : 测试内容v282
         * coverImg : 测试内容xt18
         * id : 84257
         * name : 测试内容hpxw
         * price : 45833
         */

        private String address;
        private String coverImg;
        private int id;
        private String name;
        private String price;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.address);
            dest.writeString(this.coverImg);
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.price);
        }

        public TeachActivityBean() {
        }

        protected TeachActivityBean(Parcel in) {
            this.address = in.readString();
            this.coverImg = in.readString();
            this.id = in.readInt();
            this.name = in.readString();
            this.price = in.readString();
        }

        public static final Creator<TeachActivityBean> CREATOR = new Creator<TeachActivityBean>() {
            @Override
            public TeachActivityBean createFromParcel(Parcel source) {
                return new TeachActivityBean(source);
            }

            @Override
            public TeachActivityBean[] newArray(int size) {
                return new TeachActivityBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createDate);
        dest.writeInt(this.id);
        dest.writeString(this.payTime);
        dest.writeString(this.signDate);
        dest.writeInt(this.signStatus);
        dest.writeParcelable(this.mySoVo, flags);
        dest.writeInt(this.status);
        dest.writeParcelable(this.teachActivityVO, flags);
        dest.writeString(this.userMobile);
        dest.writeString(this.userName);
        dest.writeInt(this.comboStatus);
    }

    public Double11TicketDetailsBean() {
    }

    protected Double11TicketDetailsBean(Parcel in) {
        this.createDate = in.readString();
        this.id = in.readInt();
        this.payTime = in.readString();
        this.signDate = in.readString();
        this.signStatus = in.readInt();
        this.mySoVo = in.readParcelable(SoBean.class.getClassLoader());
        this.status = in.readInt();
        this.teachActivityVO = in.readParcelable(TeachActivityBean.class.getClassLoader());
        this.userMobile = in.readString();
        this.userName = in.readString();
        this.comboStatus = in.readInt();
    }

    public static final Creator<Double11TicketDetailsBean> CREATOR = new Creator<Double11TicketDetailsBean>() {
        @Override
        public Double11TicketDetailsBean createFromParcel(Parcel source) {
            return new Double11TicketDetailsBean(source);
        }

        @Override
        public Double11TicketDetailsBean[] newArray(int size) {
            return new Double11TicketDetailsBean[size];
        }
    };
}
