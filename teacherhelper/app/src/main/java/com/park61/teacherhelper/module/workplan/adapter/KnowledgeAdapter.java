package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;

import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ActivityViewHolder>{

    private List<KnowledgeBean> datas;
    private Context mContext;

    public KnowledgeAdapter(Context context, List<KnowledgeBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_knowledge_adapter, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        KnowledgeBean b = datas.get(position);

        if(1==b.getContentType()){
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_watch);
        }else if(2==b.getContentType()) {
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_listener);
        }else{
            holder.iv_isplay.setVisibility(View.GONE);
        }

        ImageManager.getInstance().displayImg(holder.iv_content, b.getCoverImg());
        holder.title.setText(b.getTitle());
        holder.time.setText(b.getShowDate());
        if(position == datas.size()-1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_isplay;
        ImageView iv_content;
        TextView title;
        TextView time;
        View line;
        public ActivityViewHolder(View itemView) {
            super(itemView);
            iv_isplay = (ImageView) itemView.findViewById(R.id.iv_isplay);
            iv_content = (ImageView) itemView.findViewById(R.id.iv_content);
            title = (TextView) itemView.findViewById(R.id.content_title);
            time = (TextView) itemView.findViewById(R.id.content_time);
            line = itemView.findViewById(R.id.bline);
        }
    }

}
