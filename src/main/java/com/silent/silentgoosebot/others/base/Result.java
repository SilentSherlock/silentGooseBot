package com.silent.silentgoosebot.others.base;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private Status status;
    private String description;
    private Map<String, Object> resultMap;

    public Result(Status status, String msg, Map<String, Object> map) {
        this.status = status;
        this.description = msg;
        this.resultMap = map;
    }

    public Result() {
        this(Status.DEFAULT, "", new HashMap<>());
    }

    public Result(Status status) {
        this(status, "", new HashMap<>());
    }

    public Result(Status status, String msg) {
        this(status, msg, new HashMap<>());
    }

    public static Result createBySuccess(String msg) {
        Result result = new Result(Status.SUCCESS);
        result.setDescription(msg);
        return result;
    }

    public static Result createBySuccess() {
        return Result.createBySuccess("");
    }

    public static Result createByFalse(String msg) {
        Result result = new Result(Status.FALSE);
        result.setDescription(msg);
        return result;
    }

    public static Result createByFalse() {
        return Result.createByFalse("");
    }

    public static Result createByNeedLogin(String msg) {
        Result result = new Result(Status.NEED_LOGIN);
        result.setDescription(msg);
        return result;
    }

    public static Result createByNeedLogin() {
        return Result.createByNeedLogin("");
    }

    public static Result createByDefault(String msg) {
        Result result = new Result(Status.DEFAULT);
        result.setDescription(msg);
        return result;
    }

    public static Result createByDefault() {
        return Result.createByDefault("");
    }

    public static Result createByWrongRequest(String msg) {
        Result result = new Result(Status.WRONG_REQUEST);
        result.setDescription(msg);
        return result;
    }

    public static Result createByWrongRequest() {
        return Result.createByWrongRequest("");
    }

    public static Result createByNotFound(String msg) {
        Result result = new Result(Status.NOT_FOUND);
        result.setDescription(msg);
        return result;
    }

    public static Result createByNotFound() {
        return Result.createByNotFound("");
    }

}
