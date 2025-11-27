package com.thinking.machines.webrock.pojo;

import java.lang.reflect.*;
import com.thinking.machines.webrock.annotations.*;

public class Service 
{
private Class serviceClass;
private String path;
private Method service;
private String forwardTo;
boolean runOnStart;
int priority;

public Service()
{
serviceClass = null;
path = null;
service = null;
forwardTo = null;
boolean runOnStart = false;
int priority = 0;
}

public void setServiceClass(Class serviceClass)
{
this.serviceClass = serviceClass;
}
public Class getServiceClass()
{
return this.serviceClass;
}

public void setPath(String path)
{
this.path = path;
}
public String getPath()
{
return this.path;
}

public void setService(Method service)
{
this.service = service;
}
public Method getService()
{
return this.service;
}

public void setForwardTo(String forwardTo)
{
this.forwardTo = forwardTo;
}
public String getForwardTo()
{
return this.forwardTo;
}

public void setRunOnStart(boolean runOnStart)
{
this.runOnStart = runOnStart;
}
public boolean getRunOnStart()
{
return this.runOnStart;
}

public void setPriority(int priority)
{
this.priority = priority;
}
public int getPriority()
{
return this.priority;
}

}