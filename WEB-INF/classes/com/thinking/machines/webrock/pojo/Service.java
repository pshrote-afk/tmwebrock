package com.thinking.machines.webrock.pojo;

import java.util.*;
import java.lang.reflect.*;
import com.thinking.machines.webrock.annotations.*;

public class Service 
{
private Class serviceClass;
private String path;
private Method service;
private String forwardTo;
private boolean runOnStart;
private int priority;
private boolean injectApplicationScope;
private boolean injectSessionScope;
private boolean injectRequestScope;
private boolean injectApplicationDirectory;
ArrayList<AutoWiredBundle> autoWiredBundles;

public Service()
{
serviceClass = null;
path = null;
service = null;
forwardTo = null;
runOnStart = false;
priority = 0;
injectApplicationScope = false;
injectSessionScope = false;
injectRequestScope = false;
injectApplicationDirectory = false;
autoWiredBundles = null;
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

public void setInjectApplicationScope(boolean injectApplicationScope)
{
this.injectApplicationScope = injectApplicationScope;
}
public boolean getInjectApplicationScope()
{
return this.injectApplicationScope;
}

public void setInjectSessionScope(boolean injectSessionScope)
{
this.injectSessionScope = injectSessionScope;
}
public boolean getInjectSessionScope()
{
return this.injectSessionScope;
}

public void setInjectRequestScope(boolean injectRequestScope)
{
this.injectRequestScope = injectRequestScope;
}
public boolean getInjectRequestScope()
{
return this.injectRequestScope;
}

public void setInjectApplicationDirectory(boolean injectApplicationDirectory)
{
this.injectApplicationDirectory = injectApplicationDirectory;
}
public boolean getInjectApplicationDirectory()
{
return this.injectApplicationDirectory;
}

public void setAutoWiredBundles(ArrayList<AutoWiredBundle> autoWiredBundles)
{
this.autoWiredBundles = autoWiredBundles;
}
public ArrayList<AutoWiredBundle> getAutoWiredBundles()
{
return this.autoWiredBundles;
}

}