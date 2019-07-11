package com.park61.teacherhelper.widget.webview;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.ImageManager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private List<String> picUrls;

    public ImageBrowserAdapter(Activity context, ArrayList<String> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
    }

    @Override
    public int getCount() {
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_image_browser, null);
        PhotoView pvShowImage = (PhotoView) view.findViewById(R.id.pv_show_image);
        String picUrl = picUrls.get(position);
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(pvShowImage);
        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoViewAttacher.setMinimumScale(1F);
        ImageManager.getInstance().displayScaleImage(context, pvShowImage, picUrl, photoViewAttacher);
        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                Log.out("-----------------onPhotoTap_clicked--------------------------");
                ((Activity) context).finish();
            }
        });
        photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Log.out("-----------------onViewTap_clicked--------------------------");
                ((Activity) context).finish();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}