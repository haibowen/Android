package com.telink.bluetooth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataHelper {

	public static final String PATTERN_DATATIME_MS = "yyyy-MM-dd HH:mm:ss,SSS";
	public static final String ENCODE = "UTF-8";

	public static String getASCLLString(byte[] source, int start, int length) {
		StringBuilder sb = new StringBuilder();

		for (int i = start; i < length; i++) {
			sb.append((char) source[i]);
		}

		return sb.toString();

	}

	public static int byteArrayToInt(byte[] paramArrayOfByte)
	{
		int i = 0;
		for (int j = 0; j < 4; j++)
		{
			int k = 8 * (3 - j);
			i += ((0xFF & paramArrayOfByte[j]) << k);
		}
		return i;
	}

	/**
	 * 将一个单字节的byte转换成32位的int
	 * 
	 * @param b
	 *            byte
	 * @return convert result
	 */
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/**
	 * 从byte数组中获取ip、dns这些类似的地址信息
	 * 
	 * @param source
	 * @param start
	 * @return
	 */
	public static byte[] getByteNetInfo(String netinfo) {
		String source[] = netinfo.split(".");
		byte[] result = new byte[4];
		for (int i = 0; i < result.length; i++) {
			result[i] = Byte.parseByte(source[i]);
		}
		return result;
	}

	/**
	 * 从byte数组中获取ip、dns这些类似的地址信息
	 * 
	 * @param source
	 * @param start
	 * @return
	 */
	public static String getStrNetInfo(byte[] source, int start) {
		if (source.length >= 4) {
			StringBuilder sb = new StringBuilder();
			sb.append(source[start + 3] & 0xFF).append(".");
			sb.append(source[start + 2] & 0xFF).append(".");
			sb.append(source[start + 1] & 0xFF).append(".");
			sb.append(source[start + 0] & 0xFF);
			return sb.toString();
		}
		return "传进来的byte数组长度小于4";
	}

	/**
	 * 将一个单字节的Byte转换成十六进制的数
	 * 
	 * @param b
	 *            byte
	 * @return convert result
	 */
	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	/**
	 * 将一个2byte的数组转换成32位的int
	 * 
	 * @param buf
	 * @return convert result
	 */
	public static int unsigned2BytesToInt(byte[] buf, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) buf[index]));
		secondByte = (0x000000FF & ((int) buf[index + 1]));
		long l = isBigendian() ? ((long) (secondByte << 8 | firstByte)) & 0xFFFFFFFFL
				: ((long) (firstByte << 24 | secondByte << 16)) & 0xFFFFFFFFL;
		return (int) l;
	}

	/**
	 * 将一个4byte的数组转换成32位的int
	 * 
	 * @param buf
	 *            bytes buffer
	 * @param pos
	 *            中开始转换的位置
	 * @return convert result
	 */
	public static int unsigned4BytesToInt(byte[] buf, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) buf[index]));
		secondByte = (0x000000FF & ((int) buf[index + 1]));
		thirdByte = (0x000000FF & ((int) buf[index + 2]));
		fourthByte = (0x000000FF & ((int) buf[index + 3]));
		index = index + 4;
		long l = isBigendian() ? ((long) (fourthByte << 24 | thirdByte << 16
				| secondByte << 8 | firstByte)) & 0xFFFFFFFFL
				: ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
		return (int) l;
	}

	/**
	 * 把整数转byte数组，并返回大端的数组
	 * 
	 * @param value
	 * @param length
	 *            该值只能为1，2，4中的一个
	 * @return
	 */
	public static byte[] little_intToByte(int value, int length) {
		byte[] arrayOfByte = new byte[length];
		if (length == 1)
			arrayOfByte[0] = (byte) (value & 0xFF);
		else if (length == 2) {
			arrayOfByte[0] = (byte) (value & 0xFF);
			arrayOfByte[1] = (byte) ((0xFF00 & value) >> 8);
		} else {
			arrayOfByte[0] = (byte) (value & 0xFF);
			arrayOfByte[1] = (byte) ((0xFF00 & value) >> 8);
			arrayOfByte[2] = (byte) ((0xFF0000 & value) >> 16);
			arrayOfByte[3] = (byte) ((0xFF000000 & value) >> 24);
		}
		return arrayOfByte;
	}

	/**
	 * 把IP地址转化为低端字节数组
	 * 
	 * @param ipAddr
	 * @return byte[] 低端字节
	 */
	public static byte[] little_ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			if (isBigendian()) {
				ret[3] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
				ret[2] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
				ret[1] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
				ret[0] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			} else {
				ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
				ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
				ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
				ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			}
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * 把小端字节int 转成 ip地址
	 * 
	 * @param i
	 * @return
	 */
	public static String little_intToIp(int i) {
		return isBigendian() ? ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF) : (i & 0xFF) + "."
				+ ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ ((i >> 24) & 0xFF);
	}

	/**
	 * 将16位的short转换成byte数组
	 * 
	 * @param s
	 *            short
	 * @return byte[] 长度为2
	 * */
	public static byte[] shortToByteArray(short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return isBigendian() ? reorderBytes(targets) : targets;
	}

	/**
	 * 地位高位互转的方法
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] reorderBytes(byte[] data1) {
		byte[] data = data1.clone();
		int len = data.length;
		int lenD = len / 2;
		for (int i = 0; i < lenD; i++) {
			byte temp = data[i];
			data[i] = data[len - 1 - i];
			data[len - 1 - i] = temp;
		}
		return data;
	}

	private static boolean isBigendian() {
		short i = 0x1;
		return ((i >> 8) == 0x0);
	}

	/**
	 * 将32位整数转换成长度为4的byte数组
	 * 
	 * @param s
	 *            int
	 * @return byte[]
	 * */
	public static byte[] intToByteArray(int s) {
		byte[] targets = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return isBigendian() ? reorderBytes(targets) : targets;
	}

	public static byte[] intTo2ByteArray(int s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return isBigendian() ? reorderBytes(targets) : targets;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static short little_byteToShort(byte[] b, int pos) {
		short s = 0;
		short s0 = (short) (b[0 + pos] & 0xff);// 最低位
		short s1 = (short) (b[1 + pos] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * 将长度为2的byte数组转换为16位int
	 * 
	 * @param res
	 *            byte[]
	 * @return int
	 * */
	/*
	 * public static short byte2int(byte[] res, int pos) { // res =
	 * InversionByte(res); // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000 int
	 * targets = (res[pos + 0] & 0xff) | ((res[pos + 1] << 8) & 0xff00); // | //
	 * 表示安位或 return (short) targets; }
	 */

	public static byte bytetoint(byte paramByte) {
		int i = Integer.valueOf(paramByte).intValue();
		if (i < 0)
			i &= 255;
		return (byte) i;
	}

	/**
	 * 低48mac地址 高16 0x01
	 * 
	 * @param paramString
	 * @return
	 */
	public static byte[] getMacBytesToBig(byte[] arrayByte) {
		byte[] arrayOfByte = new byte[arrayByte.length];
		// arrayOfByte[7] = 0x00;
		// arrayOfByte[6] = 0x01;
		int len = arrayByte.length;
		for (int i = 0; i < len; i++) {
			arrayOfByte[i] = arrayByte[len - 1 - i];
		}
		return arrayOfByte;
	}

	/**
	 * 转mac地址为6位数组
	 * 
	 * @param paramString
	 * @return
	 */
	public static byte[] getMac6BytesRemote(String paramString) {
		if (paramString.equals("00:00:00:00:00:00")) {
			byte[] arrayOfByte = new byte[6];
			arrayOfByte[0] = 0x00;
			arrayOfByte[1] = 0x00;
			arrayOfByte[2] = 0x00;
			arrayOfByte[3] = 0x00;
			arrayOfByte[4] = 0x00;
			arrayOfByte[5] = 0x00;
			return arrayOfByte;
		} else {
			byte[] arrayOfByte = new byte[6];
			String[] arrayOfString = paramString.split(":");
			int len = arrayOfString.length;
			for (int i = 0; i < len; i++) {
				arrayOfByte[i] = bytetoint((byte) Integer.parseInt(
						arrayOfString[i], 16));
			}
			return arrayOfByte;
		}

	}

	/**
	 * 低48mac地址 高16 0x01
	 * 
	 * @param paramString
	 * @return
	 */
	public static byte[] getMac8BytesRemote(String paramString) {
		if (paramString == null || paramString.equals("00:00:00:00:00:00")) {
			byte[] arrayOfByte = new byte[8];
			arrayOfByte[0] = 0x00;
			arrayOfByte[1] = 0x00;
			arrayOfByte[2] = 0x00;
			arrayOfByte[3] = 0x00;
			arrayOfByte[4] = 0x00;
			arrayOfByte[5] = 0x00;
			arrayOfByte[6] = 0x00;
			arrayOfByte[7] = 0x00;
			return arrayOfByte;
		} else {
			byte[] arrayOfByte = new byte[8];
			arrayOfByte[0] = 0x00;
			arrayOfByte[1] = 0x01;
			String[] arrayOfString = paramString.split(":");
			int len = arrayOfString.length;
			for (int i = 0; i < len; i++) {
				arrayOfByte[i + 2] = bytetoint((byte) Integer.parseInt(
						arrayOfString[i], 16));
			}
			return arrayOfByte;
		}

	}

	/**
	 * 获取本地头的mac转为byte数组
	 * 
	 * @param paramString
	 * @return
	 */
	public static byte[] getLocalMacBytes(String paramString) {
		if (paramString.equals("00:00:00:00:00:00")) {
			byte[] arrayOfByte = new byte[8];
			arrayOfByte[0] = 0x00;
			arrayOfByte[1] = 0x00;
			arrayOfByte[2] = 0x00;
			arrayOfByte[3] = 0x00;
			arrayOfByte[4] = 0x00;
			arrayOfByte[5] = 0x00;
			arrayOfByte[6] = 0x00;
			arrayOfByte[7] = 0x00;
			return arrayOfByte;
		} else {
			byte[] arrayOfByte = new byte[8];
			arrayOfByte[6] = 0x00;
			arrayOfByte[7] = 0x00;
			String[] arrayOfString = paramString.split(":");
			int len = arrayOfString.length;
			for (int i = 0; i < len; i++) {
				arrayOfByte[i] = bytetoint((byte) Integer.parseInt(
						arrayOfString[i], 16));
			}
			return arrayOfByte;
		}

	}

	/**
	 * 低48mac地址 高16 0x01
	 * 
	 * @param data
	 * @param pos
	 * @return
	 */
	public static String getMacRemote(byte[] data, int pos) {
		StringBuffer sb = new StringBuffer();
		for (int i = 2; i < 8; i++) {
			String hex = byteToHex(data[i + pos]);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex).append(":");
		}
		return sb.substring(0, sb.lastIndexOf(":"));
	}

	/**
	 * 把字符串转为32位byte数组
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] get32ByteString(String source) {
		byte[] result = new byte[32];
		try {
			byte[] temp = source.getBytes(ENCODE);
			DataHelper.pickUpByte(result, temp, 0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getMd5(String origin) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] temp = origin.getBytes();
			if (temp != null && !"".equals(temp)) {
				resultString = byteArrayToHexString(md.digest(temp));
			}

		} catch (Exception ex) {

		}
		return resultString;
	}

	public static String getMd5(byte[] origin) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(origin));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * 对2个32位字符串进行异或操作
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static byte[] MD5(byte[] d1, byte[] d2) {
		for (int i = 0; i < 32; i++) {
			d1[i] = (byte) (d1[i] ^ d2[i]);
		}
		return d1;
	}

	/**
	 * 复制4位数组为32位
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] serial4To32(byte[] source) {
		byte[] result = new byte[32];
		for (int i = 0; i < 8; i++) {
			pickUpByte(result, source, 4 * i);
		}
		return result;

	}

	/**
	 * 16进制字符串转byte数组
	 * 
	 * @param hexStr
	 *            16进制字符串
	 * @return byte数组
	 */
	public static byte[] str16tobytes(String hexStr) {
		String str = "0123456789abcdef";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return bytes;
	}

	/**
	 * 把byte数组转成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 把密码byte数组转为16进组长32的字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String bytePassToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = b.length / 2; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static byte[] pass32tobytes(String md5pass) {
		byte[] result = new byte[32];
		// if (!StringUtils.isNull(md5pass)) {
		// pickUpByte(result, str16tobytes(md5pass), 16);
		// }
		System.err.println("新密码====>" + DataHelper.byte2String(result));
		return result;
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * eg{ DataHelper.getMac(ss, 2, 6) }有byte数组转成mac地址
	 * 
	 * @param headBytes
	 * @param pos
	 * @param len
	 * @return
	 */
	public static String getMac(byte[] headBytes, int pos, int len) {
		StringBuffer sb = new StringBuffer();
		len = len > 6 ? 6 : len;
		for (int i = 0; i < len; i++) {
			String hex = byteToHex(headBytes[i + pos]);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex).append(":");
		}
		return sb.substring(0, sb.lastIndexOf(":"));
	}

	public static String little_getMac(byte[] headBytes, int pos, int len) {
		StringBuffer sb = new StringBuffer();
		len = len > 6 ? 6 : len;
		for (int i = 0; i < len; i++) {
			String hex = byteToHex(headBytes[6 - i + pos]);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex).append(":");
		}
		return sb.substring(0, sb.lastIndexOf(":"));
	}

	/**
	 * 把4位高位byte转为long型
	 * 
	 * @param paramArrayOfByte
	 * @param pos
	 * @return
	 */
	public static long byte4ToLong(byte[] paramArrayOfByte, int pos) {
		paramArrayOfByte = DataHelper.reorderBytes(paramArrayOfByte);
		long l1 = 0xFF & paramArrayOfByte[0 + pos];
		long l2 = 0xFF & paramArrayOfByte[1 + pos];
		long l3 = 0xFF & paramArrayOfByte[2 + pos];
		long l4 = 0xFF & paramArrayOfByte[3 + pos];
		long l5 = 0xFF & 0;
		long l6 = 0xFF & 0;
		long l7 = 0xFF & 0;
		long l8 = 0xFF & 0;
		long l9 = l2 << 8;
		long l10 = l3 << 16;
		long l11 = l4 << 24;
		long l12 = l5 << 32;
		long l13 = l6 << 40;
		long l14 = l7 << 48;
		return l8 << 56 | (l14 | (l13 | (l12 | (l11 | (l10 | (l1 | l9))))));
	}

	public static long byteToSmallLong(byte[] paramArrayOfByte, int pos) {
		long l1 = 0xFF & paramArrayOfByte[0 + pos];
		long l2 = 0xFF & paramArrayOfByte[1 + pos];
		long l3 = 0xFF & paramArrayOfByte[2 + pos];
		long l4 = 0xFF & paramArrayOfByte[3 + pos];
		long l5 = 0xFF & paramArrayOfByte[4 + pos];
		long l6 = 0xFF & paramArrayOfByte[5 + pos];
		long l7 = 0xFF & paramArrayOfByte[6 + pos];
		long l8 = 0xFF & paramArrayOfByte[7 + pos];
		long l9 = l2 << 8;
		long l10 = l3 << 16;
		long l11 = l4 << 24;
		long l12 = l5 << 32;
		long l13 = l6 << 40;
		long l14 = l7 << 48;
		return l8 << 56 | (l14 | (l13 | (l12 | (l11 | (l10 | (l1 | l9))))));
	}

	/**
	 * 传入之后会先进行倒序然后输出long
	 * 
	 * @param paramArrayOfByte
	 * @return int64
	 */
	public static long byteToLong(byte[] paramArrayOfByte, int pos) {
		long l1 = 0xFF & paramArrayOfByte[0 + pos];
		long l2 = 0xFF & paramArrayOfByte[1 + pos];
		long l3 = 0xFF & paramArrayOfByte[2 + pos];
		long l4 = 0xFF & paramArrayOfByte[3 + pos];
		long l5 = 0xFF & paramArrayOfByte[4 + pos];
		long l6 = 0xFF & paramArrayOfByte[5 + pos];
		long l7 = 0xFF & paramArrayOfByte[6 + pos];
		long l8 = 0xFF & paramArrayOfByte[7 + pos];
		long l9 = l2 << 8;
		long l10 = l3 << 16;
		long l11 = l4 << 24;
		long l12 = l5 << 32;
		long l13 = l6 << 40;
		long l14 = l7 << 48;
		return l8 << 56 | (l14 | (l13 | (l12 | (l11 | (l10 | (l1 | l9))))));
	}

	/**
	 * long to byte[]
	 * 
	 * @param s
	 *            long
	 * @return byte[]
	 * */
	public static byte[] longToByteArray(long s) {
		byte[] targets = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 64位Long转换成小端字节序的byte[8]
	 * 
	 * @param l
	 * @return byte[8]
	 */
	public static byte[] longToShortBytes(long l) {
		byte[] data = new byte[8];
		long temp = l;
		for (int i = 0; i < 8; i++) {
			data[i] = (byte) (temp & 0xff);
			temp = temp >> 8;
		}
		return data;
	}

	/**
	 * Date To String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			System.out.println("dateToStr Exception:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 将时间转为高8位的数组
	 * 
	 * @param date
	 * @return
	 */
	public static byte[] getDateBytes(Date date) {
		return reorderBytes(DataHelper.longToByteArray(date.getTime()));
	}

	public static byte[] getNomalDateBytes(Date date) {
		return DataHelper.longToByteArray(date.getTime());
	}

	public static Date getDate(byte[] source, int start) {
		return new Date(DataHelper.byteToLong(reorderBytes(source), start));
	}

	/**
	 * String To Date
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	public static Date strToDate(String value, String pattern) {
		if (isNull(value)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("'" + pattern + "' Patterned Failed ");
			return null;
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNull(String param) {
		if (param == null || param.trim().equals("")
				|| param.trim().equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static byte[] ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * 小数据转为大数据
	 * 
	 * @param ss
	 * @return
	 */
	public static String toBigSortMac(String ss) {
		StringBuilder sb = new StringBuilder();
		String sss[] = ss.split(":");

		for (int i = sss.length - 1; i >= 0; i--) {
			if (i != 0) {
				sb.append(sss[i]).append(":");
			} else {
				sb.append(sss[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 截取byte数组中的一段
	 * 
	 * @param source
	 *            需要被截取的数组
	 * @param start
	 *            开始下标
	 * @param length
	 *            截取的长度
	 * @return
	 */
	public static byte[] getPartByte(byte[] source, int start, int length) {
		byte[] rs = new byte[length];
		int x = 0;
		for (int i = start; i < start + length; i++) {
			rs[x++] = source[i];
		}
		return rs;
	}

	/**
	 * 拼接byte数组
	 * 
	 * @param result
	 * @param source
	 * @param start
	 * @return
	 */
	public static byte[] arraycat(byte[] buf1, byte[] buf2) {
		byte[] bufret = null;
		int len1 = 0;
		int len2 = 0;
		if (buf1 != null)
			len1 = buf1.length;
		if (buf2 != null)
			len2 = buf2.length;
		if (len1 + len2 > 0)
			bufret = new byte[len1 + len2];
		if (len1 > 0)
			System.arraycopy(buf1, 0, bufret, 0, len1);
		if (len2 > 0)
			System.arraycopy(buf2, 0, bufret, len1, len2);
		return bufret;
	}

	/**
	 * 拼接byte数组
	 * 
	 * @param result
	 *            结果，已知长度
	 * @param source
	 * @param start
	 *            开始位置按数组小标为准
	 * @return
	 */
	public static byte[] pickUpByte(byte[] result, byte[] source, int start) {
		for (int i = 0; i < source.length; i++) {
			result[start++] = source[i];
		}
		return result;
	}

	/**
	 * 从pos开始从source中获取mac地址
	 * 
	 * @param source
	 * @param pos
	 * @return
	 */
	public static String getStrMac(byte[] source, int pos) {
		return getMac(reorderBytes(getPartByte(source, pos, 8)), 2, 6);
	}

	public static byte[] getByte8Mac(String mac) {
		return DataHelper.getMacBytesToBig(DataHelper.getMac8BytesRemote(mac));
	}

	public static byte[] getMacBytes(String paramString) {
		byte[] arrayOfByte = new byte[8];
		String[] arrayOfString = paramString.split(":");
		int len = arrayOfString.length;
		for (int i = 0; i < len; i++) {
			arrayOfByte[i] = bytetoint((byte) Integer.parseInt(
					arrayOfString[i], 16));
		}
		return arrayOfByte;
	}

	public static byte[] long4bytes(long i) {
		byte[] b = new byte[4];
		b[0] = (byte) (0xff & i);
		b[1] = (byte) ((0xff00 & i) >> 8);
		b[2] = (byte) ((0xff0000 & i) >> 16);
		b[3] = (byte) ((0xff000000 & i) >> 24);
		reorderBytes(b);
		return b;
	}

	public static String byte2String(byte[] source) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < source.length; i++) {
			sb.append(source[i]).append(",");
		}
		return sb.toString();
	}

	/**
	 * 将时间转成short  (5:30)
	 * @param hour
	 * @param min
	 * @return
	 */
	public static short timeToHexString(int hour,int min){
		StringBuffer resultSb = new StringBuffer();
		if (hour>=16) {
			if (min>=16) {
				resultSb.append(Integer.toHexString(min)).append(Integer.toHexString(hour));
			}else {
				resultSb.append("0").append(Integer.toHexString(min)).append(Integer.toHexString(hour));
			}
		}else  {
			if (min>=16) {
				resultSb.append(Integer.toHexString(min)).append("0").append(Integer.toHexString(hour));
			}else {
				resultSb.append("0").append(Integer.toHexString(min)).append("0").append(Integer.toHexString(hour));
			}
		} 
		short ret = bytesToShort(str16tobytes(resultSb.toString()), 0);
		return ret;};
		/**
		 * 把byte数组转成16进制字符串
		 * 
		 * @param b
		 * @param pos
		 *            起始下标
		 * @param len
		 *            长度
		 * @return
		 */
		public static String byteArrayToHexString(byte[] b, int pos, int len) {
			StringBuffer resultSb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				resultSb.append(byteToHexString(b[pos + i]));
			}
			return resultSb.toString();
		}

		/**
		 * 16进制字符串转byte数组
		 * 
		 * @param hexStr
		 *            16进制字符串
		 * @return byte数组
		 */
		public static byte[] hexStringToByteArray(String hexStr) {
			String str = "0123456789abcdef";
			char[] hexs = hexStr.toLowerCase().toCharArray();
			byte[] bytes = new byte[hexStr.length() / 2];
			int n;
			for (int i = 0; i < bytes.length; i++) {
				n = str.indexOf(hexs[2 * i]) * 16;
				n += str.indexOf(hexs[2 * i + 1]);
				bytes[i] = (byte) (n & 0xff);
			}
			return bytes;
		}

		/**
		 * Mac 转换成byte数组
		 * 
		 * @param mac
		 * @return
		 */
		public static byte[] macStringToByteArray(String mac) {
			if (mac == null || mac.equals("")) {
				System.err.println("mac is NULL");
				return null;
			}
			return hexStringToByteArray(mac.replace(":", "").replace("-", ""));
		}

		/**
		 * 将1byte转换成8bit字符串，e.g. 0x12=00010010
		 * 
		 * @param b
		 * @return
		 */
		public static String byte2bits(byte b) {
			int z = b;
			z |= 256;
			String str = Integer.toBinaryString(z);
			int len = str.length();
			return str.substring(len - 8, len);
		}

		/**
		 * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
		 * 
		 * @param value
		 *            要转换的int值
		 * @return byte数组
		 */
		public static byte[] intToBytes(int value) {
			byte[] src = new byte[4];
			src[3] = (byte) ((value >> 24) & 0xFF);
			src[2] = (byte) ((value >> 16) & 0xFF);
			src[1] = (byte) ((value >> 8) & 0xFF);
			src[0] = (byte) (value & 0xFF);
			return src;
		}

		/**
		 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
		 */
		public static byte[] intToBytes2(int value) {
			byte[] src = new byte[4];
			src[0] = (byte) ((value >> 24) & 0xFF);
			src[1] = (byte) ((value >> 16) & 0xFF);
			src[2] = (byte) ((value >> 8) & 0xFF);
			src[3] = (byte) (value & 0xFF);
			return src;
		}

		/**
		 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
		 * 
		 * @param src
		 *            byte数组
		 * @param offset
		 *            从数组的第offset位开始
		 * @return int数值
		 */
		public static int bytesToInt(byte[] src, int offset) {
			int value;
			value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
					| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
			return value;
		}

		/**
		 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
		 */
		public static int bytesToInt2(byte[] src, int offset) {
			int value;
			value = (int) (((src[offset] & 0xFF) << 24)
					| ((src[offset + 1] & 0xFF) << 16)
					| ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
			return value;
		}

		/**
		 * byte转换为short,先倒序然后进行转换
		 * 
		 * @param src
		 * @param positon
		 *            开始位置
		 * @return
		 */
		public static short bytesToShort(byte[] src, int positon) {
			byte[] data = new byte[2];
			System.arraycopy(src, positon, data, 0, 2);
			short value = 0;
			data = reorderBytes(data);
			value = (short) (((data[0] < 0 ? data[0] + 256 : data[0]) << 8) + (data[1] < 0 ? data[1] + 256
					: data[1]));
			return value;
		}
		
		/**
		 * 获取返回的时间字符串
		 * @param src
		 * @param positon
		 * @return
		 */
		public static String bytesToHexString(byte[] src, int positon) {
			byte[] data = new byte[2];
			System.arraycopy(src, positon, data, 0, 2);
			short value = 0;
			data = reorderBytes(data);
			String string = byteArrayToHexString(data);
			return string ;
		}

		/**
		 * long转换为byte,倒序输出byte
		 * 
		 * @param value
		 * @return
		 */
		public static byte[] longToBytes(long value) {
			byte[] data = new byte[8];
			data = longToByteArray(value);
			data = reorderBytes(data);
			return data;
		}

		/**
		 * int转换为byte,倒序输出byte
		 * 
		 * @param value
		 * @return
		 */
		public static byte[] intToBytesF(int value) {
			byte[] data = new byte[4];
			data = reorderBytes(intToBytes2(value));
			return data;
		}

		/**
		 * 将16位的short转换成byte数组,倒序输出
		 * 
		 * @param s
		 *            short
		 * @return byte[] 长度为2
		 * */
		public static byte[] shortToBytesF(short s) {
			byte[] targets = new byte[2];
			for (int i = 0; i < 2; i++) {
				int offset = (targets.length - 1 - i) * 8;
				targets[i] = (byte) ((s >>> offset) & 0xff);
			}
			return reorderBytes(targets);
		}

		/**
		 * 从8字节的byte[]中取出Mac地址
		 * 
		 * @param data
		 *            数组，例如取出的bytes[]={0x00,0x08,0x61,0x38,0x12,0x34,0x56,0x78}
		 * @param index
		 *            从第index处取8byte
		 * @return Mac值，例如8613812345678
		 */
		public static String analyzeMac(byte[] data, int index) {
			byte[] macArr = new byte[8];
			System.arraycopy(data, index, macArr, 0, 8);
			String macHex = DataHelper.byteArrayToHexString(macArr);
			for (int i = 0; i < macHex.length(); i++) {
				if (macHex.charAt(i) != '0') {
					return macHex.substring(i);
				}
			}
			return macHex;
		}

		/**
		 * 将Mac处理成标准8byte Mac,不足8byte,在前面加0
		 * 
		 * @param mac
		 *            16进制Mac,例如8613812345678
		 * @return 处理后的Mac 例如byte[8]={0x00,0x08,0x61,0x38,0x12,0x34,0x56,0x78}
		 */
		public static byte[] buildMacBytes(String mac) {
			String smac = mac.replace(":", "");
			int len = smac.length();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
			return DataHelper.hexStringToByteArray(sb.append(smac).toString());
		}

		/**
		 * 设置week有效参数，weekStatus[0]=true,表示星期1有效
		 * 
		 * @param weekStatus
		 *            星期1~7数组
		 * @return week 值
		 */
		public static byte buildWeekArray(boolean[] weekStatus) {
			byte temp = 0;
			for (int i = 0; i < 7; i++) {
				temp += (weekStatus[i] ? 1 : 0) << i;
			}
			return temp;
		}

		/**
		 * 解析week有效值，第0位表示星期一
		 * 
		 * @return week设置情况
		 */
		public static boolean[] analyzeWeekArray(byte week) {
			boolean[] weekStatus = new boolean[7];
			for (int i = 0; i < 7; i++) {
				weekStatus[i] = (week & 1) > 0 ? true : false;
				week = (byte) (week >> 1);
			}
			return weekStatus;
		}
}
