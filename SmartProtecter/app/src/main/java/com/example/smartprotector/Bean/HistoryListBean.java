package com.example.smartprotector.Bean;

/**
 * 历史列表中的每一个条目。
 * Created by Huhu on 7/25/15.
 */
public class HistoryListBean {
    //报修单id
    private String service_id;
    // 图片
    private String image_path;
    //地点
    private String str_address;
    //故障源
    private String str_origin;
    //状态，1表示未解决，2表示正在维修中，3表示已解决
    private String str_state;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getStr_address() {
        return str_address;
    }

    public void setStr_address(String str_address) {
        this.str_address = str_address;
    }

    public String getStr_origin() {
        return str_origin;
    }

    public void setStr_origin(String str_origin) {
        this.str_origin = str_origin;
    }

    public String get_state() {
        return str_state;
    }

    public void set_state(String int_state) {
        this.str_state = int_state;
    }
}
