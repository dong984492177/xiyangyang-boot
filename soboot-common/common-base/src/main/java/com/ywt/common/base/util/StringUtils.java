package com.ywt.common.base.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: huangchaoyang
 * @Description: 字符串工具类
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final char SEPARATOR = '_';
	private static final String CHARSET_NAME = "UTF-8";
	private static final Logger logger  = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 转换为字节数组
	 * @param str
	 * @return
	 */
	public static byte[] getBytes(String str) {
		if(str != null) {
			try {
				return str.getBytes(CHARSET_NAME);
			} catch(UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 转换为字节数组
	 * @param bytes
	 * @return
	 */
	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, CHARSET_NAME);
		} catch(UnsupportedEncodingException e) {
			return EMPTY;
		}
	}

	/**
	 * 是否包含字符串
	 * @param str 验证字符串
	 * @param strs 字符串组
	 * @return 包含返回true
	 */
	public static boolean inString(String str, String... strs) {
		if(str != null) {
			for(String s : strs) {
				if(str.equals(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if(isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html) {
		if(html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>","<$1>");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if(str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for(char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if(currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String abbr2(String param, int length) {
		if(param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for(int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if(temp == '<') {
				isCode = true;
			} else if(temp == '&') {
				isHTML = true;
			} else if(temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if(temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if(!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if(n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)","$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result.replaceAll(
				"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
				"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>","$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while(m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for(int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if(val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch(Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if(isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("x-forwarded-for");
		} else if(isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if(isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		if(StringUtils.isNotBlank(remoteAddr)){
			String[] split = remoteAddr.split(",");
			remoteAddr = split[0];
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if(s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if(c == SEPARATOR) {
				upperCase = true;
			} else if(upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if(s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if(s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if(i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if((i > 0) && Character.isUpperCase(c)) {
				if(!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 如果不为空，则设置值
	 * @param target
	 * @param source
	 */
	public static void setValueIfNotBlank(String target, String source) {
		if(isNotBlank(source)) {
			target = source;
		}
	}

	/**
	 * 转换为JS获取对象值，生成三目运算返回结果
	 * @param objectString 对象串
	 *   例如：row.user.id
	 *   返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
	 */
	public static String jsGetVal(String objectString) {
		StringBuilder result = new StringBuilder();
		StringBuilder val = new StringBuilder();
		String[] vals = split(objectString,".");
		for(int i = 0; i < vals.length; i++) {
			val.append("." + vals[i]);
			result.append("!" + (val.substring(1)) + "?'':");
		}
		result.append(val.substring(1));
		return result.toString();
	}

	/**
	 * 将List转化成字符串
	 * @param list
	 * @param symbol
	 * @return
	 */
	public static String toStringFromList(List<String> list, String symbol) {
		String result = "";
		if(null != list && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				result = result + list.get(i);
				if(i < list.size() - 1) {
					result = result + symbol;
				}
			}
		}
		return result;
	}

	/**
	 * 将字符串数组用指定的分隔符拼接成一个字符串
	 *
	 * e.g
	 * ["a", "b", "c"], " " -> "a b c";
	 *
	 * @param array
	 * @param split
	 * @param ignoreEmptyStr 是否忽略空字符串(null or "")
	 * @return
	 */
	public static String concatenate(String[] array, String split, boolean ignoreEmptyStr) {
		if (array == null || array.length == 0) return "";

		StringBuilder result = new StringBuilder();

		for (String s : array) {
			if (ignoreEmptyStr &&
					(s == null || "".equals(s))) {
				continue;
			}
			result.append(s).append(split);
		}

		return result.toString();
	}

	/**
	 * 判断2个字符串是否相等（大小写敏感）
	 * 若字符串为null, 当空字符串""处理
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsDeNull(String str1, String str2) {
        return (str1 == null ? "":str1).equals(str2 == null ? "":str2);
    }

	/**
	 * 解析字符串，返回键值对。比如字符串m:8;n:1;q:1返回的map为{m=8, n=1, q=1}
	 * @param string
	 * @param itemSpliter
	 * @param keyValueSpliter
	 * @return
	 */
	public static Map<String, String> parseString2Map(String string, String itemSpliter, String keyValueSpliter){
		Map<String, String> map = new HashMap<String, String>();
		if(isBlank(string) || isBlank(itemSpliter) || isBlank(keyValueSpliter)){
			return map;
		}
		String[] itemArray = string.split(itemSpliter);
		for (String itemStr : itemArray) {
			String[] keyValueArray = itemStr.split(keyValueSpliter);
			map.put(keyValueArray[0], keyValueArray[1]);
		}
		return map;
	}

	/**
	 * 将unicode码反转成String
	 *
	 * @param str
	 * @return
	 */
	public static String unicodeToString(String str) {
		if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
			return str;
		}
		char aChar;
		int len = str.length();
		StringBuffer outBuffer = new StringBuffer(len);
		StringBuffer tempChar = null;
		boolean needTransfer = true;
		for (int x = 0; x < len;) {
			aChar = str.charAt(x++);
			if (aChar == '\\') {
				tempChar = new StringBuffer();
				tempChar.append(aChar);
				aChar = str.charAt(x++);
				tempChar.append(aChar);
				if (aChar == 'u') {
					int value = 0;
					needTransfer = true;
					for (int i = 0; i < 4; i++) {
						aChar = str.charAt(x++);
						tempChar.append(aChar);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								needTransfer = false;
								x--;
								tempChar.deleteCharAt(tempChar.length()-1);
								break;
						}
					}
					if (needTransfer) {
						outBuffer.append((char) value);
					} else {
						outBuffer.append(tempChar);
					}
				} else {
					outBuffer.append(tempChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}


	/**
	 * URLEncode参数并拼接
	 */
	public static String concatParamsWithURLEncode(Map<String, String> map) throws UnsupportedEncodingException {
	    return  concatParamsWithURLEncode(map, "utf-8");
	}

	/**
	 * URLEncode参数并拼接
	 */
	public static String concatParamsWithURLEncode(Map<String, String> map, String charset) throws UnsupportedEncodingException {
		if (map == null || map.size() <= 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String, String>> entrySet = map.entrySet();
		for(Map.Entry<String, String> entry : entrySet){
			if (entry.getValue() != null) {
				if(sb.length() > 0){
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset));
			}
		}

		return  sb.toString();
	}

	/**
	 * URLEncode参数并拼接
	 */
	public static String concatParamsWithoutURLEncode(Map<String, String> map) {
		if (map == null || map.size() <= 0) {
			return "";
		}

	    StringBuilder sb = new StringBuilder();
	    Set<Map.Entry<String, String>> entrySet = map.entrySet();
	    for(Map.Entry<String, String> entry : entrySet){
	    	if (entry.getValue() != null) {
		        if(sb.length() > 0){
		            sb.append("&");
		        }
		        sb.append(entry.getKey()).append("=").append(entry.getValue());
	    	}
	    }

	    return  sb.toString();
	}

	/**
	 * 截取指定长度的字符串
	 * @param str
	 * @param length
	 */
	public static String truncate(String str, int length){
		if(isNotBlank(str) && str.length() > length){
			return str.substring(0, length);
		}
		return str;
	}

	/**
	 * 转换null为空字符
	 * @param str
	 * @return
	 */
	public static String deNull(String str){
		if(str == null){
			return "";
		}
		return str;
	}

	/**
	 * 根据现在的后缀获取下一个后缀
	 * 后缀格式是字母 AA  BB CC。。。。ZZ , AB, AC, AD。。。。
	 * @param curSuffix
	 * @return
	 */
	public static String getNextSuffix(String curSuffix){
		if(isEmpty(curSuffix) || curSuffix.length()!=2){
			return null;
		}
		String nextSuffix = null;
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		LinkedHashSet<String> allSuffix = new LinkedHashSet<>();
		for (int i = 0; i < letters.length(); i++) {
			allSuffix.add(String.valueOf(letters.charAt(i)) + String.valueOf(letters.charAt(i)));
		}
		for (int i = 0; i < letters.length(); i++) {
			for (int j = 0; j < letters.length(); j++) {
				String s = String.valueOf(letters.charAt(i));
				String s1 = String.valueOf(letters.charAt(j));
				if(!s.equals(s1)){
					allSuffix.add(s + s1);
				}
			}
		}
		Iterator<String> iterator = allSuffix.iterator();
		while (iterator.hasNext()){
			String next = iterator.next();
			if(curSuffix.equals(next)){
				if(iterator.hasNext()){
					nextSuffix = iterator.next();
					break;
				}
			}
		}
		return nextSuffix;
	}

	/**
	 * 判断传入参数是否全为中文汉字或英文
	 *
	 * @param name
	 * @return
	 */
	public static boolean isChineseOrEnglish(String name) {

		String regEx = "[a-zA-Z\u4E00-\u9FFF]+";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(name.trim());
		return matcher.matches();
	}

	/**
	 * 判断将一个十进制数字转换成二进制，判断二进制第几位是1
	 * @param decimalNum 十进制数字
	 * @param number 判断第几位
	 * @return
	 */
	public static boolean checkBinaryNumber(int decimalNum,int number){
		//判断第number位是否为1
		int result = 1 << number-1;
		if ((decimalNum & result) == result)
		{
			return true;
		}else {
			return false;
		}
	}


	/**
	 * 获取昵称
	 * @param nickName
	 * @param type
	 * @return
	 */
	public static String handleNickName(String nickName, String type) {
		String tmpNickName = filterEmoji(nickName);
		try {
			if (StringUtils.equalsIgnoreCase(type, "encode")) {
				tmpNickName = URLEncoder.encode(nickName, "UTF-8");
			} else if (StringUtils.equalsIgnoreCase(type, "decode")) {
				tmpNickName = URLDecoder.decode(nickName, "UTF-8");
			}
		} catch (Exception e) {
			tmpNickName = StringUtils.filterEmoji(nickName);
		}
		return tmpNickName;
	}



	/**
	 * 过滤emoji表情
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
        }
        return source;
    }

	public static <T> void stringNullToEmpty(T t) {
		if (null == t) {
			return;
		}
		Field[] declaredFields = t.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			if (field.getType().equals(String.class)) {
				// 将属性的首字母大写
				String methodName = field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase());
				try {
					Method methodGet = t.getClass().getMethod("get" + methodName);
					// 调用getter方法获取属性值
					String str = (String) methodGet.invoke(t);
					if (StringUtils.isBlank(str)) {
						// 如果为null的String类型的属性则重新复制为空字符串
						field.set(t, field.getType().getConstructor(field.getType()).newInstance(""));
					}
				} catch (Exception e) {
					logger.warn("[EmptyNullUtil.stringBlankToNull] e:{}", e);
				}
			}
		}
	}

}
