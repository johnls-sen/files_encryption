package com.ls.train.files_encryption.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

public class RsaAndAesTest {

    @Test
    public void fileEncryptAndDecrypt(){

        String key = UUID.randomUUID().toString().replace("-","").substring(0,16);

        try{
            FileInputStream fis = new FileInputStream(new File("H:\\各套项目一\\files_encryption\\upload/test.txt"));
            FileOutputStream fos = new FileOutputStream(new File("H:\\各套项目一\\files_encryption\\upload/dest.txt"));


            AESUtil.encryptFile(fis,fos,key);
            String enKey = RSAUtil.defaultEncrypt(key);
            System.out.println();
            FileInputStream fileInputStream = new FileInputStream(new File("H:\\各套项目一\\files_encryption\\upload/dest.txt"));
            FileOutputStream fileOutputStream = new FileOutputStream(new File("H:\\各套项目一\\files_encryption\\upload/source.txt"));
            String deKey = RSAUtil.defaultDecrypt(enKey);
            AESUtil.decryptFile(fileInputStream,fileOutputStream,deKey);
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
