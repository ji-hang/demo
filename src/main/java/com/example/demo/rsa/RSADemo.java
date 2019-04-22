package com.example.demo.rsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.tomcat.util.codec.binary.Base64;


/**
 * @author jh.
 *
 */
public class RSADemo {

	public static final String CHARSETNAME = "UTF-8";
	public static final String ALGORITHM = "RSA";
	
    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    private static final int KEY_SIZE = 1024;
    
    
    /**
     * 数字签名方式
     */
    public static final String SIGN_ALGORITHM = "SHA1withRSA";
	
	/**
	 * 生成公钥私钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, String> createKeys() throws NoSuchAlgorithmException{
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
		//初始化密钥对的长度
		kpg.initialize(KEY_SIZE);
		//生成密钥对
		KeyPair keyPair = kpg.generateKeyPair();
		//得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = base64Encoded(publicKey.getEncoded());
		System.out.println("得到公钥：\n"+publicKeyStr);
		
		//得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = base64Encoded(privateKey.getEncoded());
		System.out.println("得到私钥：\n"+privateKeyStr);
		
		Map<String, String> key = new HashMap<String, String>();
		key.put("pubKey", publicKeyStr);
		key.put("priKey", privateKeyStr);
		return key;
	}
	
	/**
	 * 通过公钥加密
	 * @param data
	 * @param pubKey
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String encryptByPub(String data, String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// pubKey是经过base64编码的，先解码
		byte[] pubByte = base64Decoded(pubKey);
		// 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
		X509EncodedKeySpec xeks = new X509EncodedKeySpec(pubByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		Key publicKey = factory.generatePublic(xeks);
		
		//加密
		Cipher cipher = Cipher.getInstance(ALGORITHM);	
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptByte = cipher.doFinal(data.getBytes(CHARSETNAME));
		// 返回加密后由Base64编码的加密信息
		String encryptStr = base64Encoded(encryptByte);
		System.out.println("公钥加密后：\n" + encryptStr);
		return encryptStr;
	}
	
	/**
	 * 私钥解密
	 * @param encryptData
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 */
	private static String decryptByPri(String encryptData, String priKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		//私钥有经过base64编码，先解码
		byte[] priByte = base64Decoded(priKeyStr);
		//还原私钥
		PKCS8EncodedKeySpec pks = new PKCS8EncodedKeySpec(priByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		Key priKey = factory.generatePrivate(pks);
		//对数据解密
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		//加密的数据有经过base64编码，所以先解码
		byte[] encryptByte = base64Decoded(encryptData);
		//返回UTF-8编码的解密信息
		byte[] decryptByte = cipher.doFinal(encryptByte);
		String decryptData = new String(decryptByte, CHARSETNAME);
		System.out.println("私钥解密后：\n" + decryptData);
		return decryptData;
	}
	
	/**
	 * 私钥加密
	 * @param data
	 * @param priKeyStr
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	private static String encryptByPri(String data, String priKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		//私钥有经过base64编码，先解码
		byte[] priByte = base64Decoded(priKeyStr);
		//还原私钥
		PKCS8EncodedKeySpec pks = new PKCS8EncodedKeySpec(priByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		Key priKey = factory.generatePrivate(pks);
		
		// 用私钥对数据加密 
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        byte[] encryptByte = cipher.doFinal(data.getBytes(CHARSETNAME));
        // 返回加密后由Base64编码的加密信息
		String encryptStr = base64Encoded(encryptByte);
		System.out.println("私钥加密后：\n" + encryptStr);
		return encryptStr;
	}
	
	/**
	 * 公钥解密
	 * @param encryptByPriStr
	 * @param string
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 */
	private static String decryptByPub(String encryptByPriStr, String pubKeyStr) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
		//公钥有经过base64编码，先解码
		byte[] pubByte = base64Decoded(pubKeyStr);
		//还原公钥
		X509EncodedKeySpec xeks = new X509EncodedKeySpec(pubByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		Key publicKey = factory.generatePublic(xeks);
		
		//加密的数据有经过base64编码，所以先解码
		byte[] encryptByte = base64Decoded(encryptByPriStr);
		// 用公钥对数据解密 
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptByte = cipher.doFinal(encryptByte);
        
        String decryptData = new String(decryptByte, CHARSETNAME);
		System.out.println("公钥解密后：\n" + decryptData);
		return decryptData;
	}
	
	
	/**
	 * 私钥签名
	 * @param param  需要签名的内容
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 * @throws UnsupportedEncodingException 
	 * @throws SignatureException 
	 */
	private static String sign(String param, String priKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		//私钥有经过base64编码，先解码
		byte[] priByte = base64Decoded(priKeyStr);
		//还原私钥
		PKCS8EncodedKeySpec pks = new PKCS8EncodedKeySpec(priByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey priKey = factory.generatePrivate(pks);
		//SHA1withRSA算法进行签名 
		Signature sign = Signature.getInstance(SIGN_ALGORITHM);
		sign.initSign(priKey);
		sign.update(param.getBytes(CHARSETNAME));
		byte[] signByte = sign.sign();
		//对签名进行base64编码
		String signStr = base64Encoded(signByte);
		System.out.println("私钥签名后：\n" + signStr);
		return signStr;
	}
	
	/**
	 * 公钥验签
	 * @param param     用于验签的数据
	 * @param signStr   私钥生成的签名串
	 * @param pubKeyStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static boolean verifySign(String param, String signStr, String pubKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		//公钥有经过base64编码，先解码
		byte[] pubByte = base64Decoded(pubKeyStr);
		//还原公钥
		X509EncodedKeySpec xeks = new X509EncodedKeySpec(pubByte);
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		PublicKey publicKey = factory.generatePublic(xeks);
		
		//签名串有被base64编码，先解码
		byte[] signByte = base64Decoded(signStr);
		
		//SHA1withRSA算法进行验签
		Signature verifySign = Signature.getInstance(SIGN_ALGORITHM);
		verifySign.initVerify(publicKey); 
		//用于验签的数据  
        verifySign.update(param.getBytes(CHARSETNAME));
        boolean flag = verifySign.verify(signByte); 
        System.out.println("公钥验签结果：\n" + flag);
		return flag;
	}
	
	/**
	 * base64编码
	 * @param param
	 * @return
	 */
	public static String base64Encoded(byte[] param) {
		return Base64.encodeBase64String(param);
	}
	
	/**
	 * base64解码
	 * @param param
	 * @return
	 */
	public static byte[] base64Decoded(String param) {
		return Base64.decodeBase64(param);
	}
	
	public static void main(String[] args) throws Exception {
		
		//获取公钥密钥
		Map<String, String> key = RSADemo.createKeys();
		//待加密数据
		String data = "天街小雨润如酥，草色遥看近却无。最是一年春好处，绝胜烟柳满皇都。";
		
		System.err.println("--------------------公钥加密、私钥解密-----------------------");
		
		//公钥加密
		String encryptData = encryptByPub(data, key.get("pubKey"));
		//私钥解密
		decryptByPri(encryptData, key.get("priKey"));
		
		System.err.println("--------------------私钥加密、公钥解密-----------------------");
		
		//私钥加密
		String encryptByPriStr = encryptByPri(data, key.get("priKey"));
		//公钥解密
		decryptByPub(encryptByPriStr, key.get("pubKey"));
		
		System.err.println("--------------------私钥签名-----------------------");
		
		String param = "name=jh&age=24&use=测试私钥签名";
		// 私钥签名
		String signStr = sign(param, key.get("priKey"));
		//公钥验签
		verifySign(param, signStr, key.get("pubKey"));
		
	}

}
