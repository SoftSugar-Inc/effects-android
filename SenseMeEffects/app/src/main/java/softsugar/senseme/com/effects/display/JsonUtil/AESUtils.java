package softsugar.senseme.com.effects.display.JsonUtil;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String TAG = AESUtils.class.getSimpleName();

    /**
     * 采用AES加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密
     *
     * @param secretKey 加密密码，长度：16 或 32 个字符
     * @param data      待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String secretKey, byte[] data) {
        try {
            // 创建AES秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
            // 创建密码器
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptByte = cipher.doFinal(data);
            // 将加密以后的数据进行 Base64 编码
            return base64Encode(encryptByte);
        } catch (Exception e) {
            handleException("encrypt", e);
        }
        return null;
    }

    public static String encrypt(byte[] encryptStr, String key, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(encryptStr);
            return base64Encode(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * AES 解密
     *
     * @param secretKey  解密的密钥，长度：16 或 32 个字符
     * @param base64Data 加密的密文 Base64 字符串
     */
    public static byte[] decrypt(String secretKey, String base64Data) {
        try {
            byte[] data = base64Decode(base64Data);
            // 创建AES秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
            // 创建密码器
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 执行解密操作
            byte[] result = cipher.doFinal(data);
            return result;
            //return new String(result, CHARSET_UTF8);
        } catch (Exception e) {
            handleException("decrypt", e);
        }
        return null;
    }

    /**
     * 将 字节数组 转换成 Base64 编码
     * 用Base64.DEFAULT模式会导致加密的text下面多一行（在应用中显示是这样）
     */
    public static String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * 将 Base64 字符串 解码成 字节数组
     */
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    /**
     * 处理异常
     */
    private static void handleException(String methodName, Exception e) {
        e.printStackTrace();
        Log.e(TAG, methodName + "---->" + e);
    }
}


