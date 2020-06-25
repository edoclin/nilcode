package cn.nilcode.server.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户-角色表
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uRId;

    private String roleId;

    private String userId;


}
