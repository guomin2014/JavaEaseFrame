package com.gm.javaeaseframe.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MD5加密工具(百度提供)
 * @author apple
 *
 */
public class MD5Util
{
    private static Log log = LogFactory.getLog(MD5Util.class);

	/**
	 * 获取32位MD5加密
	 * @param str
	 * @param charSet
	 * @return
	 */
	public static String strToMd5(String str, String charSet)
	{
		String md5Str = null;
		if (str != null && str.length() != 0)
		{
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(str.getBytes(charSet));
				byte b[] = md.digest();
				int i;
				StringBuffer buf = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++)
				{
					i = b[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						buf.append("0");
					buf.append(Integer.toHexString(i));
				}
				md5Str = buf.toString();
			}
			catch (NoSuchAlgorithmException e)
			{
				log.error("MD5 加密发生异常。加密串：" + str);
			}
			catch (UnsupportedEncodingException e2)
			{
				log.error("MD5 加密发生异常。加密串：" + str);
			}
		}
		return md5Str;
	}
	/**
	 * 获取16位MD5加密
	 * @param str
	 * @return
	 */
	public static String strToMd5(String str)
	{
		 String temp = strToMd5(str, "UTF-8");
	        if (temp != null) 
	        {
	            return temp.substring(8, 24);// 16位的加密
	        }
	        return temp;
	}

	/**
	 * 按照字典序逆序拼接参数
	 * 
	 * @param params
	 * @return
	 */
	private static String getSign(String... params)
	{
		List<String> srcList = new ArrayList<String>();
		for (String param : params)
		{
			srcList.add(param);
		}
		// 按照字典序逆序拼接参数
//		Arrays.sort(params);
//		Collections.sort(srcList, String.CASE_INSENSITIVE_ORDER);
//		Collections.reverse(srcList);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < srcList.size(); i++)
		{
			sb.append(srcList.get(i));
		}
		return sb.toString();
	}
	private static String getSignWithSort(String... params)
	{
		List<String> srcList = new ArrayList<String>();
		for (String param : params)
		{
			srcList.add(param);
		}
		// 按照字典序逆序拼接参数
		Arrays.sort(params);
		Collections.sort(srcList, String.CASE_INSENSITIVE_ORDER);
		Collections.reverse(srcList);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < srcList.size(); i++)
		{
			sb.append(srcList.get(i));
		}
		return sb.toString();
	}

	/**
	 * MD5 加密调用
	 * @param params
	 * @return
	 */
	public static String getSignAndMD5(String... params)
	{
		String sign = getSign(params);
		return strToMd5(sign, "utf-8");
	}
	public static String getSignAndMD5WithSort(String... params)
	{
		String sign = getSignWithSort(params);
		return strToMd5(sign, "utf-8");
	}
	
	public static void main(String[] args)
	{
		String md5 = getSignAndMD5("100000", "7c4f2461dad2dea54c9a34b7967e0c7717d1d2f0aa71ef9bc69f4911d7b43685c123e861763717a5348dbe596dbd5ab75f82d1cfb302207f51980ccfb2d841f948719f302e9a84b0b9dd04c5c522958e3fd2553f4acb2d99a911eb9822f0148438cd9d1158a537479c8b6e8bf697f697ef009d36e41a05e9eaefe4285e83ca5252bb3439f5d769bc0b0cdf69c68f8797fb9e77d3a00d3b507b299aa62b21628cfa13e87c699d9aa5e82e7fa0b9efb83a0778d8730fdeefe4c545c5c08d56c6957f2b604c55f4b58931548486fae447a5d8fd994870fc224d424d6e8f2671f1c75c6c68098873702521fb968f49395dfc","1438415515517");
		System.out.println(md5);	
		System.out.println("a75717da5420447a47c16a2fe1d622ba");	
		md5 = strToMd5("100000"+"7c4f2461dad2dea54c9a34b7967e0c7717d1d2f0aa71ef9bc69f4911d7b43685c123e861763717a5348dbe596dbd5ab75f82d1cfb302207f51980ccfb2d841f948719f302e9a84b0b9dd04c5c522958e3fd2553f4acb2d99a911eb9822f0148438cd9d1158a537479c8b6e8bf697f697ef009d36e41a05e9eaefe4285e83ca5252bb3439f5d769bc0b0cdf69c68f8797fb9e77d3a00d3b507b299aa62b21628cfa13e87c699d9aa5e82e7fa0b9efb83a0778d8730fdeefe4c545c5c08d56c6957f2b604c55f4b58931548486fae447a5d8fd994870fc224d424d6e8f2671f1c75c6c68098873702521fb968f49395dfc"+"1438415515517","utf-8");
		System.out.println(md5);
	}
}
