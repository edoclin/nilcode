package cn.nilcode.server.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 链接表
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Links implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.UUID)
    private String linkId;

    private String linkName;

    private String linkUrl;

    private String linkType;


}
