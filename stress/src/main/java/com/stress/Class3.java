package com.stress;

import com.jtransc.annotation.JTranscKeep;

@JTranscKeep
public class Class3 {

public static final String static_const_3_0 = "Hi, my num is 3 0";

static int static_field_3_0;

int member_3_0;

public void method3()
{
	System.out.println(static_const_3_0);
}

public void method3_1(int p0, String p1)
{
	System.out.println(p1);
}

}
