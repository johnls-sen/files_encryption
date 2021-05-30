package com.ls.train.files_encryption.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {

    private final static String CHARSET = "UTF-8";

    private final static String ALGORITHM = "RSA";

    private final static String SIGN_ALGORITHM = "SHA256withRSA";

    private final static String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbyH8AeznlnXlPRbfIzUTX/cGh38XvVFymG9i9HnzEUxF7g0Kx0iZ+So1g5dmzkqNeqe3hM2L7bFcfNzsqojNbvXS4et7Bo0cgGNOmYt1iiAz2S4tJVHGGK778CVpBQzzBj7d5eJb0CRR5FlPL4MSZmzphZlsQYin6n56cB3HL4wIDAQAB";//加密公钥

    private final static String PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJvIfwB7OeWdeU9Ft8jNRNf9waHfxe9UXKYb2L0efMRTEXuDQrHSJn5KjWDl2bOSo16p7eEzYvtsVx83OyqiM1u9dLh63sGjRyAY06Zi3WKIDPZLi0lUcYYrvvwJWkFDPMGPt3l4lvQJFHkWU8vgxJmbOmFmWxBiKfqfnpwHccvjAgMBAAECgYA4ZlNMqlBgyC12PMh4A5EMHcvvviNcLY/Jd8V1WsdTELwAmpoX5PyYtocO8MhrkghhqmX8JW2Nl0DHhWh/W+/5kvEHs7z1IxkIbeP1Qs0gXhcqDxHc0XnbfEemFdHfXd/XIObpg5lvvHisy5H9oyyx3Pij7M56cRSETixCMEHYYQJBANSJLWOcg3Ljxo6RL7UDqEzJ9XUmaHCod+FOLbBtrJhKbmkG8o1ED0BduBlrtbn9pguw7WVlkPzW2TQvfHlOxVECQQC7pCoeRonX+lo7PqR7kVbvMNUSuq1NUeiwMamdfcAC8VJE6QQMk8cJ5iNUozGpo14ZSl5ggWlu8ENdz9BdxIDzAkEAqRVBAOBBSyOekUk79Pwl5oE9P6gAqq+8P4hcnOGDz9xXHkMir/QXVhO5JLAl8QO0F9T36BT3Vc7vGa81jaR3IQJAIphi15Ajy/kxd6jWBJ5IS/NR5ZEbcJ0AbLPLVOkhZKl/hIr9nyAD23Rd4TXxaxheugT3H43clYKguQQeo5seNwJAI0xASalEZSO+PEuq6M507GMjg9KIhJerqbBJNFN66s5r0HymEu0gyXHEIclZan78RIsGRf+wN0lKu6zn/cxTXQ==";//加密私钥

    public static Map<String,String> keyMap = new HashMap<>();


    public static void generateKey(){
        try{
            //创建秘钥生成器，keyPairGenerator
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(1024,new SecureRandom());
            //生成秘钥对,并获取公钥，私钥
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPrivateKey privateKey =(RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey =(RSAPublicKey) keyPair.getPublic();

            //将公私钥转换为字符串存储到keyMap
            String publicKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
            String privateKeyStr = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
            keyMap.put("publicKey",publicKeyStr);
            keyMap.put("privateKey",privateKeyStr);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 随机公钥加密,应用随机公钥加密会调用公钥秘钥生成器，同时应持久化保存公钥私钥
     * @param str
     * @return
     * @throws Exception
     */

    public static String randomEncrypt(String str) throws Exception{
        generateKey();
        byte[] publicKey = Base64.getDecoder().decode(keyMap.get("publicKey"));
        return encrypt(str,publicKey);
    }

    /**
     * 随机私钥解密，在调用此方法前需要先调用私钥设置方法，在随机公钥加密阶段需要保存私钥
     * @param str
     * @return
     * @throws Exception
     */
    public static String randomDecrypt(String str) throws Exception{
        byte[] privateKey = Base64.getDecoder().decode(keyMap.get("privateKey"));
        return  decrypt(str,privateKey);
    }

    public static String randomDecrypt(String str,String privateKeyStr) throws Exception{
        byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
        return  decrypt(str,privateKey);
    }

    /**
     * 默认公钥加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String defaultEncrypt(String str) throws Exception{
        byte[] publicKey = Base64.getDecoder().decode(PUBLIC_KEY);
        return encrypt(str,publicKey);
    }

    /**
     * 默认私钥解密
     * @param str
     * @return
     * @throws Exception
     */
    public static String defaultDecrypt(String str) throws Exception{
        byte[] privateKey = Base64.getDecoder().decode(PRIVATE_KEY);
        return decrypt(str,privateKey);
    }

    /**
     * 公钥加密
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String str,byte[] publicKey) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,rsaPublicKey);
        String enStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(CHARSET)));
        return  enStr;
    }

    /**
     * 私钥解密
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String str,byte[] privateKey) throws Exception{

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,rsaPrivateKey);
        String enStr = new String(cipher.doFinal(Base64.getDecoder().decode(str.getBytes(CHARSET))));
        return  enStr;
    }

    public static String  getRandomPublicKey(){
        if(keyMap.containsKey("publicKey")){
            return keyMap.get("publicKey");
        }
        return "";
    }

    public static String  getRandomPrivateKey(){
        if(keyMap.containsKey("privateKey")){
            return keyMap.get("privateKey");
        }
        return "";
    }

    public static String defaultSign(String content){
        return signContent(content,PRIVATE_KEY);
    }

    public static String signContent(String content,String privateKeyString){
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        KeyFactory keyFactory = null;
        byte[] sign = null;
        try {
            //获取秘钥
            keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //实例化签名
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            //初始化签名
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            sign = signature.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(sign);
    }

    public static void  setRandomPublicKey(String publicKey){
        keyMap.put("publicKey",publicKey);
    }

    public static void  setRandomPrivateKey(String privateKey){
        keyMap.put("privateKey",privateKey);
    }
}
