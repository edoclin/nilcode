package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/19 14:47
 * @description
 */
@Data
@Accessors(chain = true)
public class UpdateLinkVo {
    private String linkName;
    private String linkType;
    private String oldUrl;
    private String linkUrl;
}