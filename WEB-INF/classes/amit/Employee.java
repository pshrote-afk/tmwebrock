package amit;

import com.thinking.machines.webrock.annotations.*;

@Path("/employee")
public class Employee
{
@Path("/add")
public void method1()
{
System.out.println("Method1 did something");
}
@Path("/update")
public void method2()
{
System.out.println("Method2 did something");
}
@Path("/get")
public void method3()
{
System.out.println("Method3 did something");
}
}