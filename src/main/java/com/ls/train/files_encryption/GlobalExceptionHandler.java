package com.ls.train.files_encryption;

import com.ls.train.files_encryption.bean.ResponseMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object globalException(HttpServletResponse response, Exception ex){
        Map<String,Object> objectMap = new HashMap<String,Object>();
        ex.printStackTrace();
        objectMap.put("message","发生错误");
        objectMap.put("错误代码", ex.getMessage());
        objectMap.put("错误信息",ex.getStackTrace()[0]);
       return ResponseMessage.fail(objectMap);
    }
}
