package com.pwf.vo;

import lombok.Data;

@Data
public class ResultVO {
    private boolean success;
    private String msg;
    private Object body;

    public ResultVO(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public ResultVO(boolean success, String msg, Object body) {
        this.success = success;
        this.msg = msg;
        this.body = body;
    }

    public ResultVO() {
    }

}
