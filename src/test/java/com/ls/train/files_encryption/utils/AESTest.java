package com.ls.train.files_encryption.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

public class AESTest {

    @Test
    public void testEncryptFile(){
        try{
            String key = UUID.randomUUID().toString().substring(0,16);
            FileInputStream fis = new FileInputStream(new File("H:\\各套项目一\\files_encryption\\upload/test.txt"));
            FileOutputStream fos = new FileOutputStream(new File("H:\\各套项目一\\files_encryption\\upload/dest.txt"));


            AESUtil.encryptFile(fis,fos,key);

            FileInputStream fileInputStream = new FileInputStream(new File("H:\\各套项目一\\files_encryption\\upload/dest.txt"));
            FileOutputStream fileOutputStream = new FileOutputStream(new File("H:\\各套项目一\\files_encryption\\upload/source.txt"));
            AESUtil.decryptFile(fileInputStream,fileOutputStream,key);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
