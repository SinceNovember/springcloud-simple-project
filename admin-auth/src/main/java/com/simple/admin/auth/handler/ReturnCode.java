package com.simple.admin.auth.handler;

public enum ReturnCode {
    RC500(1000,"����"),
    CLIENT_AUTHENTICATION_FAILED(1001,"�ͻ�����֤ʧ��"),
    USERNAME_OR_PASSWORD_ERROR(1002,"�û������������"),
    UNSUPPORTED_GRANT_TYPE(1003, "��֧�ֵ���֤ģʽ");

    ReturnCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
