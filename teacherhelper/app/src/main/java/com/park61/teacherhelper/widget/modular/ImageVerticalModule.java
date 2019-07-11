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

import java.util.List;

/**
 * Created by shubei on 2018/4/16.
 */

public class ImageVerticalModule extends LinearLayout {

    private ListView lv_img;
    private ImageListAdapter adapter;

    public ImageVerticalModule(Context context, List<CmsItem> sList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.image_vertical_module, this, true);
        lv_img = (ListView) findViewById(R.id.lv_img);
        lv_img.setFocusable(false);
        adapter = new ImageListAdapter(context, sList);
        lv_img.setAdapter(adapter);
        lv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewInitTool.judgeGoWhere(sList.get(position), context);
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
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            lp.width = screenWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            img.setLayoutParams(lp);
            img.setMaxWidth(screenWidth);
            img.setMaxHeight(screenWidth * 5); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
        }
    }
}
