package com.thinking.machines.webrock;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.webrock.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotations.*;

public class TMWebRock extends HttpServlet
{
public void doGet(HttpServletRequest request, HttpServletResponse response)
{
try
{
PrintWriter pw = response.getWriter();
response.setContentType("text/plain");

//part 1
String uri = request.getRequestURI(); 	//eg. "/tmwebrock/schoolservice/student/update"
String parts[] = uri.split("schoolservice");
String clientPath = parts[1];				//eg. "/student/update"
System.out.println("uri: "+request.getRequestURI());
System.out.println("Client path: "+clientPath);

//part 2
ServletContext servletContext = getServletContext();
Map<String,Service> servicesMap = (HashMap)servletContext.getAttribute("ServicesMap");

Service serviceObject = servicesMap.get(clientPath);
System.out.println(serviceObject.getServiceClass());
System.out.println(serviceObject.getPath());
System.out.println(serviceObject.getService());
Class<?> serviceClass = serviceObject.getServiceClass();
String path = serviceObject.getPath();
Method service = serviceObject.getService();

//part 3
Object obj1 = serviceClass.getDeclaredConstructor().newInstance();
Object returnResponse;
returnResponse = service.invoke(obj1);
if(returnResponse==null) returnResponse = "";	//return empty string if "service" has "void" return type

pw.print(returnResponse);
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
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
}catch(Exception e)
{
System.out.println(e);
}

}
} //end of class