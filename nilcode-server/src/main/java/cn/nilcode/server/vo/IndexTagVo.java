package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/21 9:24
 * @description
 */
@Data
@Accessors(chain = true)
public class IndexTagVo {
    private String tagId;
    private String tagName;
}