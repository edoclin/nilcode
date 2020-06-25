package cn.nilcode.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/15 10:03
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogVo {

    public BlogVo(String blogId, String coverImg, String title, String content, String classifications, String tags, Integer clickCount, String publishedDate, Boolean draft) {
        this.blogId = blogId;
        this.coverImg = coverImg;
        this.title = title;
        this.content = content;
        this.classifications = classifications;
        this.tags = tags;
        this.clickCount = clickCount;
        this.publishedDate = publishedDate;
        this.draft = draft;
    }

    private String blogId;
    private Date createDate;
    private String coverImg;
    private String title;
    private String content;
    private String classifications;
    private String tags;
    private Integer clickCount;
    private String publishedDate;
    private Boolean draft;
    private String userId;
}
