package com.yingtao.ndklib;

import android.text.TextUtils;
import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

    /**
     * 进行MD5加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */

    public static String encryptToMD5(String info) {
        byte[] digesta = null;
        String rs = null;
        try {
            // 得到一个md5的消息摘要
            MessageDigest alga = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes("UTF-8"));
            // 得到该摘要
            digesta = alga.digest();
            rs = byte2hex(digesta);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 将摘要转为BASE64编码字符串
        return rs;
    }


    //解密数据
    public static String decrypt(String key, String message) throws Exception {

        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return java.net.URLDecoder.decode(new String(retByte), "UTF-8");

    }

    public static String encrypt(String message, String key)
            throws Exception {
        message = java.net.URLEncoder.encode(message, "utf-8").toLowerCase();
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return toHexString(cipher.doFinal(message.getBytes("UTF-8"))).toLowerCase();
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }


    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    /**
     * 进行SHA加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        String rs = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
            rs = byte2hex(digesta);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串
        return rs;
    }

    // //////////////////////////////////////////////////////////////////////////

    /**
     * 创建密匙
     *
     * @param algorithm 加密算法,可用 DES,DESede,Blowfish
     * @return SecretKey 秘密（对称）密钥
     * @throws InvalidKeySpecException
     */
    public static SecretKey createSecretKey(String algorithm) throws InvalidKeySpecException {
        // 声明KeyGenerator对象
        KeyGenerator keygen;
        // 声明 密钥对象
        SecretKey deskey = null;
        try {
            // 返回生成指定算法的秘密密钥的 KeyGenerator 对象
            keygen = KeyGenerator.getInstance(algorithm);
            // 生成一个密钥
            deskey = keygen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 返回密匙
        return deskey;
    }

    /**
     * 根据密匙进行DES加密
     * <p/>
     * 密匙
     *
     * @param info 要加密的信息
     * @return String 加密后的信息
     */
    public static String encryptToDES(String secretKey, String info) {
        // 定义 加密算法,可用 DES,DESede,Blowfish
        String Algorithm = "DES";
        // 加密随机数生成器 (RNG),(可以不写)
        SecureRandom sr = new SecureRandom();
        // 定义要生成的密文
        byte[] cipherByte = null;
        if (secretKey.length() != secretKey.getBytes().length || secretKey.length() != 8) {
            throw new IllegalArgumentException("密钥得由8个单字节字符(英文字母,数字,标点符号等)组成.");
        }

        try {
            // 得到加密/解密器
            DESKeySpec key = new DESKeySpec(secretKey.getBytes());
            SecretKey skey = SecretKeyFactory.getInstance("DES")
                    .generateSecret(key);
            Cipher c1 = Cipher.getInstance(Algorithm);
            // 用指定的密钥和模式初始化Cipher对象
            // 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
            c1.init(Cipher.ENCRYPT_MODE, skey, sr);
            // 对要加密的内容进行编码处理,
            cipherByte = c1.doFinal(info.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回密文的十六进制形式
        //return byte2hex(cipherByte);
        //返回BASE64编码
        return byte2BASE64(cipherByte);
    }

    /**
     * 根据密匙进行DES解密
     *
     * @param key
     *            密匙
     * @param sInfo
     *            要解密的密文
     * @return String 返回解密后信息
     */
//	public static String decryptByDES(String secretKey, String sInfo) {
//		// 定义 加密算法,
//		String Algorithm = "DES";
//		// 加密随机数生成器 (RNG)
//		SecureRandom sr = new SecureRandom();
//		byte[] cipherByte = null;
//		if(secretKey.length() != secretKey.getBytes().length || secretKey.length()!=8){
//			throw new IllegalArgumentException("密钥得由8个单字节字符(英文字母,数字,标点符号等)组成.");
//		}
//		try {
//			// 得到加密/解密器
//			DESKeySpec key = new DESKeySpec(secretKey.getBytes());
//			SecretKey skey = SecretKeyFactory.getInstance("DES")
//					.generateSecret(key);
//			Cipher c1 = Cipher.getInstance(Algorithm);
//			// 用指定的密钥和模式初始化Cipher对象
//			c1.init(Cipher.DECRYPT_MODE, skey, sr);
//			// 对要解密的内容进行编码处理
//			cipherByte = c1.doFinal(base64ToByte(sInfo));
//			return new String(cipherByte);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}


    // /////////////////////////////////////////////////////////////////////////////

    /**
     * 创建密匙组，并将公匙，私匙放入到指定文件中
     * <p/>
     * 默认放入mykeys.bat文件中
     */
    public static void createPairKey() {
        try {
            // 根据特定的算法一个密钥对生成器
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");
            // 加密随机数生成器 (RNG)
            SecureRandom random = new SecureRandom();
            // 重新设置此随机对象的种子
            random.setSeed(1000);
            // 使用给定的随机源（和默认的参数集合）初始化确定密钥大小的密钥对生成器
            keygen.initialize(512, random);// keygen.initialize(512);
            // 生成密钥组
            KeyPair keys = keygen.generateKeyPair();
            // 得到公匙
            PublicKey pubkey = keys.getPublic();
            // 得到私匙
            PrivateKey prikey = keys.getPrivate();
            // 将公匙私匙写入到文件当中
            doObjToFile("mykeys.bat", new Object[]{prikey, pubkey});
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用私匙对信息进行签名 把签名后的信息放入到指定的文件中
     *
     * @param info     要签名的信息
     * @param signfile 存入的文件
     */
    public static void signToInfo(String info, String signfile) {
        // 从文件当中读取私匙
        PrivateKey myprikey = (PrivateKey) getObjFromFile("mykeys.bat", 1);
        // 从文件中读取公匙
        PublicKey mypubkey = (PublicKey) getObjFromFile("mykeys.bat", 2);
        try {
            // Signature 对象可用来生成和验证数字签名
            Signature signet = Signature.getInstance("DSA");
            // 初始化签署签名的私钥
            signet.initSign(myprikey);
            // 更新要由字节签名或验证的数据
            signet.update(info.getBytes());
            // 签署或验证所有更新字节的签名，返回签名
            byte[] signed = signet.sign();
            // 将数字签名,公匙,信息放入文件中
            doObjToFile(signfile, new Object[]{signed, mypubkey, info});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数字签名文件 根据公匙，签名，信息验证信息的合法性
     *
     * @return true 验证成功 false 验证失败
     */
    public static boolean validateSign(String signfile) {
        // 读取公匙
        PublicKey mypubkey = (PublicKey) getObjFromFile(signfile, 2);
        // 读取签名
        byte[] signed = (byte[]) getObjFromFile(signfile, 1);
        // 读取信息
        String info = (String) getObjFromFile(signfile, 3);
        try {
            // 初始一个Signature对象,并用公钥和签名进行验证
            Signature signetcheck = Signature.getInstance("DSA");
            // 初始化验证签名的公钥
            signetcheck.initVerify(mypubkey);
            // 使用指定的 byte 数组更新要签名或验证的数据
            signetcheck.update(info.getBytes());
            System.out.println(info);
            // 验证传入的签名
            return signetcheck.verify(signed);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将二进制转化为16进制字符串
     *
     * @param b 二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = null;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0");
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 十六进制字符串转化为2进制
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 二进制转BASE64编码字符串
     *
     * @param b
     * @return
     */
    public static String byte2BASE64(byte[] b) {
        String rs = "";
        try {
            rs = BASE64Encoder.class.newInstance().encode(b);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定的对象写入指定的文件
     *
     * @param file 指定写入的文件
     * @param objs 要写入的对象
     */
    public static void doObjToFile(String file, Object[] objs) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            for (int i = 0; i < objs.length; i++) {
                oos.writeObject(objs[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回在文件中指定位置的对象
     *
     * @param file 指定的文件
     * @param i    从1开始
     * @return
     */
    public static Object getObjFromFile(String file, int i) {
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            for (int j = 0; j < i; j++) {
                obj = ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 封装秘钥
     *
     * @return 秘钥对象
     * @throws Exception 异常
     */
    private static Key toKey(byte[] key, String desType) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(desType);
        return keyFactory.generateSecret(dks);
    }

    /**
     * DES解密，NDK中调用,方法名与参数列表不得更改
     *
     * @param data 密文
     * @return 明文
     * @throws Exception 异常
     */
    public static String decrypt(byte[] data, byte[] key, byte[] py, String desType, String desMode) throws Exception {
        Key k = toKey(key, desType);
        Cipher cipher = Cipher.getInstance(desMode);
        IvParameterSpec iv = new IvParameterSpec(py);
        cipher.init(Cipher.DECRYPT_MODE, k, iv);//解密模式
        return Base64.encodeToString(cipher.doFinal(data), Base64.NO_WRAP);
    }

    /**
     * DES加密，NDK中调用,方法名与参数列表不得更改
     *
     * @param data 明文
     * @return 密文
     * @throws Exception 异常
     */
    public static String encrypt(byte[] data, byte[] key, byte[] py, String desType, String desMode) throws Exception {
        Key k = toKey(key, desType);
        Cipher cipher = Cipher.getInstance(desMode);
        IvParameterSpec iv = new IvParameterSpec(py);
        cipher.init(Cipher.ENCRYPT_MODE, k, iv);//加密模式
        return Base64.encodeToString(cipher.doFinal(data), Base64.NO_WRAP);
    }

    /**
     * DES解密
     *
     * @param decryptString 密文
     * @param ivStr         加密key
     * @param keyStr        加密偏移量
     * @return 明文
     * @throws Exception exception
     */
    public static String decrypt(String decryptString, String keyStr, String ivStr) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        if (!TextUtils.isEmpty(ivStr)) {
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return new String(cipher.doFinal(Base64.decode(decryptString, Base64.NO_WRAP)));
    }

    /**
     * DES加密
     *
     * @param encryptString 明文
     * @param keyStr        解密key
     * @param ivStr         偏移量
     * @return 密文
     * @throws Exception exception
     */
    public static String encrypt(String encryptString, String keyStr, String ivStr)
            throws Exception {
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        if (!TextUtils.isEmpty(ivStr)) {
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        return Base64.encodeToString(cipher.doFinal(encryptString.getBytes()), Base64.NO_WRAP);
    }

    //-----------------------------AES-----------------------------------

    /**
     * 加密
     */
    static String encryptAes(String sSrc, String key, String iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
        IvParameterSpec _iv = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, _iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

    /**
     * 解密
     */
    static String decryptAes(String sSrc, String key, String iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec _iv = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, _iv);
        byte[] encrypted = Base64.decode(sSrc, Base64.NO_WRAP);// 先用base64解码
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, "utf-8");
    }

}
