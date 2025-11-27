package bobby;

import com.thinking.machines.webrock.annotations.*;

@Path("/tree")
public class Tree
{
public void upRoot()
{
System.out.println("Method uproot() did something");
}

//example 1 of forward
@Forward("/tree/stopGrowing")
@Path("/grow")
public int method1()
{
return 5;
}

@Path("/stopGrowing")
public String method4()
{
return "Stopped growing";
}

//example 2 of forward
@Forward("/testservice/city/ujjain/gods")
@Path("/update")
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