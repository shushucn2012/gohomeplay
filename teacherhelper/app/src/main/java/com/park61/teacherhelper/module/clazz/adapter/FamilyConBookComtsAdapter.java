package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.FamilyConBookSeeAllStuActivity;
import com.park61.teacherhelper.module.clazz.bean.FamilyClazzComtItem;
import com.park61.teacherhelper.module.clazz.bean.StudentBean;
import com.park61.teacherhelper.module.course.adapter.MyCollectCourseListAdapter;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;
import com.park61.teacherhelper.widget.pw.SharePopWin;
import com.park61.teacherhelper.widget.webview.ShowImageFromWebActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 家园联系薄 RecyclerView 适配器
 *
 * @author shubei
 * @time 2018/11/22 18:00
 */
public class FamilyConBookComtsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PIC_ONE = 0;
    private static final int TYPE_PIC_FOUR = 1;
    private static final int TYPE_PIC_OTHER = 2;

    private int maxDescripLine = 6; //TextView默认最大展示行数

    private Context mContext;
    private LayoutInflater factory;
    private List<FamilyClazzComtItem> mlist;
    private OnOperateClickedLsner mOnOperateClickedLsner;

    public FamilyConBookComtsAdapter(Context context, List<FamilyClazzComtItem> list) {
        this.mContext = context;
        this.mlist = list;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    /**
     * @param position
     * @return 根据position位置设置不同视图类型
     */
    @Override
    public int getItemViewType(int position) {
        /*
         * 计算每一个位置对应的item类型
         * */
        if (mlist.get(position).getTeachCommentSourceList().size() == 1) {
            return TYPE_PIC_ONE;
        } else if (mlist.get(position).getTeachCommentSourceList().size() == 4) {
            return TYPE_PIC_FOUR;
        } else {
            return TYPE_PIC_OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
         * 根据不同的类型加载对应的布局
         *
         * */
        switch (viewType) {
            case TYPE_PIC_ONE:
                View viewOne = factory.inflate(R.layout.item_familyconbook_comts_one, parent, false);
                return new MyOneViewHolder(viewOne);
            case TYPE_PIC_FOUR:
                View viewFour = factory.inflate(R.layout.item_familyconbook_comts_four, parent, false);
                return new MyOtherViewHolder(viewFour);
            case TYPE_PIC_OTHER:
                View viewOther = factory.inflate(R.layout.item_familyconbook_comts_other, parent, false);
                return new MyOtherViewHolder(viewOther);
            default:
                return null;
        }
    }

    /**
     * @param holder
     * @param position 根据holder设置不同的数据
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyOneViewHolder) {
            setOnePicData((MyOneViewHolder) holder, position);
        } else if (holder instanceof MyFourViewHolder) {
            setFourPicData((MyFourViewHolder) holder, position);
        } else {
            setOtherPicData((MyOtherViewHolder) holder, position);
        }
    }

    /**
     * 初始化公共布局数据
     */
    public void initComData(MyBaseViewHolder holder, FamilyClazzComtItem familyClazzComtItem, int position) {
        ImageManager.getInstance().displayImg(holder.img_author_header, familyClazzComtItem.getIssuePic());
        holder.tv_author_name.setText(familyClazzComtItem.getIssueName());
        holder.tv_pub_time.setText(familyClazzComtItem.getShowIssueDate());
        holder.tv_desc.setText(familyClazzComtItem.getContent());

        //descriptionView设置默认显示高度
        //holder.tv_desc.setHeight(holder.tv_desc.getLineHeight() * maxDescripLine);
        if (familyClazzComtItem.isShowExpand() == null) {
            //根据高度来判断是否需要再点击展开
            holder.tv_desc.post(new Runnable() {

                @Override
                public void run() {
                    if (holder.tv_desc.getLineCount() > maxDescripLine) {//文字行数大于最大行数，显示展开，默认收起
                        holder.tv_desc.setMaxLines(maxDescripLine);
                        holder.expandView.setVisibility(View.VISIBLE);
                        familyClazzComtItem.setShowExpand(true);
                        familyClazzComtItem.setExpand(false);
                    } else {//文字行数小于最大行数，不显示展开，默认收起
                        holder.tv_desc.setMaxHeight(mContext.getResources().getDisplayMetrics().heightPixels);
                        holder.expandView.setVisibility(View.GONE);
                        familyClazzComtItem.setShowExpand(false);
                        familyClazzComtItem.setExpand(false);
                    }
                }
            });
        } else {
            if (familyClazzComtItem.isShowExpand()) {
                holder.expandView.setVisibility(View.VISIBLE);
                if (familyClazzComtItem.isExpand()) {
                    holder.expandView.setText("收起");
                    holder.tv_desc.setMaxHeight(mContext.getResources().getDisplayMetrics().heightPixels);
                } else {
                    holder.expandView.setText("展开");
                    holder.tv_desc.setMaxLines(maxDescripLine);
                }
            } else {
                holder.expandView.setVisibility(View.GONE);
                holder.tv_desc.setMaxHeight(mContext.getResources().getDisplayMetrics().heightPixels);
            }
        }

        holder.expandView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                familyClazzComtItem.setExpand(!familyClazzComtItem.isExpand());
                if (familyClazzComtItem.isExpand()) {//展示全部，按钮设置为点击收起。
                    holder.expandView.setText("收起");
                    holder.tv_desc.setMaxHeight(mContext.getResources().getDisplayMetrics().heightPixels);
                } else {//显示3行，按钮设置为点击显示全部。
                    holder.expandView.setText("展开");
                    holder.tv_desc.setMaxLines(maxDescripLine);
                }
            }
        });

        if (position == mlist.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        if (familyClazzComtItem.getTeachCommentChildList().size() == 1) {
            ImageManager.getInstance().displayImg(holder.img_stu0, familyClazzComtItem.getTeachCommentChildList().get(0).getPicUrl());
            holder.img_stu0.setVisibility(View.VISIBLE);
            holder.img_stu1.setVisibility(View.GONE);
            holder.img_stu2.setVisibility(View.GONE);
            holder.img_stu3.setVisibility(View.GONE);
            holder.tv_shenglue.setVisibility(View.GONE);
        } else if (familyClazzComtItem.getTeachCommentChildList().size() == 2) {
            ImageManager.getInstance().displayImg(holder.img_stu0, familyClazzComtItem.getTeachCommentChildList().get(0).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu1, familyClazzComtItem.getTeachCommentChildList().get(1).getPicUrl());
            holder.img_stu0.setVisibility(View.VISIBLE);
            holder.img_stu1.setVisibility(View.VISIBLE);
            holder.img_stu2.setVisibility(View.GONE);
            holder.img_stu3.setVisibility(View.GONE);
            holder.tv_shenglue.setVisibility(View.GONE);
        } else if (familyClazzComtItem.getTeachCommentChildList().size() == 3) {
            ImageManager.getInstance().displayImg(holder.img_stu0, familyClazzComtItem.getTeachCommentChildList().get(0).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu1, familyClazzComtItem.getTeachCommentChildList().get(1).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu2, familyClazzComtItem.getTeachCommentChildList().get(2).getPicUrl());
            holder.img_stu0.setVisibility(View.VISIBLE);
            holder.img_stu1.setVisibility(View.VISIBLE);
            holder.img_stu2.setVisibility(View.VISIBLE);
            holder.img_stu3.setVisibility(View.GONE);
            holder.tv_shenglue.setVisibility(View.GONE);
        } else if (familyClazzComtItem.getTeachCommentChildList().size() == 4) {
            ImageManager.getInstance().displayImg(holder.img_stu0, familyClazzComtItem.getTeachCommentChildList().get(0).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu1, familyClazzComtItem.getTeachCommentChildList().get(1).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu2, familyClazzComtItem.getTeachCommentChildList().get(2).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu3, familyClazzComtItem.getTeachCommentChildList().get(3).getPicUrl());
            holder.img_stu0.setVisibility(View.VISIBLE);
            holder.img_stu1.setVisibility(View.VISIBLE);
            holder.img_stu2.setVisibility(View.VISIBLE);
            holder.img_stu3.setVisibility(View.VISIBLE);
            holder.tv_shenglue.setVisibility(View.GONE);
        } else if (familyClazzComtItem.getTeachCommentChildList().size() > 4) {
            ImageManager.getInstance().displayImg(holder.img_stu0, familyClazzComtItem.getTeachCommentChildList().get(0).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu1, familyClazzComtItem.getTeachCommentChildList().get(1).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu2, familyClazzComtItem.getTeachCommentChildList().get(2).getPicUrl());
            ImageManager.getInstance().displayImg(holder.img_stu3, familyClazzComtItem.getTeachCommentChildList().get(3).getPicUrl());
            holder.img_stu0.setVisibility(View.VISIBLE);
            holder.img_stu1.setVisibility(View.VISIBLE);
            holder.img_stu2.setVisibility(View.VISIBLE);
            holder.img_stu3.setVisibility(View.VISIBLE);
            holder.tv_shenglue.setVisibility(View.VISIBLE);
        } else {
            holder.img_stu0.setVisibility(View.GONE);
            holder.img_stu1.setVisibility(View.GONE);
            holder.img_stu2.setVisibility(View.GONE);
            holder.img_stu3.setVisibility(View.GONE);
            holder.tv_shenglue.setVisibility(View.GONE);
        }
        holder.area_goto_seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FamilyConBookSeeAllStuActivity.class);
                List<StudentBean> studentBeanList = new ArrayList<>();
                for (int i = 0; i < familyClazzComtItem.getTeachCommentChildList().size(); i++) {
                    StudentBean studentBean = new StudentBean();
                    studentBean.setUserChildId(familyClazzComtItem.getTeachCommentChildList().get(i).getUserChildId());
                    studentBean.setName(familyClazzComtItem.getTeachCommentChildList().get(i).getUserChildName());
                    studentBean.setPictureUrl(familyClazzComtItem.getTeachCommentChildList().get(i).getPicUrl());
                    studentBeanList.add(studentBean);
                }
                it.putExtra("STUDENT_LIST", (Serializable) studentBeanList);
                mContext.startActivity(it);
            }
        });
        holder.area_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateClickedLsner != null)
                    mOnOperateClickedLsner.onDelOrEdit(v, position);
            }
        });
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/teach/#/homegarten/index/" + mlist.get(position).getId();
                Intent it = new Intent(mContext, SharePopWin.class);
                it.putExtra("shareUrl", shareUrl);
                it.putExtra("picUrl", AppUrl.SHARE_APP_ICON);
                it.putExtra("title", mContext.getResources().getString(R.string.app_name));
                it.putExtra("description", "宝宝在幼儿园的表现，快来看看吧");
                mContext.startActivity(it);
            }
        });
    }

    private void setOnePicData(MyOneViewHolder holder, int position) {
        final FamilyClazzComtItem familyClazzComtItem = mlist.get(position);
        initComData(holder, familyClazzComtItem, position);

        //ImageManager.getInstance().displayImg(holder.img_photo, familyClazzComtItem.getTeachCommentSourceList().get(0).getSource());

        //获取图片真正的宽高
        Glide.with(mContext).asBitmap().load(familyClazzComtItem.getTeachCommentSourceList().get(0).getSource()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Log.out("width ===============" + width); //900px
                Log.out("height ================" + height); //500px

                if (width > height) {
                    ViewGroup.LayoutParams layoutParams = holder.img_photo.getLayoutParams();
                    layoutParams.width = DevAttr.getScreenWidth(mContext) - DevAttr.dip2px(mContext, 150);
                    double percent = height * 1.0 / width;
                    layoutParams.height = (int) (layoutParams.width * percent);
                } else {
                    ViewGroup.LayoutParams layoutParams = holder.img_photo.getLayoutParams();
                    layoutParams.width = DevAttr.getScreenWidth(mContext) - DevAttr.dip2px(mContext, 150);
                    layoutParams.height = DevAttr.dip2px(mContext, 200);
                }
                holder.img_photo.setImageBitmap(bitmap);
            }
        });

        holder.img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.out("=====================img_photo==========onItemClick=====================");
                startShowImageActivity(familyClazzComtItem.getTeachCommentSourceList().get(0).getSource(), familyClazzComtItem);
            }
        });
    }

    private void setFourPicData(MyFourViewHolder holder, int position) {
        final FamilyClazzComtItem familyClazzComtItem = mlist.get(position);
        initComData(holder, familyClazzComtItem, position);

        if (CommonMethod.isListEmpty(familyClazzComtItem.getTeachCommentSourceList())) {
            List<FamilyClazzComtItem.TeachCommentSourceListBean> urlList = new ArrayList<>();
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
        } else {
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(familyClazzComtItem.getTeachCommentSourceList()));
        }
        holder.gv_eva_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startShowImageActivity(familyClazzComtItem.getTeachCommentSourceList().get(position).getSource(), familyClazzComtItem);
            }
        });
    }

    private void setOtherPicData(MyOtherViewHolder holder, int position) {
        final FamilyClazzComtItem familyClazzComtItem = mlist.get(position);
        initComData(holder, familyClazzComtItem, position);

        if (CommonMethod.isListEmpty(familyClazzComtItem.getTeachCommentSourceList())) {
            List<FamilyClazzComtItem.TeachCommentSourceListBean> urlList = new ArrayList<>();
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
        } else {
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(familyClazzComtItem.getTeachCommentSourceList()));
        }
        holder.gv_eva_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.out("=====================gv_eva_pic==========onItemClick=====================");
                startShowImageActivity(familyClazzComtItem.getTeachCommentSourceList().get(position).getSource(), familyClazzComtItem);
            }
        });
    }

    /**
     * 通用ViewHolder
     */
    public class MyBaseViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_author_header;
        TextView tv_author_name;
        TextView tv_pub_time;
        TextView tv_desc;
        TextView tv_shenglue;
        View bottom_line, area_goto_seeall, area_operate, descriptionView;
        ImageView img_stu0, img_stu1, img_stu2, img_stu3, img_share;
        Button expandView;

        public MyBaseViewHolder(View itemView) {
            super(itemView);
            img_author_header = itemView.findViewById(R.id.img_author_header);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            tv_pub_time = itemView.findViewById(R.id.tv_pub_time);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            bottom_line = itemView.findViewById(R.id.bottom_line);
            img_stu0 = itemView.findViewById(R.id.img_stu0);
            img_stu1 = itemView.findViewById(R.id.img_stu1);
            img_stu2 = itemView.findViewById(R.id.img_stu2);
            img_stu3 = itemView.findViewById(R.id.img_stu3);
            img_share = itemView.findViewById(R.id.img_share);
            tv_shenglue = itemView.findViewById(R.id.tv_shenglue);
            area_goto_seeall = itemView.findViewById(R.id.area_goto_seeall);
            area_operate = itemView.findViewById(R.id.area_operate);
            descriptionView = itemView.findViewById(R.id.description_layout);
            expandView = itemView.findViewById(R.id.expand_view);
        }
    }

    /**
     * 一张图ViewHolder
     */
    public class MyOneViewHolder extends MyBaseViewHolder {

        ImageView img_photo;

        public MyOneViewHolder(View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
        }
    }

    /**
     * 4张图ViewHolder
     */
    public class MyFourViewHolder extends MyBaseViewHolder {

        GridViewForScrollView gv_eva_pic;

        public MyFourViewHolder(View itemView) {
            super(itemView);
            gv_eva_pic = itemView.findViewById(R.id.gv_eva_pic);
        }
    }

    /**
     * 多张图ViewHolder
     */
    public class MyOtherViewHolder extends MyBaseViewHolder {

        GridViewForScrollView gv_eva_pic;

        public MyOtherViewHolder(View itemView) {
            super(itemView);
            gv_eva_pic = itemView.findViewById(R.id.gv_eva_pic);
        }
    }

    private class GvEvaPicAdapter extends BaseAdapter {

        private List<FamilyClazzComtItem.TeachCommentSourceListBean> listPictrue;

        public GvEvaPicAdapter(List<FamilyClazzComtItem.TeachCommentSourceListBean> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public FamilyClazzComtItem.TeachCommentSourceListBean getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_show_item2, null);
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            ImageManager.getInstance().displayScaleImage(mContext, img_input, listPictrue.get(position).getSource(), null);
            return convertView;
        }
    }

    public void startShowImageActivity(String url, FamilyClazzComtItem item) {
        List<String> listImgSrc = new ArrayList<>();
        for (int i = 0; i < item.getTeachCommentSourceList().size(); i++) {
            listImgSrc.add(item.getTeachCommentSourceList().get(i).getSource());
        }
        Intent intent = new Intent();
        intent.putExtra(ShowImageFromWebActivity.IMAGE_URL, url);
        intent.putStringArrayListExtra(ShowImageFromWebActivity.IMAGE_URL_ALL, (ArrayList<String>) listImgSrc);
        intent.setClass(mContext, ShowImageFromWebActivity.class);
        mContext.startActivity(intent);
    }

    public interface OnOperateClickedLsner {
        void onDelOrEdit(View view, int pos);

        void onShare(int pos);
    }

    public void setOnOperateClickedLsner(OnOperateClickedLsner lsner) {
        this.mOnOperateClickedLsner = lsner;
    }

}
