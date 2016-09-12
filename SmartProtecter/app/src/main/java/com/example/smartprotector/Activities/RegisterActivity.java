package com.example.smartprotector.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartprotector.Bean.BitmapBean;
import com.example.smartprotector.Net.NetCore;
import com.example.smartprotector.Net.Register;
import com.example.smartprotector.R;
import com.example.smartprotector.Utils.CutImageUtil;
import com.example.smartprotector.Utils.DoubleClickUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity implements OnClickListener {
    //手机号输入框
    private EditText edt_phone;
    //验证码输入框
    private EditText edt_vertify;
    //获取验证码的按钮
    private Button btn_getVertify;
    //密码输入框
    private EditText edt_password;
    //昵称输入框
    private EditText edt_nickname;
    //注册按钮
    private Button btn_commit;
    //上传头像按钮
    private Button btn_setIcon;
    //返回按钮
    private ImageButton btn_back;
    //上传的头像的ImageView
    private ImageView iv_icon;
    //打开本地相册
    private final String IMAGE_TYPE = "image/*";
    private int IMAGE_CODE = 1;
    //获取验证码倒计时
    int i = 30;
    //头像文件名
    private String filename = "";
    //当前时间
    private String time;
    //手机标识符
    private String IMEI;
    //七牛上存储图片的默认url
    private String str_iconPath;
    private String str_base = "http://7xki4k.com1.z0.glb.clouddn.com/";
    //push时的参数
    private String str_phone, str_password, str_nickname;

    private Handler handler_icon = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pushInit();
                    break;
                case 1:
                    String token = msg.obj.toString();
                    UploadPic(filename, IMEI + time, token);

                    break;
                case 2:
                    break;

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SMSSDK.initSDK(this, "977e03f905c0", "e67972344a8451954ba12d0b5969937f");
        init();
    }

    /**
     * 初始化方法
     */

    private void init() {
        //初始化控件
        edt_phone = (EditText) findViewById(R.id.edt_phoneNumber);
        edt_vertify = (EditText) findViewById(R.id.edt_yanzhengma);
        btn_getVertify = (Button) findViewById(R.id.btn_getYanzhengma);
        btn_commit = (Button) findViewById(R.id.btn_toRegister);
        btn_setIcon = (Button) findViewById(R.id.btn_setIcon);
        iv_icon = (ImageView) findViewById(R.id.iv_getIcon);
        edt_password = (EditText) findViewById(R.id.edt_setPassword);
        edt_nickname = (EditText) findViewById(R.id.edt_nickname);
        btn_back = (ImageButton) findViewById(R.id.btn_backToLogin);
        //为控件设置监听器
        btn_commit.setOnClickListener(this);
        btn_getVertify.setOnClickListener(this);
        btn_setIcon.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);

            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = edt_phone.getText().toString();
        switch (v.getId()) {
            //获取验证码的按钮
            case R.id.btn_getYanzhengma:

                if (!judgePhoneNums(phoneNums)) {
                    return;
                }
                SMSSDK.getVerificationCode("86", phoneNums);

                btn_getVertify.setClickable(false);
                btn_getVertify.setText("倒计时(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            //上传头像的按钮
            case R.id.btn_setIcon:
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
                break;
            //提交按钮
            case R.id.btn_toRegister:
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                //若用户所填资料合法，则向服务器提交资料
                if (testIslegal() == true) {
                    time = getTime();
                    IMEI = getIMEI();
                    //上传注册照片。
                    pushInit();

                    //检测验证码
                    SMSSDK.submitVerificationCode("86", phoneNums, edt_vertify.getText().toString());
                }
                break;
            case R.id.btn_backToLogin:
                Intent i = new Intent();
                i.setClass(this, LoginActivity.class);
                this.startActivity(i);
                finish();
        }
    }

    /**
     * 向服务器提交资料。
     */
    private void push() {

        str_iconPath = IMEI + time;
        str_iconPath = str_iconPath.replace(" ", "%20");
        str_iconPath = str_iconPath.replace(":", "%3A");
        str_iconPath = str_base + str_iconPath;
        str_password = md5(str_password);
        new Register(RegisterActivity.this, str_phone, str_password, str_nickname, str_iconPath, new Register.SuccessCallBack() {
            @Override
            public void onSuccess(String result) {

            }
        }, new Register.FailCallBack() {
            @Override
            public void onFail() {
                Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 测试用户所填资料是否正确
     */
    private boolean testIslegal() {
        str_password = edt_password.getText().toString();
        str_nickname = edt_nickname.getText().toString();
        String str_yanzhegnma = edt_vertify.getText().toString();
        str_phone = edt_phone.getText().toString();
        if (str_password.equals("") || str_nickname.equals("")) {
            Toast.makeText(RegisterActivity.this, "请正确输入资料", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_password.length() < 5) {
            Toast.makeText(RegisterActivity.this, "输入密码不得小于6位数", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_phone.equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        } else if (str_yanzhegnma.equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        } else if (filename.equals("")) {
            Toast.makeText(RegisterActivity.this, "请上传头像", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取图片上传凭证的方法
     */
    public void pushInit() {
        //处理头像URL

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
                    handler_icon.sendMessage(msg);

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

    /**
     * 获取相册照片的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                try {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    ContentResolver cr = this.getContentResolver();
                    Bitmap album_bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    if (album_bitmap == null) {
                    }
                    album_bitmap = ThumbnailUtils.extractThumbnail(album_bitmap, 180, 180);
                    BitmapBean bean = CutImageUtil.setImageRoundCorner(album_bitmap);
                    filename = bean.getFilename();
                    iv_icon.setImageBitmap(bean.getBitmap());

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {

                    return;
                }
                break;
            case RESULT_CANCELED: // 用户取消照相
                return;
        }


    }


    /**
     * 获取验证码的handler
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btn_getVertify.setText("倒计时(" + i + ")");
            } else if (msg.what == -8) {
                btn_getVertify.setText("重新发送");
                btn_getVertify.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        //向服务器发送资料
                        push();
                        RegisterActivity.this.startActivity(intent);
                        RegisterActivity.this.finish();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    //((Throwable) data).printStackTrace();
                }
            }
        }
    };


    /**
     * 判断手机号是否合法
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "请输入有效手机号", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断手机号的长度
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 判断是不是手机号
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
        if (TextUtils.isEmpty(mobileNums)) return false;
        else return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
