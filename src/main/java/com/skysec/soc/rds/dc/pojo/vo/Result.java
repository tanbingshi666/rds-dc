package com.skysec.soc.rds.dc.pojo.vo;

import lombok.Data;

@Data
public class Result<T> extends BaseResult {
    protected T data;

    private Result() {
        this.code = ResultStatus.SUCCESS.getCode();
        this.message = ResultStatus.SUCCESS.getMessage();
    }

    public static <T> Result<T> buildFail() {
        Result<T> result = new Result<>();
        result.setCode(ResultStatus.FAIL.getCode());
        result.setMessage(ResultStatus.FAIL.getMessage());
        return result;
    }

    public static <T> Result<T> build(boolean succ, T data) {
        Result<T> result = new Result<>();
        if (succ) {
            result.setCode(ResultStatus.SUCCESS.getCode());
            result.setMessage(ResultStatus.SUCCESS.getMessage());
            result.setData(data);
        } else {
            result.setCode(ResultStatus.FAIL.getCode());
            result.setMessage(ResultStatus.FAIL.getMessage());
        }
        return result;
    }

    public static <T> Result<T> buildSuc() {
        Result<T> result = new Result<>();
        result.setCode(ResultStatus.SUCCESS.getCode());
        result.setMessage(ResultStatus.SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> buildSuc(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultStatus.SUCCESS.getCode());
        result.setMessage(ResultStatus.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> buildFailure(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultStatus.FAIL.getCode());
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}