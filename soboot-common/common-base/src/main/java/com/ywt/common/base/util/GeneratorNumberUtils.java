package com.ywt.common.base.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class GeneratorNumberUtils {

        //数字秘钥
        private final static long SECRET_KEY = 5658116;
        //转换字符（0-9分别为["D","e","C","A","#","b","J","I","z","M"]
        private final static String CONVERT_KEY = "DeCA#bJIzM";
        //混淆字母
        private final static String CONFUSED_WORDS_KEY = "FxYNgq";
        //总加密字符串长度
        private final static int LEN_KEY = 32;

        private static final String[] m = {"D","e","C","A","#","b","J","I","z","M"};

        private static final String[] m2 = {"D","e","C","A","b","J","I","z","M","0","1","2","3","4","5","6"};
        public static String generator(){

            StringBuffer buffer = new StringBuffer();

            for(int i=0;i<13;i++){
                int j = new Random().nextInt(15);
                System.out.println(j);
                buffer.append(m2[j]);
            }
            return buffer.toString();
        }

        /**
         *   数字加密算法
         **/
        public String encrypt(String str,long time){

            //数字校验
            if(!isNumber(str)){
                System.out.println(str + "不是数字");
                return null;
            }

            long number = Long.parseLong(str);
            long newNumber = (number + time) * SECRET_KEY;
            String[] numArr = String.valueOf(newNumber).split("");
            String[] initArr = CONVERT_KEY.split("");
            int len = numArr.length;
            StringBuffer buffer = new StringBuffer();

            //数字转字母
            for(int i = 0; i < len; i++){
                int inx = Integer.parseInt(numArr[i]);
                buffer.append(initArr[inx]);
            }

            //随机加入混淆字符
            String[] cwkArr = CONFUSED_WORDS_KEY.split("");
            if(len < LEN_KEY){
                int l = LEN_KEY - len;
                for(int i = 0; i < l; i++){
                    int index = (int)(Math.random()*buffer.length());
                    int inx = (int)(Math.random()*(CONFUSED_WORDS_KEY.length()));
                    buffer.insert(index,cwkArr[inx]);
                }
            }
            String result = buffer.toString();
            return result;
        }

        /**
         * 解密算法
         * */
        public String decrypt(String str,long time){
            if(null == str || "".equals(str)){
                return null;
            }
            int l = CONFUSED_WORDS_KEY.length();
            String[] cwkArr = CONFUSED_WORDS_KEY.split("");
            for(int i = 0; i < l; i++){
                str = str.replaceAll(cwkArr[i],"");
            }
            String[] initArr = str.split("");
            int len = initArr.length;
            StringBuffer result = new StringBuffer();
            for(int i = 0; i < len; i++ ){
                int k = CONVERT_KEY.indexOf(initArr[i]);
                if(k == -1){
                    return null;
                }
                result.append(k);
            }
            Long number;
            try {
                long total = Long.parseLong(result.toString());
                long sum = total/SECRET_KEY;
                number = sum - time;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
            return number.toString();
        }

        /**
         *   测试
         **/
        public static void main(String[] args) {

            System.out.println(GeneratorNumberUtils.generator());
            long time = System.currentTimeMillis();
            System.out.println("time:" + time);
            GeneratorNumberUtils t = new GeneratorNumberUtils();
            String number = "100085";
            System.out.println(number);
            String result = t.encrypt(number,time);
            t.decrypt(result,time);

            encry("100085");

            decry("eDDDzb");
        }

        /**
         *  数字校验
         * */
        public static boolean isNumber(String value) {
            String pattern = "^[0-9]*[1-9][0-9]*$";
            boolean isMatch = Pattern.matches(pattern, value);
            return isMatch;
        }

        public static String encry(String str){

            if(!isNumber(str)){
                return null;
            }

            StringBuilder builder = new StringBuilder();
            for(int i=0;i<str.length();i++){
                int t = Integer.parseInt(String.valueOf(str.charAt(i)));
                builder.append(m[t]);

            }
            return builder.toString();
        }

        public static String decry(String str){

            if(StringUtils.isEmpty(str)){
                return null;
            }

            StringBuilder builder = new StringBuilder();
            for(int i=0;i<str.length();i++){
                String t = String.valueOf(str.charAt(i));
                for(int j = 0;j<m.length;j++){
                    if(t.equals(m[j])){
                        builder.append(j);
                        break;
                    }
                }
            }
            return builder.toString();
        }
}
