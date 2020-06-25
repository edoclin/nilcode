package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/20 11:52
 * @description
 */
@Data
@Accessors(chain = true)
public class ArchiveVo {
    
    private String blogId;
    private String blogName;
}