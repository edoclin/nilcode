package cn.nilcode.server.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.UUID)
    private String blogId;

    private LocalDateTime createDate;

    private String coverImg;

    private String title;

    private String content;

    private Integer clickCount;

    private LocalDate publishedDate;

    private Boolean draft;

    private String userId;


}
