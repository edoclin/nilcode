package cn.nilcode.server.vo;
import	java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/15 21:45
 * @description
 */
@Data
@Accessors(chain = true)
public class ClassificationVo {
    private String classId;
    /**
     * className
     */
    private String value;

    /**
     * className
     */
    private String label;
    private Integer relativeCount;
    private List<ClassificationVo> children;
}
