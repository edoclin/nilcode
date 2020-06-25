package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/18 15:28
 * @description
 */
@Data
@Accessors(chain = true)
public class AutoCompleteSearchVo {
    private String data;
    private String value;
    private String type;
}