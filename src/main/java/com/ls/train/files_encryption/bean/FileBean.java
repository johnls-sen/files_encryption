package com.ls.train.files_encryption.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息持久化类
 * @author john森
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String fileId;//文件保存id

    private String fileName;//文件当前名称

    private String originName;//文件原始名称

    private String fileType;//文件类型

    private float fileSize;//文件大小

    private Date uploadDate;//文件上传日期

    private String savePath;//文件保存目录

    private String envelope;//文件加密信封

}
