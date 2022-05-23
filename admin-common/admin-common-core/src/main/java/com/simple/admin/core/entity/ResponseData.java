package com.simple.admin.core.entity;

/**
 * ·µ»ΨΚµΜε
 */
public class ResponseData {

    private int code;

    private String msg;

    private Object data;

    public ResponseData(int code) {
        this.code = code;
    }

    public ResponseData() {
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}