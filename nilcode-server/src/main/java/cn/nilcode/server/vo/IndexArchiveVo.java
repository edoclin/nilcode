package cn.nilcode.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zero`s
 * @version v1.0.0
 * @date 2020/1/20 11:43
 * @description
 */
@Data
@Accessors(chain = true)
public class IndexArchiveVo {

    private String date;
    private List<ArchiveVo> list;

}