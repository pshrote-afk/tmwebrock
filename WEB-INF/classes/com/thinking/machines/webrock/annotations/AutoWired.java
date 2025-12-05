package com.thinking.machines.webrock.annotations;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)		//default is RetentionPolicy.CLASS which is not available via java reflection during runtime
@Target(ElementType.FIELD)
public @interface AutoWired
{
String name();
}