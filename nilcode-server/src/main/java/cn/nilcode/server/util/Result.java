package cn.nilcode.server.util;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/15 10:09
 * @description
 */
@Data
@Accessors(chain = true)
public class Result {
    private Integer code;
    private String message;
    private String data;

    private Result() {
    }


    public static  String build(ResultCode code, String data) {
        return JSONUtil.toJsonStr(new Result().setCode(code.getCode()).setMessage(ResultCode.getMessage(code)).setData(data));
    }
    public static <T> String build(ResultCode code, List<T> data) {
        return JSONUtil.toJsonStr(new Result().setCode(code.getCode()).setMessage(ResultCode.getMessage(code)).setData(JSONUtil.toJsonStr(data)));
    }

    public static <T> String build(ResultCode code, T viewObject) {
        return JSONUtil.toJsonStr(new Result().setCode(code.getCode()).setMessage(ResultCode.getMessage(code)).setData(JSONUtil.toJsonStr(viewObject)));
    }
    public static String build(ResultCode code) {
        return JSONUtil.toJsonStr(new Result().setCode(code.getCode()).setMessage(ResultCode.getMessage(code)));
    }

    public static  String build(ResultCode code, Integer data) {
        return JSONUtil.toJsonStr(new Result().setCode(code.getCode()).setMessage(ResultCode.getMessage(code)).setData("" + data));
    }
}
