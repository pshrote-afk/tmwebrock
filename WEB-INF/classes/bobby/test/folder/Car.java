package bobby.test.folder;

import com.thinking.machines.webrock.annotations.*;

@Get
@Path("/car")
public class Car
{

@Path("/add")
public void method1()
{
System.out.println("Method1 did something for Car");
}

@Path("/update")
public String method2()
{
return "Method2 did something for Car";
}


public void helperFunction()
{
}

@Post
@Path("/getAll")
public void method3()
{
System.out.println("Method3 did something for Car");
}
}