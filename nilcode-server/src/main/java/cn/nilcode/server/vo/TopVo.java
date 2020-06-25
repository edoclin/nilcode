package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/21 21:52
 * @description
 */
@Data
@Accessors(chain = true)
public class TopVo {

    private String blogId;
    private String blogName;
}