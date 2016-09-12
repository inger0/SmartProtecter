package com.example.smartprotector.Bean;

/**
 * 报修单的详细信息
 * Created by Huhu on 7/25/15.
 */
public class ServiceContentBean {
    //地址
    private String address;
    //电话号码
    private String Phonenumber;
    //报修类型
    private int types;
    //故障源
    private String origin;
    //上传时间
    private String Report_time;
    //解决时间
    private String Reolve_time;
    //故障原因
    private String reason;
    //备注
    private String memo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getReport_time() {
        return Report_time;
    }

    public void setReport_time(String report_time) {
        Report_time = report_time;
    }

    public String getReolve_time() {
        return Reolve_time;
    }

    public void setReolve_time(String reolve_time) {
        Reolve_time = reolve_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
