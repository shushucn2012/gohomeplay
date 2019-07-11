package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;

/**
 * Created by shubei on 2018/4/16.
 */

public class ImageHorizontalModule extends LinearLayout {

    private GridViewForScrollView gv_img;
    private ImageListAdapter adapter;
    private List<CmsItem> dataList;

    public ImageHorizontalModule(Context context, List<CmsItem> sList) {
        super(context);
        this.dataList = sList;
        LayoutInflater.from(context).inflate(R.layout.image_horizontal_module, this, true);
        gv_img = (GridViewForScrollView) findViewById(R.id.gv_img);
        gv_img.setFocusable(false);
        gv_img.setNumColumns(sList.size());
        adapter = new ImageListAdapter(context, sList);
        gv_img.setAdapter(adapter);
        gv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewInitTool.judgeGoWhere(dataList.get(position), context);
            }
        });
    }

    class ImageListAdapter extends BaseAdapter {

        private List<CmsItem> mList;
        private Context mContext;
        private LayoutInflater factory;

        public ImageListAdapter(Context _context, List<CmsItem> _list) {
            this.mList = _list;
            this.mContext = _context;
            factory = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CmsItem getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = factory.inflate(R.layout.imgmoudle_list_item, null);
                holder = new ViewHolder();
                holder.img_rights = (ImageView) convertView.findViewById(R.id.img_rights);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CmsItem item = mList.get(position);
            makeImageFitHeight(holder.img_rights);
            ImageManager.getInstance().displayImg(holder.img_rights, item.getLinkPic());
            return convertView;
        }

        class ViewHolder {
            ImageView img_rights;
        }

        /**
         * 是图片高度自适应
         */
        private void makeImageFitHeight(ImageView img) {
            int screenWidth = DevAttr.getScreenWidth(mContext);
            int itemWidth = screenWidth / dataList.size();
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            lp.width = itemWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            img.setLayoutParams(lp);
            img.setMaxWidth(itemWidth);
            img.setMaxHeight(itemWidth * 5); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
        }
    }
}
