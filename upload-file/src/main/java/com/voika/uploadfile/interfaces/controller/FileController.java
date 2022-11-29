package com.voika.uploadfile.interfaces.controller;

import com.voika.uploadfile.infrastructure.JsonData;
import com.voika.uploadfile.infrastructure.config.SpringConfig;
import com.voika.uploadfile.infrastructure.exception.BusinessException;
import com.voika.uploadfile.infrastructure.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Resource(name = "springConfig")
    private SpringConfig springConfig;

    @Value("${my-config.file.upload-dir}")
    private String uploadDir;

    @Value("${my-config.file.show-mapping.dns}")
    private String dns;

    @Value("${my-config.file.show-mapping.port}")
    private int port;

    @Value("${my-config.file.show-mapping.mapping}")
    private String mapping;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public JsonData upload(MultipartFile file, HttpServletRequest request) {
        try {
            if (null == file) {
                throw new BusinessException("文件file不能为空");
            }
            // service
            String savePath = UrlUtil.resolveClassPath(uploadDir,request);
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String oldAllName = file.getOriginalFilename();
            String[] split = oldAllName.split("\\.");
            String suffix = split[split.length - 1];
            String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
            savePath = savePath + newFileName;
            log.info("savePath==============================================>{}", savePath);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(file.getBytes());
            fos.close();
            Map<String, Object> resp = new HashMap<>();
            StringBuffer path = new StringBuffer("http://");
            String contextPath = request.getContextPath();
            if (!mapping.startsWith("/")) {
                mapping = "/" + mapping;
            }
            if (!mapping.endsWith("/")) {
                mapping = mapping + "/";
            }
            path.append(dns).append(":").append(port).append(contextPath).append(mapping).append(newFileName);
            resp.put("path", path);
            return JsonData.success("上传成功", resp);
        } catch (BusinessException e) {
            int code = 0;
            return JsonData.error(e.getMessage(), code);
        } catch (Exception e) {
            String msg = "上传文件时出现异常";
            log.error(msg, e);
            return JsonData.error(msg);
        }
    }

}
