package com.gm.javaeaseframe.common.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AES加密/解密工具(百度提供)
 * 
 * @author apple
 */
public class AESUtil {

    static Log log = LogFactory.getLog(AESUtil.class);

    /**
     * AES 加密
     * 
     * @param content   待加密的明文内容
     * @param password  密钥
     * @return          加密后的BASE64安全字符串
     * @throws Exception
     */
    public static String encrypt(String content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        if ((result != null) && (result.length > 0)) {
            return Base64.encodeBase64URLSafeString(result);
        }
        return null;
    }

    public static String encryptForApp(String content, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        if ((result != null) && (result.length > 0)) {
            // return Base64.encodeBase64URLSafeString(result);
            return new String(Base64.encodeBase64(result));
        }
        return null;
    }

    /**
     * AES 解密
     * 
     * @param content    加密后的BASE64安全字符串
     * @param password   密钥
     * @return           明文内容
     * @throws Exception
     */
    public static String decrypt(String content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes("UTF-8"));
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] tmp = Base64.decodeBase64(content);
        byte[] result = cipher.doFinal(tmp);
        if ((result != null) && (result.length > 0)) {
            log.debug("[AESUtils][decrypt][result.length]:" + result.length);
            log.debug("[AESUtils][decrypt][result]:" + new String(result, "utf-8"));
            return new String(result, "UTF-8");
        } else {
            log.debug("[AESUtils][decrypt][result] is null");
        }
        return null;
    }

    public static String decryptForApp(String content, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] tmp = Base64.decodeBase64(content);
        byte[] result = cipher.doFinal(tmp);
        if ((result != null) && (result.length > 0)) {
            log.debug("[AESUtils][decrypt][result.length]:" + result.length);
            log.debug("[AESUtils][decrypt][result]:" + new String(result, "utf-8"));
            return new String(result, "UTF-8");
        } else {
            log.debug("[AESUtils][decrypt][result] is null");
        }
        return null;
    }
}
