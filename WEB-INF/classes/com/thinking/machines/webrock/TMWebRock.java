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

public void service(HttpServletRequest request, HttpServletResponse response)
{
    try {
        String uri = request.getRequestURI(); 	//eg. "/tmwebrock/schoolservice/student/update"
        String parts[] = uri.split("schoolservice");
        String clientPath = parts[1];				//eg. "/student/update"
        System.out.println("uri: "+request.getRequestURI());
        System.out.println("Client path: "+clientPath);

        ServletContext servletContext = getServletContext();
        Map<String,Service> servicesMap = (HashMap)servletContext.getAttribute("ServicesMap");

        Service serviceObject = servicesMap.get(clientPath);
        Class<?> serviceClass = serviceObject.getServiceClass();
        Method service = serviceObject.getService();

        String method = request.getMethod().toUpperCase();
        String serviceLevelMethod = "";
        String serviceClassLevelMethod = "";

        // method-level get/post will get precedence over class-level get/post. 
        // eg. class has get annotation, but its method has post annotation.

        //Check 1/3
        if(service.isAnnotationPresent(Get.class) || service.isAnnotationPresent(Post.class))
        {
            if(service.isAnnotationPresent(Get.class) )
            {
                serviceLevelMethod = "Get";
            }
            if(service.isAnnotationPresent(Post.class) )
            {
                serviceLevelMethod = "Post";
            }
            if(method.equalsIgnoreCase(serviceLevelMethod))
            {
                switch(serviceLevelMethod)
                {
                    case "Get": doGet(request,response); break;
                    case "Post": doPost(request,response); break;
                }   
                return;
            }
            else
            {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return;
            }
            
        }
        //Check 2/3
        else if(serviceClass.isAnnotationPresent(Get.class) || serviceClass.isAnnotationPresent(Post.class))
        {
            if(serviceClass.isAnnotationPresent(Get.class) )
            {
                serviceClassLevelMethod = "Get";
            }
            if(serviceClass.isAnnotationPresent(Post.class) )
            {
                serviceClassLevelMethod = "Post";
            }
            if(method.equalsIgnoreCase(serviceClassLevelMethod))
            {
                switch(serviceClassLevelMethod)
                {
                    case "Get": doGet(request,response); break;
                    case "Post": doPost(request,response); break;
                }   
                return;
            }
            else
            {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return;
            }
        }
        //Check 3/3
        else
        {
            switch(method)
            {
                case "GET": doGet(request,response); break;
                case "POST": doPost(request,response); break;
                default: response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); return; //eg. if client sends invalid method = "xyz"
            }   
        }        
    } catch (Exception exception) {
        System.out.println("TMWebRock.java service(): "+exception);
    }

}

public void doGet(HttpServletRequest request, HttpServletResponse response)
{
System.out.println("Routed to/ran doGet");
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
Class<?> serviceClass = serviceObject.getServiceClass();
Method service = serviceObject.getService();
String forwardTo = serviceObject.getForwardTo();

System.out.println("Forward to: "+forwardTo);


//part 3
Object obj1 = serviceClass.getDeclaredConstructor().newInstance();
Object returnResponse;
returnResponse = service.invoke(obj1);
if(returnResponse==null) returnResponse = "";	//return empty string if "service" has "void" return type

if(forwardTo!=null)
{
Service serviceForwardObject = servicesMap.get(forwardTo);
if(serviceForwardObject!=null)
{
System.out.println("Entered manual obj2 invocation");
serviceClass = serviceForwardObject.getServiceClass();
service = serviceForwardObject.getService();

Object obj2 = serviceClass.getDeclaredConstructor().newInstance();
returnResponse = service.invoke(obj2);
if(returnResponse==null) returnResponse = "";	//return empty string if "service" has "void" return type
}
else
{
System.out.println("Entered forward to a regular servlet");
RequestDispatcher requestDispatcher;
requestDispatcher = request.getRequestDispatcher(forwardTo);
requestDispatcher.forward(request,response);
}
}

pw.print(returnResponse);
pw.flush();
}catch(Exception e)
{
System.out.println(e);
}
}
public void doPost(HttpServletRequest request, HttpServletResponse response)
{
System.out.println("Routed to/ran doPost");
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
Class<?> serviceClass = serviceObject.getServiceClass();
Method service = serviceObject.getService();
String forwardTo = serviceObject.getForwardTo();

System.out.println("Forward to: "+forwardTo);


//part 3
Object obj1 = serviceClass.getDeclaredConstructor().newInstance();
Object returnResponse;
returnResponse = service.invoke(obj1);
if(returnResponse==null) returnResponse = "";	//return empty string if "service" has "void" return type

if(forwardTo!=null)
{
Service serviceForwardObject = servicesMap.get(forwardTo);
if(serviceForwardObject!=null)
{
System.out.println("Entered manual obj2 invocation");
serviceClass = serviceForwardObject.getServiceClass();
service = serviceForwardObject.getService();

Object obj2 = serviceClass.getDeclaredConstructor().newInstance();
returnResponse = service.invoke(obj2);
if(returnResponse==null) returnResponse = "";	//return empty string if "service" has "void" return type
}
else
{
System.out.println("Entered forward to a regular servlet");
RequestDispatcher requestDispatcher;
requestDispatcher = request.getRequestDispatcher(forwardTo);
requestDispatcher.forward(request,response);
}
}

pw.print(returnResponse);
pw.flush();
}catch(Exception e)
{
System.out.println(e);
}
} //end of post
} //end of class