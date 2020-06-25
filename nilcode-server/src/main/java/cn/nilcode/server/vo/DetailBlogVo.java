package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/17 22:12
 * @description
 */
@Data
@Accessors(chain = true)
public class DetailBlogVo {
    private String blogTitle;
    private String[] classifications;
    private String[] tags;
    private String content;
    private String publishedDate;
    private Integer clickCount;

    /**
     * todo : 评论
     */

}
