package com.ncs.commons.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

public final class RSAUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RSAUtil.class);


    public static final String KEYSTORE_TYPE_P12 = "PKCS12";
    public static final String KEYSTORE_TYPE_JKS = "JKS";
    public static final String ENCRYPT_TYPE_RSA = "RSA";
    public static final String DEFAILT_ALGORITHM = "MD5withRSA";
    public static final String CERT_DEFAULTPATH = "";
    public static final String CERT_TYPE_X509 = "X.509";

    public static KeyStore getKeyStore(String filePath, String keyPassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(guessKeyStoreType(filePath));
        FileInputStream file = new FileInputStream(new File(filePath));
        keyStore.load(file, keyPassword.toCharArray());
        return keyStore;
    }

    public static KeyStore getKeyStore(InputStream inputStream, String keyPassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE_P12);
        keyStore.load(inputStream, keyPassword.toCharArray());
        return keyStore;
    }

    public static String guessKeyStoreType(String filePath) {

        String ext = FilenameUtils.getExtension(filePath).toLowerCase();
        if (ext.equals("p12") || ext.equals("pfx")) {
            return KEYSTORE_TYPE_P12;
        }
        if (ext.equals("jks")) {
            return KEYSTORE_TYPE_JKS;
        }
        return null;
    }

    public static RSAPublicKey getPublicKey(KeyStore keyStore) throws Exception {
        String key_aliases = null;
        Enumeration<String> enumeration = keyStore.aliases();
        key_aliases = enumeration.nextElement();
        if (keyStore.isKeyEntry(key_aliases)) {
            RSAPublicKey publicKey = (RSAPublicKey) keyStore.getCertificate(key_aliases).getPublicKey();
            return publicKey;
        }
        return null;
    }


    public static RSAPrivateKey getPrivateKey(KeyStore keyStore, String keyPassword) throws Exception {
        String key_aliases = null;
        Enumeration<String> enumeration = keyStore.aliases();
        key_aliases = enumeration.nextElement();
        if (keyStore.isKeyEntry(key_aliases)) {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(key_aliases, keyPassword.toCharArray());
            return privateKey;
        }
        return null;
    }


    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        if (null == publicKey) {// 如果公钥为空，采用系统公钥
            throw new Exception("rsa publicKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11,如果明文长度大于模长-11则要分组加密
        key_len -= 11;
        byte[] datas = data.getBytes();
        byte[] dataReturn = null;
        for (int i = 0; i < datas.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(datas, i, i + key_len));
            dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
        }
        return Base64.encodeBase64String(dataReturn);

    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {// 如果私钥为空采用系统私钥

            throw new Exception("rsa privateKey is null");
        }
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        // base64解密
        byte[] bytes = Base64.decodeBase64(data);
        // 分组解密
        StringBuffer sb = new StringBuffer();
        // 如果密文长度大于模长则要分组解密
        for (int i = 0; i < bytes.length; i += key_len) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(bytes, i, i + key_len));
            sb.append(new String(doFinal));
        }
        return sb.toString();
    }

    public static X509Certificate getPublicKeyCer(InputStream publicInput) throws Exception {
        CertificateFactory certificatefactory = CertificateFactory.getInstance(CERT_TYPE_X509);
        X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(publicInput);
        return cert;
    }

    /**
     * 签名
     *
     * @param data       签名内容
     * @param algorithm  签名
     * @param privateKey 签名私钥
     * @return
     * @throws Exception
     */
    public static String signData(byte[] data, String algorithm, RSAPrivateKey privateKey) throws Exception {
        if (null == privateKey) {// 如果私钥为空采用系统私钥
            throw new Exception("rsa privateKey is null");
        }
        if (null == algorithm || algorithm.length() == 0) {
            algorithm = DEFAILT_ALGORITHM;
        }
        Signature signture = Signature.getInstance(algorithm);
        signture.initSign(privateKey);
        signture.update(data);
        return Base64.encodeBase64String(signture.sign());
    }

    /**
     * @param content   签名原文内容
     * @param signvalue 签名值
     * @param publicKey 验签公钥
     * @return
     */
    public static boolean vefySign(String content, String signvalue, PublicKey publicKey) {
        try {
            if (null == signvalue || null == content)
                return false;
            Signature signature = null;
            if (null == publicKey) {
                throw new Exception("rsa publicKey  is null");
            }
            signature = Signature.getInstance(DEFAILT_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(Base64.decodeBase64(signvalue));
            return bverify;
        } catch (Exception e) {
            LOG.error("验签异常{}", e);
            return false;
        }
    }

    /**
     * @param content   签名原文内容
     * @param signvalue 签名值
     * @param cert      验签证书
     * @return
     */
    public static boolean vefySign(String content, String signvalue, X509Certificate cert) {
        try {
            if (null == signvalue || null == content)
                return false;
            Signature signature = null;
            if (null == cert) {
                throw new Exception("rsa X509Certificate  is null");
            }
            signature = Signature.getInstance(cert.getSigAlgName());
            signature.initVerify(cert.getPublicKey());
            signature.update(content.getBytes());
            boolean bverify = signature.verify(Base64.decodeBase64(signvalue));
            return bverify;
        } catch (Exception e) {
            LOG.error("验签异常{}", e);
            return false;
        }
    }

    public static PublicKey readPublicKey(File fileInputStream) {
        try {
            return RSAUtil.getPublicKeyCer(new FileInputStream(fileInputStream)).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("读取公钥失败", e);
        }

    }

    /**
     * 读取pem格式公钥
     *
     * @param certPemString pem字符串
     * @return
     */
    public static PublicKey readPublicKeyPEM(String certPemString) {
        try {
            return RSAUtil.getPublicKeyCer(new ByteArrayInputStream(certPemString.getBytes("UTF-8"))).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("读取公钥失败", e);
        }

    }


}
