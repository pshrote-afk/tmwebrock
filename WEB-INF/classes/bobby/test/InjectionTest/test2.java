package bobby.test.InjectionTest;

import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;

@InjectApplicationScope
@InjectSessionScope
@InjectRequestScope
@Path("/injection")
public class test2
{
private ApplicationScope applicationScope;
private SessionScope sessionScope;
private RequestScope requestScope;

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

//services start
@Path("/add")
public void someMethod1()
{
applicationScope.setAttribute("age",32);
sessionScope.setAttribute("id","A1001");
requestScope.setAttribute("name","username");
System.out.println("someMethod() from InjectionTest folder ran: "+requestScope.getAttribute("name"));
System.out.println("someMethod() from InjectionTest folder ran: "+sessionScope.getAttribute("id"));
System.out.println("someMethod() from InjectionTest folder ran: "+applicationScope.getAttribute("age"));
}

@Path("/get")
public String someMethod2()
{
return "Application scope, \"appName\" attribute: "+applicationScope.getAttribute("appName");
}

}