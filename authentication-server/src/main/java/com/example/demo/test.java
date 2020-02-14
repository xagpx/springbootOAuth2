package com.example.demo;

import java.util.Base64;

public class test {

	public static void main(String[] args) {
		String t = Base64.getEncoder().encodeToString("resource1:secret".getBytes());
		System.out.println(t);
	}

}
