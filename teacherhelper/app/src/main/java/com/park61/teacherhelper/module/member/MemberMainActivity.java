package com.park61.teacherhelper.module.member;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.MyTrainApplyListActivity;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.member.bean.MemberType;
import com.park61.teacherhelper.module.member.bean.VipGroupMemberBean;
import com.park61.teacherhelper.module.my.MyTiBaoActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;
import com.park61.teacherhelper.widget.dialog.SingleBtnDialog;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员首页
 *
 * @author shubei
 * @time 2019/1/18 16:46
 */
public class MemberMainActivity extends BaseActivity {

    private ImageView img_user, img_invite_member, img_manage, img_auth_status, img_glod_vip, img_exp_vip;
    private TextView tv_username, tv_vip_expire_date, tv_member_one_price, tv_member_one_intro, tv_member_group_price, tv_member_group_intro,
            tv_no_vip;
    private View area_null_members, area_group_members, area_goto_auth;
    private ShowImageWebView wv_content_start;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;
    private View vip_type0_bg, vip_type1_bg;

    private GridViewForScrollView gv_gourp_members;
    private GvMembersAdapter gvMembersAdapter;
    private List<VipGroupMemberBean> memberList = new ArrayList<>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_member_main);
    }

    @Override
    public void initView() {
        img_user = findViewById(R.id.img_user);
        tv_username = findViewById(R.id.tv_username);
        tv_vip_expire_date = findViewById(R.id.tv_vip_expire_date);
        gv_gourp_members = findViewById(R.id.gv_gourp_members);
        area_null_members = findViewById(R.id.area_null_members);
        img_invite_member = findViewById(R.id.img_invite_member);
        img_manage = findViewById(R.id.img_manage);
        wv_content_start = findViewById(R.id.wv_content_start);
        mPullRefreshScrollView = findViewById(R.id.pull_refresh_scrollview);
        tv_member_one_price = findViewById(R.id.tv_member_one_price);
        tv_member_one_intro = findViewById(R.id.tv_member_one_intro);
        tv_member_group_price = findViewById(R.id.tv_member_group_price);
        tv_member_group_intro = findViewById(R.id.tv_member_group_intro);
        area_group_members = findViewById(R.id.area_group_members);
        area_goto_auth = findViewById(R.id.area_goto_auth);
        img_auth_status = findViewById(R.id.img_auth_status);
        img_glod_vip = findViewById(R.id.img_glod_vip);
        img_exp_vip = findViewById(R.id.img_exp_vip);
        tv_no_vip = findViewById(R.id.tv_no_vip);
        vip_type0_bg = findViewById(R.id.vip_type0_bg);
        vip_type1_bg = findViewById(R.id.vip_type1_bg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncMemberGroupAuthenticateStatus();
    }

    @Override
    public void initData() {
        ImageManager.getInstance().displayImg(img_user, GlobalParam.currentUser.getPictureUrl(), R.mipmap.mine_icon);
        tv_username.setText(GlobalParam.currentUser.getName());
        tv_vip_expire_date.setText(GlobalParam.currentUser.getExpireDate());

        if (GlobalParam.currentUser.getIsMember() == -1) {//非会员
            img_glod_vip.setVisibility(View.INVISIBLE);
            img_exp_vip.setVisibility(View.INVISIBLE);
            tv_no_vip.setVisibility(View.VISIBLE);
            tv_vip_expire_date.setVisibility(View.INVISIBLE);
        } else if (GlobalParam.currentUser.getIsMember() == 0) {//体验会员
            img_glod_vip.setVisibility(View.INVISIBLE);
            img_exp_vip.setVisibility(View.VISIBLE);
            tv_no_vip.setVisibility(View.INVISIBLE);
            tv_vip_expire_date.setVisibility(View.VISIBLE);
            tv_vip_expire_date.setText("        " + GlobalParam.currentUser.getExpireDate());
        } else if (GlobalParam.currentUser.getIsMember() == 1) {//黄金会员
            img_glod_vip.setVisibility(View.VISIBLE);
            img_exp_vip.setVisibility(View.INVISIBLE);
            tv_no_vip.setVisibility(View.INVISIBLE);
            tv_vip_expire_date.setVisibility(View.VISIBLE);
        } else {
            img_glod_vip.setVisibility(View.INVISIBLE);
            img_exp_vip.setVisibility(View.INVISIBLE);
            tv_no_vip.setVisibility(View.INVISIBLE);
            tv_vip_expire_date.setVisibility(View.INVISIBLE);
        }

        asyncGetMemberRightIntro();
        asyncGetMemberTypeList();

       /* memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());
        memberList.add(new VipGroupMemberBean());*/
        gvMembersAdapter = new GvMembersAdapter();
        gv_gourp_members.setAdapter(gvMembersAdapter);
    }

    @Override
    public void initListener() {
        gv_gourp_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, MemberListActivity.class));
            }
        });
        img_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MemberListActivity.class));
            }
        });
        findViewById(R.id.img_invite_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareInfoBean shareInfoBean = new ShareInfoBean();
                StringBuilder sb = new StringBuilder();
                sb.append("http://m.61park.cn/teach/#/member/invite/");
                sb.append(android.net.Uri.encode(GlobalParam.currentUser.getName()));//为了避免空格变“+”的问题
                sb.append("/");
                sb.append(GlobalParam.currentUser.getGroupId());
                shareInfoBean.setShareUrl(sb.toString());
                shareInfoBean.setPicUrl(AppUrl.SHARE_APP_ICON);
                shareInfoBean.setTitle("61学院会员免费认证，快来加入吧");
                shareInfoBean.setDescription("通过我的邀请，即可快速成为会员哦");
                showShareDialog(shareInfoBean);
            }
        });
        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        scrollView.setCallbacks(mCallbacks);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mCallbacks.onScrollChanged(scrollView.getScrollY());
            }
        });
    }

    /**
     * 滑动黏贴view的监听事件
     */
    private ObservableScrollView.Callbacks mCallbacks = new ObservableScrollView.Callbacks() {

        @Override
        public void onUpOrCancelMotionEvent() {
        }

        @Override
        public void onScrollChanged(int scrollY) {
            if (scrollY > DevAttr.dip2px(mContext, 90)) {// 控制TOPBAR的渐变效果
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
            } else if (scrollY > DevAttr.dip2px(mContext, 30)) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
            } else {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#00000000"));
            }
        }

        @Override
        public void onDownMotionEvent() {
        }
    };

    /*
     * 查询园所会员认证状态
     */
    public void asyncMemberGroupAuthenticateStatus() {
        String wholeUrl = AppUrl.host + AppUrl.memberGroupAuthenticateStatus;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String res = jsonResult.optString("data");
            if ("-1".equals(res)) {//不存在记录
                img_auth_status.setImageResource(R.mipmap.icon_goto_auth);
                img_auth_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, GroupVipApplyActivity.class));
                    }
                });
            } else if ("0".equals(res)) {//待审核
                img_auth_status.setImageResource(R.mipmap.icon_doing_auth);
                img_auth_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent it = new Intent(mContext, MyTiBaoActivity.class);
                        it.putExtra("PAGE_TITLE", "提报记录");
                        it.putExtra("url", "http://m.61park.cn/teach/#/training/list");
                        startActivity(it);*/
                        //startActivity(new Intent(mContext, MyTrainApplyListActivity.class));
                    }
                });
            } else if ("1".equals(res)) {//审核通过
                asyncUserIsKindergartenAdmin();
                img_auth_status.setImageResource(R.mipmap.icon_goto_auth);
                img_auth_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, GroupVipApplyActivity.class));
                    }
                });
            } else if ("2".equals(res)) {//审核拒绝
                img_auth_status.setImageResource(R.mipmap.icon_goto_auth);
                img_auth_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, GroupVipApplyActivity.class));
                    }
                });
            }

            if (GlobalParam.currentUser.getIsMember() == 0) {//体验会员
                area_goto_auth.setVisibility(View.VISIBLE);
                area_group_members.setVisibility(View.GONE);
            } else if (GlobalParam.currentUser.getIsMember() == 1) {//黄金会员
                if (GlobalParam.currentUser.getIsTeachGroupAuthShow() == 0) {
                    area_goto_auth.setVisibility(View.GONE);
                    area_group_members.setVisibility(View.VISIBLE);
                    //-----注意------start//
                    asyncUserIsKindergartenAdmin();
                    //-----注意------end//
                } else if (GlobalParam.currentUser.getIsTeachGroupAuthShow() == 1) {
                    area_goto_auth.setVisibility(View.VISIBLE);
                    area_group_members.setVisibility(View.GONE);
                }
            } else {//非会员
                area_goto_auth.setVisibility(View.VISIBLE);
                area_group_members.setVisibility(View.GONE);
            }
        }
    };

    /*
     * 用户是否幼儿园管理员
     */
    public void asyncUserIsKindergartenAdmin() {
        String wholeUrl = AppUrl.host + AppUrl.userIsKindergartenAdmin;//"http://10.10.10.18:8380/service/teachMember/userIsKindergartenAdmin";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            if ("0".equals(jsonResult.optString("data"))) {//是否管理员：0不是，1是
                GlobalParam.isGroupManager = false;
                img_manage.setVisibility(View.GONE);
                img_invite_member.setVisibility(View.GONE);
            } else {
                GlobalParam.isGroupManager = true;
                img_manage.setVisibility(View.VISIBLE);
                img_invite_member.setVisibility(View.VISIBLE);
            }
            asyncGetMemberList();
        }
    };

    private void asyncGetMemberList() {
        String wholeUrl = AppUrl.host + AppUrl.teachGroupMemberList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray actJay = jsonResult.optJSONArray("teachGroupMemberList");
            memberList.clear();
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < Math.min(actJay.length(), 10); i++) {
                    memberList.add(gson.fromJson(actJay.optJSONObject(i).toString(), VipGroupMemberBean.class));
                }
                area_null_members.setVisibility(View.GONE);//有成员时不显示暂无提示与邀请按钮
            } else {
                area_null_members.setVisibility(View.VISIBLE);//无成员时显示暂无提示与邀请按钮
            }
            gvMembersAdapter.notifyDataSetChanged();
        }
    };

    private class GvMembersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public VipGroupMemberBean getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_vip_groupmembers, null);
            CircleImageView img_member_header = convertView.findViewById(R.id.img_member_header);
            ImageView img_admin_label = convertView.findViewById(R.id.img_admin_label);
            //ImageManager.getInstance().displayImg(img_member_header, memberList.get(position).getPictureUrl(), R.mipmap.mine_icon);
            if (position == 9) {
                img_member_header.setImageResource(R.mipmap.icon_more_members);
            } else {
                ImageManager.getInstance().displayImg(img_member_header, memberList.get(position).getPictureUrl(), R.mipmap.mine_icon);
            }
            if (memberList.get(position).getIsAdmin() == 0) {
                img_admin_label.setVisibility(View.GONE);
            } else {
                img_admin_label.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    /*
     * 会员权益文案
     */
    public void asyncGetMemberRightIntro() {
        String wholeUrl = AppUrl.host + AppUrl.memberRightIntro;//"http://10.10.10.18:8380/service/teachMember/userIsKindergartenAdmin";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, glistener);
    }

    BaseRequestListener glistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.initShowimgWebview(wv_content_start);
            ViewInitTool.setWebData(wv_content_start, jsonResult.optString("data"));
        }
    };

    /*
     * 会员类型列表
     */
    public void asyncGetMemberTypeList() {
        String wholeUrl = AppUrl.host + AppUrl.memberTypeList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, mlistener);
    }

    BaseRequestListener mlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            List<MemberType> typeList = new ArrayList<>();
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    typeList.add(gson.fromJson(actJay.optJSONObject(i).toString(), MemberType.class));
                }
                tv_member_one_price.setText(FU.strDbFmt(typeList.get(0).getSalePrice()));
                //tv_member_one_intro.setText(typeList.get(0).getName() + "(" + typeList.get(0).getNum() + "人)");
                tv_member_one_intro.setText(typeList.get(0).getName());
                tv_member_group_price.setText(FU.strDbFmt(typeList.get(1).getSalePrice()));
                //tv_member_group_intro.setText(typeList.get(1).getName() + "(" + typeList.get(1).getNum() + "人)");
                tv_member_group_intro.setText(typeList.get(1).getName());
            }
            findViewById(R.id.area_vip_type_choose).setVisibility(View.VISIBLE);
            vip_type0_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vip_type0_bg.setBackgroundResource(R.mipmap.vip_type_bg_focus);
                    vip_type1_bg.setBackgroundResource(R.mipmap.vip_type_bg_normol);
                    SingleBtnDialog singleBtnDialog = new SingleBtnDialog(mContext);
                    singleBtnDialog.showDialog("提示", "购买服务暂未开通，敬请期待", "确定", null);
                }
            });
            vip_type1_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vip_type0_bg.setBackgroundResource(R.mipmap.vip_type_bg_normol);
                    vip_type1_bg.setBackgroundResource(R.mipmap.vip_type_bg_focus);
                    SingleBtnDialog singleBtnDialog = new SingleBtnDialog(mContext);
                    singleBtnDialog.showDialog("提示", "购买服务暂未开通，敬请期待", "确定", null);
                }
            });
        }
    };


}
