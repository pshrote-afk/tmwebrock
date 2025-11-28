package bobby.test.OnStartupTest;

import com.thinking.machines.webrock.annotations.*;

public class test1
{
@OnStartup(Priority=3)
public void upRoot()
{
System.out.println("Priority 3. Startup method uproot() did something");
}

@OnStartup(Priority=5)
@Path("/grow1")
public int method1()
{
return 5;
}
@OnStartup(Priority=6)
@Path("/grow2")
public int method1(int x)
{
return 5;
}

@OnStartup(Priority=2)
@Path("/startGrowing")
public void method4()
{
System.out.println("Started growing");
}

@OnStartup(Priority=2)
public String method2()
{
return "Method2 did something";
}

//example 3 of forward
@Forward("/car/update")
@Path("/get")
public void method3()
{
System.out.println("Method3 of tree is going to forward to /car/update did something");
}
}