package test;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.webrock.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotations.*;

public class SomeClass extends HttpServlet
{
public void doGet(HttpServletRequest request, HttpServletResponse response)
{
try
{
PrintWriter pw = response.getWriter();
pw.print("doGet of SomeClass ran");
pw.flush();
}catch(Exception e)
{
System.out.println(e);
}
}
public void doPost(HttpServletRequest request, HttpServletResponse response)
{
try
{
PrintWriter pw = response.getWriter();
pw.print("doPost of SomeClass ran");
pw.flush();
}catch(Exception e)
{
System.out.println(e);
}
}
} //end of class