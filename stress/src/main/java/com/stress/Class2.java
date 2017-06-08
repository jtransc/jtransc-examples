package com.stress;

import com.jtransc.annotation.JTranscKeep;

@JTranscKeep
public class Class2 {

public static final String static_const_2_0 = "Hi, my num is 2 0";

static int static_field_2_0;

int member_2_0;

public void method2()
{
	System.out.println(static_const_2_0);
}

public void method2_1(int p0, String p1)
{
	System.out.println(p1);
}

}
