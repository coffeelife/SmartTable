package com.example.myapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.MenuResBean;

import java.util.ArrayList;
import java.util.List;

public class MenuGridAdapter extends BaseAdapter {
    private List<MenuResBean.CommonAppListBean> mTables = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public MenuGridAdapter(List<MenuResBean.CommonAppListBean> mTables, Context context) {
        this.mTables.addAll(mTables);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTables.size();
    }

    @Override
    public Object getItem(int i) {
        return mTables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_grid_list, group, false);
            viewHolder.mImageView = view.findViewById(R.id.iv_channel_logo);
            viewHolder.mTextView = view.findViewById(R.id.tv_channel_name);
            viewHolder.redPoint = view.findViewById(R.id.view_redpoint);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (mTables.get(position).getAppTipsCount() >0){
            viewHolder.redPoint.setVisibility(View.VISIBLE);
        }else{
            viewHolder.redPoint.setVisibility(View.GONE);
        }

        viewHolder.mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_menu_default));

        viewHolder.mTextView.setText(mTables.get(position).getName());
        return view;
    }

    public static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        View redPoint;
    }
}