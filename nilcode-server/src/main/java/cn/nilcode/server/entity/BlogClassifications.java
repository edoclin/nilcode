package cn.nilcode.server.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 博客-分类表
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogClassifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.UUID)
    private String blogClassId;

    private String classId;

    private String blogId;


}
