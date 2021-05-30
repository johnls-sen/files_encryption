package com.ls.train.files_encryption.utils;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerRequest {

    public static String sendGet(String url){
        String result = "";
        try{
            //创建HTTP客服端模拟,设置连接属性
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(180000);
            httpClient.getParams().setContentCharset("UTF-8");

            //创建get请求，添加请求头
            String uuid = UUID.randomUUID().toString();
            GetMethod get = new GetMethod(url);
            get.setRequestHeader("X-SID", uuid);
            get.setRequestHeader("X-Signature",RSAUtil.defaultSign(uuid));

            //执行并解析请求
            int status = httpClient.executeMethod(get);
            if(status == HttpStatus.SC_OK){
                result = get.getResponseBodyAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String sendGet(String url,String params){
        String newUrl = url+"?"+params;
        return sendGet(newUrl);
    }

    public static InputStream get(String url,String params){
        String result = "";
        String newUrl = url+"?"+params;
        InputStream inputStream = null;
        try{
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(180000);
            httpClient.getParams().setContentCharset("UTF-8");

            String uuid = UUID.randomUUID().toString();
            GetMethod get = new GetMethod(newUrl);
            get.setRequestHeader("X-SID", uuid);
            get.setRequestHeader("X-Signature",RSAUtil.defaultSign(uuid));

            int status = httpClient.executeMethod(get);
            if(status == HttpStatus.SC_OK){
                inputStream = get.getResponseBodyAsStream();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return inputStream;
    }

    public static void sendGet(String url,String params,OutputStream outputStream){
        String result = "";
        String newUrl = url+"?"+params;
        InputStream inputStream = null;
        try{
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(180000);
            httpClient.getParams().setContentCharset("UTF-8");

            String uuid = UUID.randomUUID().toString();
            GetMethod get = new GetMethod(newUrl);
            get.setRequestHeader("X-SID", uuid);
            get.setRequestHeader("X-Signature",RSAUtil.defaultSign(uuid));

            int status = httpClient.executeMethod(get);
            if(status == HttpStatus.SC_OK){
                inputStream = get.getResponseBodyAsStream();
                byte[] bytes = new byte[1024];
                int len = -1;
                while((len = inputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendPost(String url, MultipartFile multipartFile){
        String result = "";
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post =new HttpPost(url);

            String uuid = UUID.randomUUID().toString();
            post.addHeader("X-SID",uuid);
            post.addHeader("X-Signature", RSAUtil.defaultSign(uuid));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("utf-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
            builder.addBinaryBody("file",multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA,
                    multipartFile.getOriginalFilename());// 文件流
            post.setEntity(builder.build());

            HttpResponse response = httpClient.execute(post);
            StatusLine statusLine= response.getStatusLine();

            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                HttpEntity responseEntity = response.getEntity();
                result = EntityUtils.toString(responseEntity);
                System.out.println("result"+result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
