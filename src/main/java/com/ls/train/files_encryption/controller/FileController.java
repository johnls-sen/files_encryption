package com.ls.train.files_encryption.controller;

import com.alibaba.fastjson.JSONObject;
import com.ls.train.files_encryption.bean.FileBean;
import com.ls.train.files_encryption.bean.ResponseMessage;
import com.ls.train.files_encryption.service.FileService;
import com.ls.train.files_encryption.utils.AESUtil;
import com.ls.train.files_encryption.utils.MyCharacterUtil;
import com.ls.train.files_encryption.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping("/uploadFiled.do")
    public Object uploadFile(@RequestParam("file") MultipartFile multipartFile){
        if(fileService.addFile(multipartFile))
            return ResponseMessage.ok("message","上传成功！");
        return ResponseMessage.fail("message","上传失败！");
    }

    public Object deleteFile(String fileId){
        return ResponseMessage.ok("message","删除成功！");
    }

    @RequestMapping("/downloadFile.do")
    public void downloadFile(@RequestParam("fileId") String fileId, HttpServletResponse response){
        InputStream is =null;
        FileBean fileBean = fileService.findBeanById(fileId);
        if(fileBean==null || fileBean.getFileId()==null || "".equals(fileBean.getFileId())) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONObject.toJSONString(ResponseMessage.fail("message","下载失败")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            is = fileService.getFile(fileId);
            try {
                String fileName = new String(fileBean.getOriginName().getBytes("iso-8859-1"),"utf-8");
                response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"utf-8"));
                response.setContentType("application/x-msdownload");
                response.setCharacterEncoding("UTF-8");
                String key = RSAUtil.defaultDecrypt(fileBean.getEnvelope());
                AESUtil.decryptFile(is,response.getOutputStream(),key);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSONObject.toJSONString(ResponseMessage.fail("message","下载失败")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public Object updateFile(@RequestBody FileBean fileBean){
        return ResponseMessage.ok("message","修改成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/readFileList.do")
    public Object readFileList(){
        List<FileBean> fileBeans = fileService.findBeanList();
        if(fileBeans!=null && fileBeans.size()>0){
            return JSONObject.toJSON(ResponseMessage.ok("message","查询成功!","obj",fileBeans));
        }
        return JSONObject.toJSON(ResponseMessage.fail("message","查询失败!"));
    }

    @RequestMapping("/indexFile.do")
    public Object indexFile(@RequestBody Map<String,String> map, HttpServletResponse response){
        FileBean fileBean = null;
        if(map.containsKey("fileId")){
            String fileId = map.get("fileId");
            fileBean= fileService.findBeanById(fileId);
            return JSONObject.toJSON(ResponseMessage.ok("message","查询成功","obj",fileBean));
        }
        return JSONObject.toJSON(ResponseMessage.fail("message","查询失败!"));
    }

    @RequestMapping("testException")
    public Object testException(){
        int i = 1 / 0;
        return ResponseMessage.ok();
    }
}
