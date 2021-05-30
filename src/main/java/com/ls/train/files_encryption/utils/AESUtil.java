package com.ls.train.files_encryption.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.UUID;

/**
 * @author ls
 * @description 使用AES下ECB模型128位算法，字符串加和文件加解密
 * @date 2021.05.06
 */
public class AESUtil {

    private final static String ALGORITHM_PATTERN="AES/ECB/PKCS5Padding";

    private final static String SECRET_KEY = "LsAndGcl1314love";

    private final static String CHARSET = "UTF-8";

    private final static String ALGORITHM = "AES";

    public static String RANDOM_KEY ;


    /**
     * 随机秘钥加密
     * @param str
     * @return
     */
    public static String randomEncrypt(String str){
        return encrypt(str);
    }

    /**
     * 随机秘钥加密，可输入
     * @param str
     * @param secretKey
     * @return
     */
    public static String randomEncrypt(String str,String secretKey){
        RANDOM_KEY = secretKey;
        return encrypt(str,RANDOM_KEY);
    }

    /**
     * 默认秘钥加密
     * @param str
     * @return
     */
    public static String defaultEncrypt(String str){
        return encrypt(str,SECRET_KEY);
    }

    /**
     * 随机秘钥解密
     * @param str
     * @return
     */
    public static String randomDecrypt(String str){
        return decrypt(str);
    }

    /**
     * 随机秘钥解密，可输入
     * @param str
     * @param secretKey
     * @return
     */
    public static String randomDecrypt(String str,String secretKey){
        RANDOM_KEY = secretKey;
        return decrypt(str,RANDOM_KEY);
    }

    /**
     *
     * @param str
     * @return
     */
    public static String defaultDecrypt(String str){
        return decrypt(str,SECRET_KEY);
    }


    public static String encrypt(String str){
        RANDOM_KEY = UUID.randomUUID().toString().substring(0,16);
        return base64Encode(encryptToBytes(str,RANDOM_KEY));
    }

    public static String encrypt(String str,String secretKey){
        return base64Encode(encryptToBytes(str,secretKey));
    }

    public static String decrypt(String str){
        String result = null;
        try{
            result = new String(decryptToBytes(base64Decode(str),RANDOM_KEY));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String str,String secretKey){
        String result = null;
        try{
            result = new String(decryptToBytes(base64Decode(str),secretKey));
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static byte[] encryptToBytes(String str,String secretKey){
        byte[] enBytes = null;
        try{
            //创建秘钥生成器
            KeyGenerator keyGenerator =  KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128);

            Cipher cipher = Cipher.getInstance(ALGORITHM_PATTERN);
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(secretKey.getBytes(),ALGORITHM));
            enBytes = cipher.doFinal(str.getBytes(CHARSET));

        }catch (Exception e){
            e.printStackTrace();
        }
        return enBytes;
    }

    public static byte[] decryptToBytes(byte[] str,String secretKey){
        byte[] deBytes = null;
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128);

            Cipher cipher =  Cipher.getInstance(ALGORITHM_PATTERN);
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(secretKey.getBytes(),ALGORITHM));
            deBytes = cipher.doFinal(str);

        }catch (Exception e){
            e.printStackTrace();
        }

        return deBytes;
    }

    public static CipherInputStream encryptFileToInputStream(InputStream inputStream,String secretKey){
        CipherInputStream cis = null;
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128);

            Cipher cipher =  Cipher.getInstance(ALGORITHM_PATTERN);
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(secretKey.getBytes(),ALGORITHM));
            cis= new CipherInputStream(inputStream,cipher);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cis;
    }


    public static void encryptFile(InputStream inputStream,OutputStream outputStream, String secretKey){
        CipherInputStream cis = null;

        try{

            cis= encryptFileToInputStream(inputStream,secretKey);

            byte[] bytes = new byte[1024];
            int len = -1;
            while( (len=cis.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(cis!=null){
                    cis.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static CipherInputStream decryptFileToInputStream(InputStream inputStream,String secretKey){
        CipherInputStream cis = null;
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(128);

            Cipher cipher =  Cipher.getInstance(ALGORITHM_PATTERN);
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(secretKey.getBytes(),ALGORITHM));
            cis = new CipherInputStream(inputStream,cipher);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cis;
    }

    public static void decryptFile(InputStream inputStream,OutputStream outputStream, String secretKey){
        CipherInputStream cis = null;

        try{

            cis= decryptFileToInputStream(inputStream,secretKey);

            byte[] bytes = new byte[1024];
            int len = -1;
            while( (len=cis.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(cis!=null){
                    cis.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64Decode(String str) throws Exception{
        return Base64.decodeBase64(str.getBytes(CHARSET));
    }

}
