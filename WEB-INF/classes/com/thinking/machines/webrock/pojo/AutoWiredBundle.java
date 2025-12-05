// a bundle contains the following information regarding @AutoWired. 
// 1) the field on which @AutoWired is applied 
// 2) name of the resource which needs to be auto wired
package com.thinking.machines.webrock.pojo;

import java.lang.reflect.*;

public class AutoWiredBundle
{
private Field field;
private String autoWiredName;

public Field getField() {
    return this.field;
}

public void setField(Field field) {
    this.field = field;
}

public String getAutoWiredName() {
    return this.autoWiredName;
}

public void setAutoWiredName(String autoWiredName) {
    this.autoWiredName = autoWiredName;
}
}