package cn.nilcode.server.controller;


import cn.hutool.core.convert.impl.UUIDConverter;
import cn.hutool.core.lang.UUID;
import cn.nilcode.server.entity.Files;
import cn.nilcode.server.service.IFilesService;
import cn.nilcode.server.util.Result;
import cn.nilcode.server.util.ResultCode;
import cn.nilcode.server.vo.UploadFileVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author nilcode
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/server/files")
public class FilesController {

    @Autowired
    private IFilesService iFilesService;

    @Value("${admin.file-upload-path}")
    private String path;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile[] files, HttpServletRequest request) {
        if (null == files || files.length < 1) {
            return "请选择文件";
        }

        File upload = new File(path + "/files");

        //文件目录不存在，就创建一个
        if (!upload.isDirectory()) {
            upload.mkdirs();
        }
        try {
            List<Files> save = new ArrayList<>(files.length);

            for (MultipartFile file : files) {

                String fileName = file.getOriginalFilename();

                if ((null != fileName) && (!"".equals(fileName))) {
                    String[] split = fileName.split("\\.");
                    String fileId = UUID.fastUUID().toString().replaceAll("-", "");
                    String savedFileName = new Timestamp(System.currentTimeMillis()).toString().replaceAll(":", "-") + "-" + fileId + "." + split[split.length - 1];
                    File fileServer = new File(upload, savedFileName);
                    file.transferTo(fileServer);
                    String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/files/" + savedFileName;


                    save.add(new Files()
                            .setFileId(fileId)
                            .setOriginalFileName(fileName)
                            .setSaveFileName(savedFileName)
                            .setFileUrl(filePath));
                }
            }
            System.out.println(save);
            iFilesService.saveBatch(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.build(ResultCode.SAVE_SUCCESS);
    }

    @GetMapping("/get/{page}")
    public String getFileByPage(@PathVariable Integer page) {
        Page<Files> one = new Page<>(page, 12);

        List<UploadFileVo> vos = new ArrayList<>();
        iFilesService.page(one).getRecords().forEach(file -> {
            vos.add(new UploadFileVo()
                    .setFileId(file.getFileId())
                    .setOriginalFileName(file.getOriginalFileName())
                    .setFileUrl(file.getFileUrl()));
        });
        return Result.build(ResultCode.QUERY_SUCCESS, vos);
    }

    @GetMapping("/total")
    public String filesTotal() {
        return Result.build(ResultCode.QUERY_SUCCESS, "" + iFilesService.count());
    }

    @GetMapping("/del/{fileId}")
    public String delByFileId(@PathVariable String fileId) {
        Files byId = iFilesService.getOne(Wrappers.<Files>lambdaQuery().eq(Files::getFileId, fileId));
        File upload = new File(path + "/files", byId.getSaveFileName());

        if (upload.delete() && iFilesService.remove(Wrappers.<Files>lambdaQuery().eq(Files::getFileId, fileId))) {
            return Result.build(ResultCode.DEL_SUCCESS);
        }
        return Result.build(ResultCode.SERVER_ERROR);
    }


}
