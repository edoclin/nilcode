package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/16 21:53
 * @description
 */
@Data
@Accessors(chain = true)
public class IndexBlogVo {
    private String blogId;
    private String blogTitle;
    private String coverImg;
    private String[] tags;
    private String[] classifications;
    private String publishedDate;
}
