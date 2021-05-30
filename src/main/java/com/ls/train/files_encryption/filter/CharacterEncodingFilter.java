package com.ls.train.files_encryption.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    private String encoding = "UTF-8";
    private String contentType = "application/json;charset=UTF-8";

    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        String type = response.getContentType();
        System.out.println("type"+type);
        if(type == null || "".equals(type)){
            response.setContentType(contentType);
        }else if("application/x-msdownload".equals(type) || type.contains("image")) {
            response.setContentType(type);
        }else
            response.setContentType(contentType);
        chain.doFilter(request, response);

    }

    public void destroy() {
        // TODO Auto-generated method stub

    }


}