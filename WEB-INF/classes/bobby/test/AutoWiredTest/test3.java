package bobby.test.AutoWiredTest;

import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;

//@InjectRequestScope
@InjectSessionScope
@Path("/autowiredTest")
public class test3
{
@AutoWired(name="xyz")
private Student student;

@AutoWired(name="bulb1")
private Bulb bulb1;

@AutoWired(name="bulb2")
private Bulb bulb2;


private ApplicationScope applicationScope;
private SessionScope sessionScope;
private RequestScope requestScope;

public void setStudent(Student student)
{
this.student = student;
}
public Student getStudent()
{
return this.student;
}

public void setBulb1(Bulb bulb1)
{
this.bulb1 = bulb1;
}
public Bulb getBulb1()
{
return this.bulb1;
}

public void setBulb2(Bulb bulb2)
{
this.bulb2 = bulb2;
}
public Bulb getBulb2()
{
return this.bulb2;
}


public void setApplicationScope(ApplicationScope applicationScope)
{
this.applicationScope = applicationScope;
}
public ApplicationScope getApplicationScope()
{
return this.applicationScope;
}


public void setSessionScope(SessionScope sessionScope)
{
this.sessionScope = sessionScope;
}
public SessionScope getSessionScope()
{
return this.sessionScope;
}


public void setRequestScope(RequestScope requestScope)
{
this.requestScope = requestScope;
}
public RequestScope getRequestScope()
{
return this.requestScope;
}

@Forward("/autowiredTest/get")
@Path("/set")
public void someMethod1()
{
Bulb bulb1 = new Bulb();
bulb1.setWattage(60);
sessionScope.setAttribute("bulb1",bulb1);

Bulb bulb2 = new Bulb();
bulb2.setWattage(100);
requestScope.setAttribute("bulb2",bulb2);
}

@Path("/get")
public void someMethod2()
{

System.out.println("AutoWired example. Application scope.\nStudent name: "+student.getName());	//autowired from application scope. Initially it was loaded into application scope via @OnStartup

System.out.println("AutoWired example. Session scope.\nBulb wattage: "+bulb1.getWattage());	//autowired from session scope
System.out.println("AutoWired example. Request scope.\nBulb wattage: "+bulb2.getWattage());	//autowired from request scope
}
}