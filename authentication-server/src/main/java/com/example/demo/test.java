package com.example.demo;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;


public class test {

	public static void main(String[] args) {
		String t = Base64.getEncoder().encodeToString("resource1:secret".getBytes());
		String m= UUID.randomUUID().toString().replace("-", "");
		System.out.println(t);
		System.out.println(m);
		System.out.println(KeyValue_lenght(64,false));
	}
	private static String KeyValue_lenght(int lenght, boolean... ma) {
		Random random = new Random();
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < lenght; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return ma.length != 0 ? ma[0] ? sb.toString().toUpperCase() : sb.toString().toLowerCase() : sb.toString();
    }
}
