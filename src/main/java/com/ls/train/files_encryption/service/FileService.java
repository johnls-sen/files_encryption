package com.ls.train.files_encryption.service;

import com.ls.train.files_encryption.bean.FileBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public interface FileService extends BaseService<FileBean>{
    boolean addFile(MultipartFile multipartFile);

    InputStream getFile(String fileId);
}
