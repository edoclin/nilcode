package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/19 13:27
 * @description
 */
@Data
@Accessors(chain = true)
public class LinkVo {

    private String linkName;
    private String linkType;
    private String linkUrl;
}