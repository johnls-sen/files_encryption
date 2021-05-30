package com.ls.train.files_encryption.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ls.train.files_encryption.bean.FileBean;
import com.ls.train.files_encryption.utils.ApiUtil;
import com.ls.train.files_encryption.utils.MyServerRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service("fileService")
public class FileServiceImp implements FileService {

    private final static String DOMAIN = "http://localhost:8082/";//请求服务端地址

    public boolean addBean(FileBean bean){
        return false;
    }

    public FileBean findBeanById(String beanId){
        FileBean fileBean = null;
        String website = DOMAIN + ApiUtil.INDEX_FILE;
        String jsonString = MyServerRequest.sendGet(website,"fileId="+beanId);
        if("".equals(jsonString)){
            return fileBean;
        }
        JSONObject object = JSONObject.parseObject(jsonString);
        fileBean = object.getObject("obj",FileBean.class);
        return fileBean;
    }

    public List<FileBean> findBeanList(){
        List<FileBean> list = null;
        String website = DOMAIN + ApiUtil.LIST_FILE;
        String jsonString = MyServerRequest.sendGet(website);
        if("".equals(jsonString)){
            return list;
        }
        JSONObject object = JSONObject.parseObject(jsonString);
        JSONArray array = object.getJSONArray("obj");
        list = array.toJavaList(FileBean.class);
        return list;
    }

    public boolean modifyBean(FileBean bean){
        return false;
    }

    public boolean removeBean(String beanId){
        return false;
    }

    @Override
    public boolean addFile(MultipartFile multipartFile) {
        String website = DOMAIN + ApiUtil.UPLOAD_FILE;
        String jsonString = MyServerRequest.sendPost(website,multipartFile);
        if("".equals(jsonString)){
            return false;
        }
        JSONObject object = JSONObject.parseObject(jsonString);
        boolean isSuccess = object.getBoolean("flag");
        return isSuccess;
    }

    @Override
    public InputStream getFile(String fileId) {
        String website = DOMAIN + ApiUtil.DOWNLOAD_FILE;
        InputStream inputStream = MyServerRequest.get(website,"fileId="+fileId);
        return inputStream;
    }
}
