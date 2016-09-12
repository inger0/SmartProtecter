package com.example.smartprotector.Widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.smartprotector.R;

/**
 * Created by Huhu on 7/21/15.
 */
public class ServiceTypePopWindow extends PopupWindow {

    //传入的View
    public View contentView;
    //获取3种类型
    public TextView type1, type2, type3;
    //获取按钮
    public Button parent;

    //构造函数
    public ServiceTypePopWindow(final Activity activity, Button parent) {
        this.parent = parent;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.type_list, null);
        init();
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 50);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置半透明背景
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }

    void init() {
        type1 = (TextView) contentView.findViewById(R.id.type_one);
        type2 = (TextView) contentView.findViewById(R.id.type_two);
        type3 = (TextView) contentView.findViewById(R.id.type_three);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.setText(type1.getText());
                ServiceTypePopWindow.this.dismiss();
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.setText(type2.getText());
                ServiceTypePopWindow.this.dismiss();
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.setText(type3.getText());
                ServiceTypePopWindow.this.dismiss();
            }
        });
    }


    //设置显示方式
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }

}
