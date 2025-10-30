package com.angel.minio.util.web.domain;


public class ResultEntity<T> {
    /**
     * 状态码：0表示成功，1表示失败
     */
    private Integer code;
    /**
     * 成功或失败信息
     */
    private String msg;

    private T data;

    public ResultEntity() {
        this.code=0;
        this.msg = "请求成功";
    }

    public ResultEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultEntity(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResultEntity success() {
        return new ResultEntity(0, "");
    }

    public static ResultEntity success(String msg) {
        return new ResultEntity(0, msg);
    }

    public static ResultEntity fail(String msg) {
        return new ResultEntity(1, msg);
    }

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

    @Override
    public String toString() {
        return "ResultEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
