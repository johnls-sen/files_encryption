package com.ls.train.files_encryption.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class RSATest {

    @Test
    public void testEncrypt(){
        String key = UUID.randomUUID().toString().replace("-","").substring(0,16);
        System.out.println("加密前：key="+key);
        try{
            String enStr = RSAUtil.defaultEncrypt(key);
            String deStr = RSAUtil.defaultDecrypt(enStr);

            System.out.println("解密后：deStr="+deStr);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
