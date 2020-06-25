package cn.nilcode.server.vo;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/17 19:09
 * @description
 */
@Data
@Accessors(chain = true)
public class AdminBlogVo {
    private String blogId;
    private String blogTitle;
    private String publishedDate;
    private Integer clickCount;
    private String status;
    private String classifications;
}
