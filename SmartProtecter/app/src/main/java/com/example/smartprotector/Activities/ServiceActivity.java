package com.example.smartprotector.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.smartprotector.Bean.BitmapBean;
import com.example.smartprotector.Config.MyApplication;
import com.example.smartprotector.Net.NetCore;
import com.example.smartprotector.Net.NewService;
import com.example.smartprotector.R;
import com.example.smartprotector.Utils.CutImageUtil;
import com.example.smartprotector.Utils.DoubleClickUtil;
import com.example.smartprotector.Widgets.ServiceTypePopWindow;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceActivity extends Activity {
    //返回和提交按钮
    private ImageButton btn_service_back;
    //点击拍照按钮
    private ImageButton btn_takePhoto;
    //报修类型按钮
    private Button btn_ServiceType, btn_service_commit;
    //输入框
    private EditText edt_address, edt_phone, edt_origin, edt_time, edt_reason, edt_memo;
    //图像文件名
    private String filename = "";
    //当前时间
    private String time;
    //手机标识符
    private String IMEI;
    //七牛基地址
    private String str_base = "http://7xki4k.com1.z0.glb.clouddn.com/";
    private String str_ImagePath;
    //地点输入框内容
    private String str_address;
    //手机号码
    private String str_phone;
    //报修类型，用int表示
    private int int_type;
    //故障源内容
    private String str_origin;
    //预定维修时间内容
    private String str_time;
    //故障表现
    private String str_reason;
    //备注内容
    private String str_memo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getToken();
                    break;
                case 1:
                    String token = msg.obj.toString();
                    UploadPic(filename, IMEI + time, token);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        init();
    }

    /**
     * 初始化方法
     */
    void init() {
        btn_service_back = (ImageButton) findViewById(R.id.btn_service_back);
        btn_service_commit = (Button) findViewById(R.id.btn_service_commit);
        btn_takePhoto = (ImageButton) findViewById(R.id.btn_takePhoto);
        btn_ServiceType = (Button) findViewById(R.id.btn_ServiceType);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_origin = (EditText) findViewById(R.id.edt_ServiceOrigin);
        edt_time = (EditText) findViewById(R.id.edt_ServiceTime);
        edt_reason = (EditText) findViewById(R.id.edt_reason);
        edt_memo = (EditText) findViewById(R.id.edt_memo);
        setListener();
    }

    /**
     * 调用系统相机的方法，拍照并保存在ImageButton上
     */
    void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用系统自带相机
        this.startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            //检测SD卡是否存在
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "检测不到SD卡", Toast.LENGTH_SHORT).show();
                return;
            }

            // 获取相机返回的数据，并转换为Bitmap图片格式
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //切割圆角矩形并存入SD卡
            BitmapBean bean = CutImageUtil.setImageRoundCorner(bitmap);
            filename = bean.getFilename();
            BitmapDrawable drawable = new BitmapDrawable(bean.getBitmap());
            btn_takePhoto.setBackground(drawable);

        }
    }

    /**
     * 添加监听的方法
     */
    void setListener() {
        btn_service_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                if (checkIsLegal() == true) {
                    push();
                }


            }
        });
        btn_service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceActivity.this.finish();
            }
        });
        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btn_ServiceType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopUpWindow();
            }
        });
    }

    /**
     * 检查用户填写资料是否合法
     *
     * @return boolean
     */
    private boolean checkIsLegal() {
        getValue();
        if (filename.equals("")) {
            Toast.makeText(this, "请拍照取证", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_phone.equals("")) {
            Toast.makeText(this, "请填写联系方式", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_address.equals("")) {
            Toast.makeText(this, "请填写地点", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_origin.equals("")) {
            Toast.makeText(this, "请填写故障源", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_time.equals("")) {
            Toast.makeText(this, "请填写预定维修时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_reason.equals("")) {
            Toast.makeText(this, "请填写故障表现", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (str_memo.equals("")) {
            Toast.makeText(this, "请填写备注", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void push() {
        //先上传七牛图片,并获取图片地址
        time = getTime();
        IMEI = getIMEI();
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
        str_ImagePath = IMEI + time;
        str_ImagePath = str_ImagePath.replace(" ", "%20");
        str_ImagePath = str_ImagePath.replace(":", "%3A");
        str_ImagePath = str_base + str_ImagePath;
        String str_registerphone = MyApplication.getInstance().getCurrentUser().getUser_phone();
        //上传资料到服务器
        new NewService(ServiceActivity.this, str_ImagePath, str_registerphone, str_address, str_phone, int_type, str_origin, str_time, str_reason, str_memo, new NewService.FailedCallback() {
            @Override
            public void onFailure() {

            }
        }, new NewService.SuccessfulCallback() {
            @Override
            public void onSuccess(String result) {

            }
        });

        ServiceActivity.this.finish();
    }


    /**
     * 取值操作
     */
    private void getValue() {
        str_address = edt_address.getText().toString();
        str_phone = edt_phone.getText().toString();
        str_origin = edt_origin.getText().toString();
        str_time = edt_time.getText().toString();
        str_reason = edt_reason.getText().toString();
        str_memo = edt_memo.getText().toString();
        String str_type = btn_ServiceType.getText().toString();
        if (str_type.equals("宿舍楼")) {
            int_type = 1;
        }
        if (str_type.equals("教学楼")) {
            int_type = 2;
        }
        if (str_type.equals("校园设施")) {
            int_type = 3;
        }


    }

    /**
     * 获取七牛上传图片的凭证
     */
    private void getToken() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                String url = "http://115.28.201.92/smartprotecter/sp/image_upload.php";
                try {
                    String token = NetCore.getResultFromNet(url);
                    msg.what = 1;
                    msg.obj = token;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    /**
     * 七牛上传图片的方法
     *
     * @param PicPath
     * @param key
     * @param token
     */
    public void UploadPic(String PicPath, String key, String token) {

        UploadManager uploadManager = new UploadManager();
        uploadManager.put(PicPath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.i("qiniu", info + "");

            }
        }, null);
    }


    /**
     * 设置弹出菜单
     */
    void showPopUpWindow() {
        ServiceTypePopWindow pop = new ServiceTypePopWindow(this, btn_ServiceType);
        pop.showPopupWindow(btn_ServiceType);

    }

    /**
     * 获取头像照片上传时间
     *
     * @return
     */
    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        return time;
    }

    /**
     * 获取手机标识符
     *
     * @return
     */
    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        return IMEI;
    }
}
