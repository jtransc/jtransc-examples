package com.stress;

import com.jtransc.annotation.JTranscKeep;

@JTranscKeep
public class Class4 {

public static final String static_const_4_0 = "Hi, my num is 4 0";

static int static_field_4_0;

int member_4_0;

public void method4()
{
	System.out.println(static_const_4_0);
}

public void method4_1(int p0, String p1)
{
	System.out.println(p1);
}

}
