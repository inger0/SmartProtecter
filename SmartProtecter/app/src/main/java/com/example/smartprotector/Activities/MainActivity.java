package com.example.smartprotector.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.smartprotector.Adapters.MyPagerAdapter;
import com.example.smartprotector.Fragments.HistoryFragment;
import com.example.smartprotector.Fragments.MoreFragment;
import com.example.smartprotector.Fragments.PersonalFragment;
import com.example.smartprotector.R;

import java.util.ArrayList;

/**
 * @author huhu
 * @date 2015-7-11
 * SmartProtector主界面
 */


public class MainActivity extends FragmentActivity {
    //底部栏的布局
    public LinearLayout layout_bottombar;
    //导航栏的布局
    public RelativeLayout btn_personal, btn_history, btn_more;
    //导航栏的按钮和文字
    public Button bperson, bhistory, bmore;
    public TextView tperson, thistory, tmore;
    //viewPager实例
    public ViewPager viewpager;
    //fragment
    public Fragment historyfragment, morefragment, personalfragment;
    //fragmentList
    public ArrayList<Fragment> fragmentlist;
    //Adapter
    public MyPagerAdapter viewpageradapter;
    //控制界面改变的handler
    public Handler hideHandler = new Handler();
    //隐藏的时间间隔
    public int waitDuration = 5000;
    //隐藏导航栏的runnable
    public HideRunnable hideRunnable = new HideRunnable();
    //判断是否开始计时
    public boolean hasRunnable = false;
    //判断是否已经隐藏导航栏
    public boolean hasHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setViewpager();
        setListener();
    }


    /**
     * 初始化方法
     */
    void init() {
        //间隔一定时间执行Runnable
        hideHandler.postDelayed(hideRunnable, waitDuration);
        hasRunnable = true;

        layout_bottombar = (LinearLayout) findViewById(R.id.bottombar);

        btn_personal = (RelativeLayout) findViewById(R.id.titlebar_btn_personal);
        btn_history = (RelativeLayout) findViewById(R.id.titlebar_btn_history);
        btn_more = (RelativeLayout) findViewById(R.id.titlebar_btn_more);

        bperson = (Button) findViewById(R.id.bperson);
        bhistory = (Button) findViewById(R.id.bhistory);
        bmore = (Button) findViewById(R.id.bmore);
        /* 这个地方要注意 如果不禁用按钮，事件会被按钮给吃掉。*/
        bperson.setClickable(false);
        bhistory.setClickable(false);
        bmore.setClickable(false);

        tperson = (TextView) findViewById(R.id.tperson);
        thistory = (TextView) findViewById(R.id.thistory);
        tmore = (TextView) findViewById(R.id.tmore);

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        historyfragment = new HistoryFragment();
        personalfragment = new PersonalFragment();
        morefragment = new MoreFragment();

        fragmentlist = new ArrayList<>();
        fragmentlist.add(personalfragment);
        fragmentlist.add(historyfragment);
        fragmentlist.add(morefragment);
        viewpageradapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentlist);

        hideHandler = new Handler();


    }

    /**
     * 添加监听的方法,
     */
    void setListener() {
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(0);
                bperson.setBackgroundResource(R.mipmap.person_on);
                tperson.setTextColor(0xff00b7ee);
                bhistory.setBackgroundResource(R.mipmap.history_off);
                thistory.setTextColor(0xff999999);
                bmore.setBackgroundResource(R.mipmap.more_off);
                tmore.setTextColor(0xff999999);

            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
                bperson.setBackgroundResource(R.mipmap.person_off);
                tperson.setTextColor(0xff999999);
                bhistory.setBackgroundResource(R.mipmap.history_on);
                thistory.setTextColor(0xff00b7ee);
                bmore.setBackgroundResource(R.mipmap.more_off);
                tmore.setTextColor(0xff999999);

            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);
                bperson.setBackgroundResource(R.mipmap.person_off);
                tperson.setTextColor(0xff999999);
                bhistory.setBackgroundResource(R.mipmap.history_off);
                thistory.setTextColor(0xff999999);
                bmore.setBackgroundResource(R.mipmap.more_on);
                tmore.setTextColor(0xff00b7ee);
            }
        });

    }

    /**
     * 设置ViewPager及按钮状态变化监听
     */
    void setViewpager() {
        viewpager.setAdapter(viewpageradapter);
        viewpager.setCurrentItem(1);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        btn_personal.performClick();
                        break;
                    case 1:
                        btn_history.performClick();
                        break;
                    case 2:
                        btn_more.performClick();
                        break;
                }

            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (hasHide) {
                //调用上浮动画
                final Animation animUp = new TranslateAnimation(0, 0, 150, 0);
                animUp.setDuration(400);
                animUp.setFillAfter(true);
                layout_bottombar.setAnimation(animUp);
                animUp.startNow();
                layout_bottombar.setVisibility(View.VISIBLE);
                hasHide = false;
                hideHandler.postDelayed(hideRunnable, waitDuration);
            } else {
                //重启计时器
                if (hasRunnable) {
                    hideHandler.removeCallbacks(hideRunnable);
                    hideHandler.postDelayed(hideRunnable, waitDuration);
                }
            }
        }
        return false;
    }

    /**
     * 防止点击事件被Viewpager吃掉，故重新分配给onTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //给onTouchEvent发送事件
        this.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 隐藏标题栏的类
     */
    class HideRunnable implements Runnable {

        @Override
        public void run() {
            hideHandler.removeCallbacks(hideRunnable);
            hasHide = true;
            final Animation anim = new TranslateAnimation(0, 0, 0, 150);
            anim.setDuration(400);
            anim.setFillAfter(true);
            anim.setInterpolator(new LinearInterpolator());
            layout_bottombar.setAnimation(anim);
            layout_bottombar.setVisibility(View.GONE);
            anim.startNow();
        }
    }
}
