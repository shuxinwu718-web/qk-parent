package com.qk.controller;

import com.qk.common.Result;
import com.qk.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Value("${file.upload.path:images/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result upload(HttpServletRequest request) {
        // 获取所有上传的文件（只处理文件，不处理普通表单字段）
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // 获取所有文件的参数名
        Iterator<String> fileNames = multipartRequest.getFileNames();

        MultipartFile file = null;
        String paramName = null;

        // 遍历找到第一个文件
        while (fileNames.hasNext()) {
            String name = fileNames.next();
            MultipartFile tempFile = multipartRequest.getFile(name);
            if (tempFile != null && !tempFile.isEmpty()) {
                file = tempFile;
                paramName = name;
                break;
            }
        }

        // 检查是否有文件
        if (file == null) {
            log.warn("没有接收到任何文件");
            throw new BusinessException("请选择要上传的文件");
        }

        log.info("接收到文件: 参数名={}, 原始文件名={}, 大小={}KB",
                paramName,
                file.getOriginalFilename(),
                file.getSize() / 1024);

        // 生成新文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + extension;

        // 保存文件
        String projectRoot = System.getProperty("user.dir");
        String uploadDirPath = Paths.get(projectRoot, uploadPath).toString();

        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File destFile = new File(uploadDirPath + File.separator + newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new BusinessException("文件上传失败，请稍后重试");
        }

        String fileUrl = "/images/" + newFileName;
        log.info("文件上传成功: {}", fileUrl);
        return Result.success(fileUrl);
    }
}