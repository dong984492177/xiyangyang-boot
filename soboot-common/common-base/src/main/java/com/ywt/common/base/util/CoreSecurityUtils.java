package com.ywt.common.base.util;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.ywt.common.base.constant.CharsetConstant;
import com.ywt.common.base.constant.CharsetConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: 安全工具集合类
 * 转码工具: 编码 解码
 * BASE64、HEX
 * 消息摘要：单向散列
 * MD5、SHA1、CRC32
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class CoreSecurityUtils {

    private final transient static Logger logger = LoggerFactory.getLogger(CoreSecurityUtils.class);

    private static final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1PADDING";

    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";

    // RSA密钥长度
    private static final int KEY_SIZE = 2048;

    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    /**
     * base64编码
     * @param input 输入数据
     * @return 编码结果
     */
    public static String encodeBase64(byte[] input) {
        return BaseEncoding.base64().encode(input);
    }

    /**
     * base64解码
     * @param encodeString 已编码字符串
     * @return 解码结果
     */
    public static byte[] decode(String encodeString) {
        return BaseEncoding.base64().decode(encodeString);
    }

    /**
     * 十六进制编码
     * @param input 输入数据
     * @return 编码结果 小写字母
     */
    public static String encodeHex(byte[] input) {
        return BaseEncoding.base16().lowerCase().encode(input);
    }

    /**
     * 十六进制解码
     * @param encodeString 已编码字符串 小写字母
     * @return 解码结果
     */
    public static byte[] decodeHex(String encodeString) {
        return BaseEncoding.base16().lowerCase().decode(encodeString);
    }

    /**
     * MD5摘要
     * @param input 输入数据
     * @return 摘要结果
     */
    public static String digestMD5(byte[] input) {
        return Hashing.md5().hashBytes(input).toString();
    }

    /**
     * SHA1摘要
     * @param input 输入数据
     * @return 摘要结果
     */
    public static String digestSHA1(byte[] input) {
        return Hashing.sha1().hashBytes(input).toString();
    }

    /**
     * SHA256摘要
     * @param input 输入数据
     * @return 摘要结果
     */
    public static String digestSHA256(byte[] input) {
        return Hashing.sha256().hashBytes(input).toString();
    }

    /**
     * CRC32摘要
     * 结果与jdk自带crc32结果不一致，该方法只用于内部业务逻辑计算checksum值，不能对外业务使用
     * 参照 <a href="https://github.com/google/guava/issues/1332">https://github.com/google/guava/issues/1332</>
     * @param input 输入数据
     * @return 摘要结果
     */
    public static long digestCRC32(byte[] input) {
        return Hashing.crc32c().hashBytes(input).padToLong();
    }

    /**
     * RSA用私钥对信息生成数字签名
     *
     * @param input 源数据
     * @param encodedPrivateKey 私钥
     * @return 签名结果
     */
    public static byte[] signRSA(byte[] input, byte[] encodedPrivateKey) throws RuntimeException {
        try {
            //构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //取私钥匙对象
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //用私钥对信息生成数字签名
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateKey);
            signature.update(input);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA用公钥验证数字签名
     *
     * @param input 源数据
     * @param encodedPublicKey 公钥
     * @param sign 签名
     * @return 签名结果
     */
    public static boolean verifySignRSA(byte[] input, byte[] encodedPublicKey, byte[] sign) throws RuntimeException {
        try {
            //构造X509EncodedKeySpec对象
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //取公钥匙对象
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            //用公钥对信息生成数字签名
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(publicKey);
            signature.update(input);
            //验证签名是否正常
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream
                out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        return out.toByteArray();
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptRSA(String data, String publicKey){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(BaseEncoding.base64().decode(publicKey));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return BaseEncoding.base64().encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CharsetConstant.CHARSET_UTF8), key.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */
    public static String decryptRSA(String data, String privateKey){
        try{
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(BaseEncoding.base64().decode(privateKey));
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, BaseEncoding.base64().decode(data), key.getModulus().bitLength()), CharsetConstant.CHARSET_UTF8);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 初始化RSA密钥对
     * @return RSA密钥对
     * @throws Exception 抛出异常
     */
    public static Map<String, String> initKey() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom secrand = new SecureRandom();
            secrand.setSeed("hahaha".getBytes());// 初始化随机产生器
            keygen.initialize(KEY_SIZE, secrand); // 初始化密钥生成器
            KeyPair keys = keygen.genKeyPair();
            String pub_key = BaseEncoding.base64().encode(keys.getPublic().getEncoded());
            String pri_key = BaseEncoding.base64().encode(keys.getPrivate().getEncoded());
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put(PUBLIC_KEY, pub_key);
            keyMap.put(PRIVATE_KEY, pri_key);
            return keyMap;
        } catch(Exception e){
            throw new RuntimeException("初始化RSA密钥对时遇到异常", e);
        }
    }

    /**
     * 3des加密
     * ECB模式，不需要初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @return 加密后的结果
     */
    public static byte[] encryptDESede(byte[] input, byte[] encodedKey) throws RuntimeException {
        try {
            //构造DESedeKeySpec对象
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(encodedKey);
            //指定加密算法
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            //取对称密钥对象
            SecretKey secretKey = secretKeyFactory.generateSecret(deSedeKeySpec);
            //加密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3des解密
     * ECB模式，不需要初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @return 解密后的结果
     */
    public static byte[] decryptDESede(byte[] input, byte[] encodedKey) throws RuntimeException {
        try {
            //构造DESedeKeySpec对象
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(encodedKey);
            //指定加密算法
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            //取公钥匙对象
            SecretKey secretKey = secretKeyFactory.generateSecret(deSedeKeySpec);
            //解密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3des加密
     * CBC模式，需要64位初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @param iv 8字节初始化向量
     * @return 加密后的结果
     */
    public static byte[] encryptDESede(byte[] input, byte[] encodedKey, byte[] iv) throws RuntimeException {
        try {
            //构造DESedeKeySpec对象
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(encodedKey);
            //指定加密算法
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            //取公钥匙对象
            SecretKey secretKey = secretKeyFactory.generateSecret(deSedeKeySpec);
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //加密
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3des解密
     * CBC模式，需要64位初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @param iv 8字节初始化向量，与加密时保持一致
     * @return 解密后的结果
     */
    public static byte[] decryptDESede(byte[] input, byte[] encodedKey, byte[] iv) throws RuntimeException {
        try {
            //构造DESedeKeySpec对象
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(encodedKey);
            //指定加密算法
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            //取公钥匙对象
            SecretKey secretKey = secretKeyFactory.generateSecret(deSedeKeySpec);
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //解密
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * aes加密
     * ECB模式，不需要初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @return 加密后的结果
     */
    public static byte[] encryptAES(byte[] input, byte[] encodedKey) throws RuntimeException {
        try {
            //构造SecretKeySpec对象
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * aes解密
     * ECB模式，不需要初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @return 解密后的结果
     */
    public static byte[] decryptAES(byte[] input, byte[] encodedKey) throws RuntimeException {
        try {
            //构造SecretKeySpec对象
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            //解密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * aes加密
     * CBC模式，需要128位初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @param iv 16字节初始化向量
     * @return 加密后的结果
     */
    public static byte[] encryptAES(byte[] input, byte[] encodedKey, byte[] iv) throws RuntimeException {
        try {
            //构造SecretKeySpec对象
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //加密
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     * CBC模式，需要128位初始化向量
     * @param input 源数据
     * @param encodedKey 私钥
     * @param iv 16字节初始化向量，与加密时保持一致
     * @return 解密后的结果
     */
    public static byte[] decryptAES(byte[] input, byte[] encodedKey, byte[] iv) throws RuntimeException {
        try {
            //构造SecretKeySpec对象
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //解密
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        CoreSecurityUtils.initKey();

        String card = encryptRSA("{\"cardNo\":\"sss\"}", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqv+zOyQYHxEDmHTHsfYHx/ecG02XT0jplMnouNLQpE0TsMjZGboyA6egv2AIsEDwqD0p5fMqYOA+jq9VLAoCa85vZmH3JjSyBbcsq+nQMedQYgF/LuJG7riysR0fXAo/A+rAJhOp9GubmBrmnH8mSWXdMsrj01KxeMdU5DuLLjrxeMOTTj+pVpasnt9XmRU+EPDmKVfAIsO06OBzDxWmOSXcDSLQDbppOHO+pmzqZx9xBQ9Tq9pGkhUR77C6TnH0HwrrJtQrqNjEKN1sP/yHCdd/NPBU49VkLWu5uVZvu2Ki2yGJ/cDc+Axpw48ln4BJxgScd+yfVWGUge2ASTZX9QIDAQAB");
        System.out.println(card);

        String encode = URLEncoder.encode(card, "utf-8");
        System.out.println(encode);

        String decode = URLDecoder.decode(encode, "utf-8");
        System.out.println(decode);

        String card2 = decryptRSA(decode, "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCq/7M7JBgfEQOYdMex9gfH95wbTZdPSOmUyei40tCkTROwyNkZujIDp6C/YAiwQPCoPSnl8ypg4D6Or1UsCgJrzm9mYfcmNLIFtyyr6dAx51BiAX8u4kbuuLKxHR9cCj8D6sAmE6n0a5uYGuacfyZJZd0yyuPTUrF4x1TkO4suOvF4w5NOP6lWlqye31eZFT4Q8OYpV8Aiw7To4HMPFaY5JdwNItANumk4c76mbOpnH3EFD1Or2kaSFRHvsLpOcfQfCusm1Cuo2MQo3Ww//IcJ13808FTj1WQta7m5Vm+7YqLbIYn9wNz4DGnDjyWfgEnGBJx37J9VYZSB7YBJNlf1AgMBAAECggEAApdHPvEoEGhwloHGeNLSgVwmGF/PmBrxtxhsF0jScnrcfoQ8jeA9W8LRc7TfzNPfEZ2DogFSNg+PwxezAazRJpMFKxb26LRJsj55Rc2QIwFvPVzqcWpqSrmvs3FOhVV+aO/Io4sOuy5aOC7IKbzfdqM9e8SJOS16lOroRWEFldi2WicpbzGGXQZZ3X6Qxvc5T3DqO276O4NUL28J1XIvPRZYNeOkOWGzwJaVcEiI9PoLyd65sEZtSkHJROFCiwWK30KnCYnehJ6DiOg8WS7cAaLXPhL7auAO061IyFoWuHzSeRAHO4mZ+hdoEyaEN9bJ9Eyf0A1Io7C7L9uYkhTmrQKBgQD4/5e/2sVwIZ7rPoUJYqNX986hWgdJbK1li8rYORuiFTeVsm1GKFZ/VBlmYiFB31nfH1HHIdGT4FOoLx9skzPzUzthk9oy2zEb7Obd+GpmQlz5p6KwmJGE+oSTBseR6UfhUm9YNRNSygcjRiNWE2SqSHIIF/C1hz4Yf1qTMwpxuwKBgQCvzqE7xTZG/JULZ/nRClwsDLN/PdvDqx1ZOK0f/vZ8+wwad58UlsdkRjtmvPR1WmW6IvcnOMu13pgHW18uNOdofAOJhl4+SOCXoE7quTWG//oqkaDJMmSwmsUAot16ate8qlbbBAQHjU848MPiiH8wWp6q4y6FyKERIIkE40MqDwKBgEsc/3AW1Nygp8obUBKmuNO+o4y1FeGmcSBi7UwQ3i8k62GzM5eob391pTvnelTexcOzNP43S3uqflIL9RTRyygqAY3MBlS1Hrln27U2TRKQDnAZi9BuFefhksCvmOCBWPe14HThffTJkSGo9NvwebuMoymh3zFDnQ3SzUX+bbYlAoGASwtx6biiFaUl3guRKLk6ZRc627rE/34gdgBqbjkAcJMdNIMRZdGC5XvRSWapFxryR4U7aQOmeUi+Lo3DUE8G5KPjQqYXvpCPRkoIxcsvXSQRXnBxaNDZCEXYiA/9aI8I7iDsGGtbaPnZ8l1b0Gwhlrtn9VwqdLMSheQUeQCln+MCgYArNLavvHXh0tGvZe6EMVjUvUfZGZ7visi3hEuwUgUwPrcdeQa3LJArViWT37xs4O8TKo/48ILT0am9RLFxIn1ZygW/hFjoCrImG5rTwZ+GhBlgdF3lG4cpkAm+qtMylAkDFEBoYwMz5vFhADr9Kbi+zHZpqBJ+fSK0cIsgdQI2Ow==");
        System.out.println(card2);


    }
}

