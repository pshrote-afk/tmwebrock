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

public class TMWebRock extends HttpServlet {

    private void autoWire(ArrayList<AutoWiredBundle> autoWiredBundles, Object obj1, HttpServletRequest request)
    {
        if(autoWiredBundles==null) return;      // Meaning: no property of this class has @AutoWired annotation
        Field field; 
        String autoWiredName;
        String setterMethod;        // String which will be passed to search for setter method
        Method method;      // actual method which will be invoked
        int indexToCapitalize = 3;
        for(AutoWiredBundle bundle : autoWiredBundles)
        {
            field = bundle.getField();
            autoWiredName = bundle.getAutoWiredName();
            Class<?> fieldType = field.getType();

            Object found = null;
            HttpSession session = request.getSession();
            ServletContext servletContext = getServletContext();

            // search 3 scopes in ascending order
            while(true) // run loop only once
            {
                found = request.getAttribute(autoWiredName);
                if(found!=null && fieldType.isInstance(found))      // 2nd condition is necessary, because we might find "xyz" of Bulb type in request scope. But we are looking for "xyz" of Student type. Since each scope can have a unique key-value pair, we move to the next scope.
                {  
                    break;
                }

                found = session.getAttribute(autoWiredName);
                if(found!=null && fieldType.isInstance(found))     
                {  
                    break;
                }

                found = servletContext.getAttribute(autoWiredName);
                if(found!=null && fieldType.isInstance(found))      
                {  
                    break;
                }

                break;
            }
            // at this point either "found" is null, or has some value. If null, we will set value regardless, print [Framework Error], and skip to next iteration. 
            if(found==null) 
            {
                System.out.println("[Framework Error] " + "Property " + "\"" + field.getName() + "\"" + " of " + field.getDeclaringClass() + " is set as null, as @AutoWired(name=\""+ autoWiredName +"\") returned null after searching request, session and application scope\n");                            
                continue;
            }

            setterMethod = "set" + field.getName();     // setterMethod = "setstudent";
            setterMethod = setterMethod.substring(0, indexToCapitalize) + Character.toUpperCase(setterMethod.charAt(indexToCapitalize)) + setterMethod.substring(indexToCapitalize + 1);  // setterMethod = "setStudent";
            try {                            
            method = obj1.getClass().getMethod(setterMethod, fieldType);     // This line is prone to error. If framework user does not write setter in camel case.
            method.invoke(obj1,found);
            } catch (Exception exception) 
            {
                System.out.println("[Framework Error] " + exception);
            }

        }

    }

    private void injectDependencies(Service serviceObject, Object obj1, HttpServletRequest request)
    {
        if(serviceObject.getInjectApplicationScope()==true)
        {
            ApplicationScope applicationScope = new ApplicationScope(getServletContext());
            try
            {
                Method method = obj1.getClass().getMethod("setApplicationScope",ApplicationScope.class);
                method.invoke(obj1,applicationScope);
            }catch(Exception exception)
            {
                System.out.println(exception);
            }
            
        }
        if(serviceObject.getInjectSessionScope()==true)
        {
            SessionScope sessionScope = new SessionScope(request.getSession());
            try
            {
                Method method = obj1.getClass().getMethod("setSessionScope",SessionScope.class);
                method.invoke(obj1,sessionScope);
            }catch(Exception exception)
            {
                System.out.println(exception);
            }
            
        }
        if(serviceObject.getInjectRequestScope()==true)
        {
            RequestScope requestScope = new RequestScope(request);
            try
            {
                Method method = obj1.getClass().getMethod("setRequestScope",RequestScope.class);
                method.invoke(obj1,requestScope);
            }catch(Exception exception)
            {
                System.out.println(exception);
            }
            
        }
        if(serviceObject.getInjectApplicationDirectory()==true)
        {
            try
            {
                String realPath = getServletContext().getRealPath("/");
                ApplicationDirectory applicationDirectory = new ApplicationDirectory(new File(realPath));
                Method method = obj1.getClass().getMethod("setApplicationDirectory",ApplicationDirectory.class);
                method.invoke(obj1,applicationDirectory);
            }catch(Exception exception)
            {
                System.out.println(exception);
            }
            
        }

    }
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            String uri = request.getRequestURI(); // eg. "/tmwebrock/schoolservice/student/update"
            String parts[] = uri.split("schoolservice");
            String clientPath = parts[1]; // eg. "/student/update"
            System.out.println("uri: " + request.getRequestURI());
            System.out.println("Client path: " + clientPath);

            ServletContext servletContext = getServletContext();
            Map<String, Service> servicesMap = (HashMap) servletContext.getAttribute("ServicesMap");

            Service serviceObject = servicesMap.get(clientPath);
            Class<?> serviceClass = serviceObject.getServiceClass();
            Method service = serviceObject.getService();

            String method = request.getMethod().toUpperCase();
            String serviceLevelMethod = "";
            String serviceClassLevelMethod = "";

            // method-level get/post will get precedence over class-level get/post.
            // eg. class has get annotation, but its method has post annotation.

            // Check 1/3
            if (service.isAnnotationPresent(Get.class) || service.isAnnotationPresent(Post.class)) {
                if (service.isAnnotationPresent(Get.class)) {
                    serviceLevelMethod = "Get";
                }
                if (service.isAnnotationPresent(Post.class)) {
                    serviceLevelMethod = "Post";
                }
                if (method.equalsIgnoreCase(serviceLevelMethod)) {
                    switch (serviceLevelMethod) {
                        case "Get":
                            doGet(request, response);
                            break;
                        case "Post":
                            doPost(request, response);
                            break;
                    }
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }

            }
            // Check 2/3
            else if (serviceClass.isAnnotationPresent(Get.class) || serviceClass.isAnnotationPresent(Post.class)) {
                if (serviceClass.isAnnotationPresent(Get.class)) {
                    serviceClassLevelMethod = "Get";
                }
                if (serviceClass.isAnnotationPresent(Post.class)) {
                    serviceClassLevelMethod = "Post";
                }
                if (method.equalsIgnoreCase(serviceClassLevelMethod)) {
                    switch (serviceClassLevelMethod) {
                        case "Get":
                            doGet(request, response);
                            break;
                        case "Post":
                            doPost(request, response);
                            break;
                    }
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }
            }
            // Check 3/3
            else {
                switch (method) {
                    case "GET":
                        doGet(request, response);
                        break;
                    case "POST":
                        doPost(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return; // eg. if client sends invalid method = "xyz"
                }
            }
        } catch (Exception exception) {
            System.out.println("TMWebRock.java service(): " + exception);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Routed to doGet");
        try {
            PrintWriter pw = response.getWriter();
            response.setContentType("text/plain");

            // part 1
            String uri = request.getRequestURI(); // eg. "/tmwebrock/schoolservice/student/update"
            String parts[] = uri.split("schoolservice");
            String clientPath = parts[1]; // eg. "/student/update"
            System.out.println("uri: " + request.getRequestURI());
            System.out.println("Client path: " + clientPath);

            // part 2
            ServletContext servletContext = getServletContext();
            Map<String, Service> servicesMap = (HashMap) servletContext.getAttribute("ServicesMap");

            Service serviceObject = servicesMap.get(clientPath);
            Class<?> serviceClass = serviceObject.getServiceClass();
            Method service = serviceObject.getService();
            String forwardTo = serviceObject.getForwardTo();

            System.out.println("Forward to: " + forwardTo);

            // part 3
            Object obj1 = serviceClass.getDeclaredConstructor().newInstance();
            
            // inject dependencies
            injectDependencies(serviceObject,obj1,request);

            // fulfill @AutoWired
            ArrayList<AutoWiredBundle> autoWiredBundles = serviceObject.getAutoWiredBundles();
            autoWire(autoWiredBundles, obj1, request);
            
            Object returnResponse;
            returnResponse = service.invoke(obj1);
            if (returnResponse == null)
                returnResponse = ""; // return empty string if "service" has "void" return type

            if (forwardTo != null) {
                Service serviceForwardObject = servicesMap.get(forwardTo);
                if (serviceForwardObject != null) {
                    System.out.println("Entered manual obj2 invocation");
                    serviceClass = serviceForwardObject.getServiceClass();
                    service = serviceForwardObject.getService();

                    Object obj2 = serviceClass.getDeclaredConstructor().newInstance();
            
                    injectDependencies(serviceForwardObject,obj2,request);
                    autoWiredBundles = serviceForwardObject.getAutoWiredBundles();
                    autoWire(autoWiredBundles, obj2, request);
            
                    returnResponse = service.invoke(obj2);
                    if (returnResponse == null)
                        returnResponse = ""; // return empty string if "service" has "void" return type
                } else {
                    System.out.println("Entered forward to a regular servlet");
                    RequestDispatcher requestDispatcher;
                    requestDispatcher = request.getRequestDispatcher(forwardTo);
                    requestDispatcher.forward(request, response);
                }
            }

            pw.print(returnResponse);
            pw.flush();
        } catch (Exception e) {
            System.out.println("[Framework Error] " + e.getCause());
            StackTraceElement stackTraceElement = e.getCause().getStackTrace()[0];
            System.out.println(stackTraceElement.getFileName() + " : " + stackTraceElement.getLineNumber());
            System.out.print("\n");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Routed to doPost");
        try {
            PrintWriter pw = response.getWriter();
            response.setContentType("text/plain");

            // part 1
            String uri = request.getRequestURI(); // eg. "/tmwebrock/schoolservice/student/update"
            String parts[] = uri.split("schoolservice");
            String clientPath = parts[1]; // eg. "/student/update"
            System.out.println("uri: " + request.getRequestURI());
            System.out.println("Client path: " + clientPath);

            // part 2
            ServletContext servletContext = getServletContext();
            Map<String, Service> servicesMap = (HashMap) servletContext.getAttribute("ServicesMap");

            Service serviceObject = servicesMap.get(clientPath);
            Class<?> serviceClass = serviceObject.getServiceClass();
            Method service = serviceObject.getService();
            String forwardTo = serviceObject.getForwardTo();

            System.out.println("Forward to: " + forwardTo);

            // part 3
            Object obj1 = serviceClass.getDeclaredConstructor().newInstance();
           
            // inject dependencies
            injectDependencies(serviceObject,obj1,request);

            // fulfill @AutoWired
            ArrayList<AutoWiredBundle> autoWiredBundles = serviceObject.getAutoWiredBundles();
            autoWire(autoWiredBundles, obj1, request);

            Object returnResponse;
            returnResponse = service.invoke(obj1);
            if (returnResponse == null)
                returnResponse = ""; // return empty string if "service" has "void" return type

            if (forwardTo != null) {
                Service serviceForwardObject = servicesMap.get(forwardTo);
                if (serviceForwardObject != null) {
                    System.out.println("Entered manual obj2 invocation");
                    serviceClass = serviceForwardObject.getServiceClass();
                    service = serviceForwardObject.getService();

                    Object obj2 = serviceClass.getDeclaredConstructor().newInstance();
                    injectDependencies(serviceForwardObject,obj2,request);
                    autoWiredBundles = serviceForwardObject.getAutoWiredBundles();
                    autoWire(autoWiredBundles, obj2, request);

                    returnResponse = service.invoke(obj2);
                    if (returnResponse == null)
                        returnResponse = ""; // return empty string if "service" has "void" return type
                } else {
                    System.out.println("Entered forward to a regular servlet");
                    RequestDispatcher requestDispatcher;
                    requestDispatcher = request.getRequestDispatcher(forwardTo);
                    requestDispatcher.forward(request, response);
                }
            }

            pw.print(returnResponse);
            pw.flush();
        } catch (Exception e) {
            System.out.println("[Framework Error] " + e.getCause());
            StackTraceElement stackTraceElement = e.getCause().getStackTrace()[0];
            System.out.println(stackTraceElement.getFileName() + " : " + stackTraceElement.getLineNumber());
            System.out.print("\n");
        }
    } // end of post
} // end of class