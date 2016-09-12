package com.example.smartprotector.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smartprotector.Activities.MyServiceActivity;
import com.example.smartprotector.Activities.ServiceActivity;
import com.example.smartprotector.Adapters.HistoryListAdapter;
import com.example.smartprotector.Bean.HistoryListBean;
import com.example.smartprotector.Config.MyApplication;
import com.example.smartprotector.Net.GetAllService;
import com.example.smartprotector.Net.GetServiceReport;
import com.example.smartprotector.Net.ImageConnection;
import com.example.smartprotector.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Huhu on 7/13/15.
 */
public class HistoryFragment extends Fragment {

    //添加新报修单的按钮
    public ImageButton btn_addNewService;
    //头像图片
    public ImageView image_icon;
    //昵称
    public TextView tv_nick;
    //引入布局
    public View view;
    //全局变量
    public Activity activity = getActivity();
    public MyApplication application;
    //历史记录页面列表
    public ListView list;

    //下拉刷新控件
    public SwipeRefreshLayout swipeLayout;
    //适配器
    HistoryListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        application = MyApplication.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, null);
        init();
        return view;
    }

    /**
     * 初始化方法
     */
    void init() {
        btn_addNewService = (ImageButton) view.findViewById(R.id.btn_addnewservice);
        image_icon = (ImageView) view.findViewById(R.id.image_icon);
        tv_nick = (TextView) view.findViewById(R.id.text_username);
        tv_nick.setText(application.getCurrentUser().getUser_nickname());
        list = (ListView) view.findViewById(R.id.lv_history);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorScheme(android.R.color.holo_blue_dark);
        //设置监听器
        setListener();
        //设置头像  要将网址中的空格去掉..这坑爹。
        String URL = application.getCurrentUser().getImage_path().replace(" ", "%20");
        new ImageConnection(image_icon).execute(URL);

        //获取所有报修单数据
        new GetAllService(MyApplication.getInstance().getCurrentUser().getUser_phone(), new GetAllService.SuccessCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {

                //解析Json数据
                JSONArray array = new JSONArray(result);
                ArrayList<HistoryListBean> servicelist = new ArrayList<HistoryListBean>();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jo = array.getJSONObject(i);
                    HistoryListBean bean = new HistoryListBean();
                    bean.setStr_address(jo.get("address").toString());
                    bean.setStr_origin(jo.get("origin").toString());
                    bean.set_state(judgeStatus(Integer.parseInt(jo.get("states").toString())));
                    bean.setImage_path(jo.get("image").toString());
                    bean.setService_id(jo.get("s_id").toString());
                    servicelist.add(bean);

                }
                adapter = new HistoryListAdapter(getActivity(), servicelist);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });


    }

    /**
     * 判断当前报修单状态的方法
     */
    private String judgeStatus(int status) {
        String str_status = "未解决";
        if (status == 1) {
            str_status = "未解决";
        }
        if (status == 2) {
            str_status = "正在维修中";
        }
        if (status == 3) {
            str_status = "已解决";
        }

        return str_status;
    }

    /**
     * 设置监听器
     */
    void setListener() {
        btn_addNewService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(), ServiceActivity.class);
                startActivity(i);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HistoryListBean bean = (HistoryListBean) list.getItemAtPosition(position);
                new GetServiceReport(bean.getService_id(), new GetServiceReport.SuccessCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {

                        JSONObject jo = new JSONObject(result);
                        Intent i = new Intent();
                        i.setClass(getActivity(), MyServiceActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("service_id", bean.getService_id());
                        bundle.putString("address", jo.get("address").toString());
                        bundle.putString("reportphone", jo.get("phonenumber").toString());
                        bundle.putString("type", jo.get("types").toString());
                        bundle.putString("origin", jo.get("origin").toString());
                        bundle.putString("reason", jo.get("reason").toString());
                        bundle.putString("time", jo.get("resolve_time").toString());
                        bundle.putString("memo", jo.get("remark").toString());
                        bundle.putString("status", jo.get("states").toString());
                        bundle.putString("image_path", jo.get("image").toString().replace(" ", "%20"));
                        if (jo.get("states").toString().equals("1")) {
                            bundle.putString("mphone", "暂时无人处理");
                        } else {
                            bundle.putString("mphone", jo.get("phonenumber_m").toString());
                        }
                        i.putExtras(bundle);
                        getActivity().startActivity(i);

                    }
                });

            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAllService(MyApplication.getInstance().getCurrentUser().getUser_phone(), new GetAllService.SuccessCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        //解析Json数据
                        ArrayList<HistoryListBean> servicelist = new ArrayList<HistoryListBean>();
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            HistoryListBean bean = new HistoryListBean();
                            bean.setStr_address(jo.get("address").toString());
                            bean.setStr_origin(jo.get("origin").toString());
                            bean.set_state(judgeStatus(Integer.parseInt(jo.get("states").toString())));
                            bean.setImage_path(jo.get("image").toString());
                            bean.setService_id(jo.get("s_id").toString());
                            servicelist.add(bean);

                        }
                        adapter = new HistoryListAdapter(getActivity(), servicelist);
                        list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
                swipeLayout.setRefreshing(false);
            }
        });
    }

}
