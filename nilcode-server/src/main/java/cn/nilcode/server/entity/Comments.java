package cn.nilcode.server.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.UUID)
    private String commentId;

    private Integer remoteIp;

    private String commentName;

    private LocalDateTime createDate;

    private String contact;

    private String content;

    private String blogId;

    private String parentCommentId;

    private Boolean audit;


}
