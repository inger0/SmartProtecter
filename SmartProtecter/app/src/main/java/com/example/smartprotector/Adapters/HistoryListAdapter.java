package com.example.smartprotector.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartprotector.Bean.HistoryListBean;
import com.example.smartprotector.Net.MainImageConnection;
import com.example.smartprotector.R;

import java.util.ArrayList;

/**
 * Created by Huhu on 8/20/15.
 */
public class HistoryListAdapter extends CommonAdapter {

    public HistoryListAdapter(Context context, ArrayList<HistoryListBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.service_list_item, position);
        HistoryListBean bean = (HistoryListBean) mDatas.get(position);
        ((TextView) holder.getView(R.id.tv_address)).setText(bean.getStr_address());
        ((TextView) holder.getView(R.id.tv_origin)).setText(bean.getStr_origin());
        ((TextView) holder.getView(R.id.tv_status)).setText(bean.get_state());
        new MainImageConnection(((ImageView) holder.getView(R.id.iv_image))).execute(bean.getImage_path().replace(" ", "%20"));
        return holder.getmConvertView();
    }
}
