package bobby.test;

import com.thinking.machines.webrock.annotations.*;

@Path("/student")
public class Student
{

@Post
@Path("/add")
public void method1()
{
System.out.println("Method1 did something");
}

@Post
@Path("/update")
public void method2()
{
System.out.println("Method2 did something");
}

@Get
@Path("/getAll")
public void method3()
{
System.out.println("Method3 did something");
}
}