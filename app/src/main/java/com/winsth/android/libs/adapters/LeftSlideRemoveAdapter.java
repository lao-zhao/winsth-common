package com.winsth.android.libs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.winsth.android.libs.R;

import com.winsth.android.libs.interfs.OnItemRemoveListener;

/**
 * 作者 Aaron Zhao
 * 时间 2020/9/18 15:08
 * 名称 LeftSlideRemoveAdapter.java
 * 描述
 */
public abstract class LeftSlideRemoveAdapter extends BaseAdapter {
    protected Context mContext;
    public OnItemRemoveListener mListener;

    public LeftSlideRemoveAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public final View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.listview_left_slide_remove, parent, false);

            holder = new MyViewHolder();
            holder.viewContent = (RelativeLayout) convertView.findViewById(R.id.view_content);
            holder.tvRmove = (TextView) convertView.findViewById(R.id.tv_remove);
            convertView.setTag(holder);

            // viewChild是实际的界面
            holder.viewChild = getSubView(position, null, parent);
            holder.viewContent.addView(holder.viewChild);
        } else {
            holder = (MyViewHolder) convertView.getTag();
            getSubView(position, holder.viewChild, parent);
        }
        holder.tvRmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemRemove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    public abstract View getSubView(int position, View convertView, ViewGroup parent);

    private class MyViewHolder {
        RelativeLayout viewContent;
        View viewChild;
        View tvRmove;
    }
}

