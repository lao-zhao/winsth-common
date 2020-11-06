package com.winsth.android.libs.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SecurityUtil {
    private static final String ALGORITHM = "DES";
    private static final String ENCODINGTYPE = ConfigUtil.TextCode.GB2312;

    private SecurityUtil() {
    }

    /**
     * 加密
     *
     * @param string 原始字符串
     * @return 加密后的字符串
     */
    public static byte[] Encryption(byte[] key, String string) {
        byte[] encryptedData = null;

        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用他把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey sk = skf.generateSecret(dks);
            // Cipher对象实际完成加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, sk, sr);
            // 获取数据并加密
            byte[] data = string.getBytes(ENCODINGTYPE);
            // 正式执行加密操作
            encryptedData = cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "InvalidKeyException:" + e.getMessage(), true);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "NoSuchAlgorithmException:" + e.getMessage(), true);
        } catch (InvalidKeySpecException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "InvalidKeySpecException:" + e.getMessage(), true);
        } catch (NoSuchPaddingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "NoSuchPaddingException:" + e.getMessage(), true);
        } catch (IllegalBlockSizeException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "IllegalBlockSizeException:" + e.getMessage(), true);
        } catch (BadPaddingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "BadPaddingException:" + e.getMessage(), true);
        } catch (UnsupportedEncodingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "UnsupportedEncodingException:" + e.getMessage(), true);
        }

        return encryptedData;
    }

    /**
     * 解密
     *
     * @param key         密钥
     * @param encryptData 加过密的数据
     * @return 返回解密后的字符串
     */
    public static String Decryption(byte[] key, byte[] encryptData) {
        String result = "";

        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用他把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey sk = skf.generateSecret(dks);
            // Cipher对象实际完成加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, sk, sr);
            // 正式执行解密操作
            byte[] decryptedData = cipher.doFinal(encryptData);

            result = new String(decryptedData);
        } catch (InvalidKeyException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "InvalidKeyException:" + e.getMessage(), true);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "NoSuchAlgorithmException:" + e.getMessage(), true);
        } catch (InvalidKeySpecException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "InvalidKeySpecException:" + e.getMessage(), true);
        } catch (NoSuchPaddingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "NoSuchPaddingException:" + e.getMessage(), true);
        } catch (IllegalBlockSizeException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "IllegalBlockSizeException:" + e.getMessage(), true);
        } catch (BadPaddingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "BadPaddingException:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 根据给定的字符串产生密钥
     *
     * @param key 密钥字符串
     * @return 返回密钥
     */
    public static byte[] getSecretKey(String key) {
        byte[] secretKey = null;

        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 为DES算法生成一个KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
            kg.init(sr);
            // 生成密钥
            SecretKey sk = kg.generateKey();
            // 获取密钥数据
            secretKey = sk.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "NoSuchPaddingException:" + e.getMessage(), true);
        }

        return secretKey;
    }
}
