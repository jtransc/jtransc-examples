package com.stress;

import com.jtransc.annotation.JTranscKeep;

@JTranscKeep
public class Class1 {

public static final String static_const_1_0 = "Hi, my num is 1 0";

static int static_field_1_0;

int member_1_0;

public void method1()
{
	System.out.println(static_const_1_0);
}

public void method1_1(int p0, String p1)
{
	System.out.println(p1);
}

}
