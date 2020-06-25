package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/16 18:18
 * @description
 */
@Data
@Accessors(chain = true)
public class UploadFileVo {
    private String fileId;
    private String originalFileName;
    private String fileUrl;
}
