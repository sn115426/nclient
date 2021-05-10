package com.ngames.nclient.module;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.lwjgl.input.Keyboard;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ModuleType
{
	String name();
	String description();
	String words(); //words to find a module in ClickGUI using navigation
	int keyBind() default Keyboard.KEY_NONE;
	Category category();
}
