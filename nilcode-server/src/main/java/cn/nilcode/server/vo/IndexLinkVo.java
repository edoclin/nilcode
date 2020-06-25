package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/19 16:11
 * @description
 */
@Data
@Accessors(chain = true)
public class IndexLinkVo {

    private String linkName;
    private String linkUrl;
}