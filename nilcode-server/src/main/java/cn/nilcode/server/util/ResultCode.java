package cn.nilcode.server.util;

import lombok.Data;
import lombok.Getter;

import java.awt.*;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/15 10:06
 * @description
 */
@Getter
public enum ResultCode {

    /**
     * 响应成功
     */
    GLOBAL_SUCCESS(200, "响应成功"),
    QUERY_SUCCESS(2001, "查询成功"),
    DEL_SUCCESS(2002, "删除成功"),
    SAVE_SUCCESS(2003, "保存成功"),
    TITLE_AVAILABLE_SUCCESS(2004, "标题可用"),
    BLOG_SAVE_OR_UPDATE_SUCCESS(2005, "文章新增/更新成功"),
    CLASSIFICATION_REMOVE_SUCCESS(2006, "分类移除成功"),
    CLASSIFICATION_SAVE_SUCCESS(2007, "分类添加成功"),
    LOGIN_SUCCESS(2008, "用户登录成功"),
    LOGOUT_SUCCESS(2009, "用户退出成功"),
    USER_CHECK_SUCCESS(2010, "用户已登录"),

    /**
     * 400 系
     */
    TITLE_ALREADY_EXISTED(4001, "标题重复"),
    CLASSIFICATION_CHILDREN_EXISTED(4002, "存在子分类"),
    CLASSIFICATION_NOT_FOUND(4003, "分类不存在"),
    TAG_NOT_FOUND(4004, "标签不存在"),
    SEARCH_NOT_FOUND(4005, "无搜索结果"),
    NULL_LINKS_FOUND(4006, "无链接结果"),
    NULL_CLASSIFICATIONS_FOUND(4007, "无分类结果"),
    NULL_TAGS_FOUND(4008, "无标签结果"),
    NULL_ARCHIVE_FOUND(4009, "无归档结果"),
    NULL_TOP_NINE_FOUND(4010, "无榜单结果"),
    NULL_USER_LOGIN(4011, "无用户登录"),

    /**
     * 失败
     */
    SERVER_ERROR(500, "服务器繁忙"),
    INVALID_PARAM_ERROR(5001, "参数错误"),
    NULL_PARAM_ERROR(5002, "参数为空"),
    BLOG_NOT_EXISTED_ERROR(5003, "文章不存在"),
    USER_OR_PASSWORD_ERROR(5004, "用户名或密码错误"),
    ACCOUNT_OTHER_ERROR(5005, "账户其他错误"),
    ;

    Integer code;
    String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    static String getMessage(ResultCode code) {
        for (ResultCode rc : ResultCode.values()) {
            if (rc.getCode().equals(code.getCode())) {
                return rc.getMessage();
            }
        }
        return null;
    }
}
