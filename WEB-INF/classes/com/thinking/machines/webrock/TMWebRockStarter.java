package com.thinking.machines.webrock;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotations.*;

public class TMWebRockStarter extends HttpServlet {

    private void sortOnStartupList(List<Service> onStartupList)
    {
        Comparator<Service> priorityComparator = new Comparator<>(){
            public int compare(Service service1, Service service2)
            {
                return service1.getPriority() - service2.getPriority();
            }
        };  // anonymous class created
        Collections.sort(onStartupList,priorityComparator);
    }

    private void executeOnStartupList(List<Service> onStartupList)
    {
        try
        {
        System.out.println("onStartupList size: " + onStartupList.size());
        while(onStartupList.isEmpty()==false)
        {
            Service serviceObject = onStartupList.remove(0);
            
            System.out.println("\n" + serviceObject.getPriority());

            Class<?> serviceClass = serviceObject.getServiceClass();
            Method service = serviceObject.getService();
            Object obj1 = serviceClass.getDeclaredConstructor().newInstance();

            if(serviceObject.getInjectApplicationScope()==true)
            {
                // inject application scope
                ApplicationScope applicationScope = new ApplicationScope(getServletContext());
                Method method = obj1.getClass().getMethod("setApplicationScope",ApplicationScope.class);
                method.invoke(obj1,applicationScope);
            }
            service.invoke(obj1);
        }
        System.out.println("onStartupList complete");
        }catch(Exception exception)
        {
            System.out.println(exception);
        }
    }

    private void scanRecursive(String pkg, Set<Class<?>> classes) throws URISyntaxException, ClassNotFoundException {
        String path = pkg.replace(".", "/"); // eg. convert bobby.test to bobby/test
        URL url = Thread.currentThread().getContextClassLoader().getResource(path); // Ask Tomcat’s classloader: “Give
                                                                                    // me the folder on classpath that
                                                                                    // matches this path.” eg.
                                                                                    // bobby/util to
                                                                                    // /WEB-INF/classes/bobby/util
        File dir = new File(url.toURI()); // convert URL to File object

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                scanRecursive(pkg + "." + f.getName(), classes);
                continue;
            }
            if (f.getName().endsWith(".class")) {
                String className = f.getName().replace(".class", "");
                // important testing outputs
                System.out.println("Class name: " + className);
                System.out.println("Class name w/ package: " + pkg + "." + className);

                Class<?> c1 = Class.forName(pkg + "." + className);
                classes.add(c1);
            }
        }
    }

    public void init() {
        System.out.println(
                "TMWebRockStarter\nTMWebRockStarter\nTMWebRockStarter\nTMWebRockStarter\nTMWebRockStarter\nTMWebRockStarter");
        try {
            WebRockModel webRockModel = new WebRockModel();
            Map<String, Service> servicesMap = webRockModel.getServicesMap();

            ServletContext servletContext = getServletContext();
            String servicePackagePrefix = servletContext.getInitParameter("SERVICE_PACKAGE_PREFIX");

            System.out.println(servicePackagePrefix);

            Set<Class<?>> classes = new HashSet<>();
            this.scanRecursive(servicePackagePrefix, classes);
            // all classes under "bobby" loaded into "classes"

            // now iterate through each class' methods
            List<Service> onStartupList = new ArrayList<>();
            Service service;
            String classPathAnnotation;
            String methodPathAnnotation;
            for (Class<?> c1 : classes) {
                for (Method m1 : c1.getDeclaredMethods()) {
                    service = new Service();
                    service.setServiceClass(c1);

                    // check for injection scopes
                    if(c1.isAnnotationPresent(InjectApplicationScope.class)) service.setInjectApplicationScope(true);
                    if(c1.isAnnotationPresent(InjectSessionScope.class)) service.setInjectSessionScope(true);
                    if(c1.isAnnotationPresent(InjectRequestScope.class)) service.setInjectRequestScope(true);
                    
                    if(c1.isAnnotationPresent(Path.class))
                    {
                        if (m1.isAnnotationPresent(Path.class)) {
                            classPathAnnotation = c1.getAnnotation(Path.class).value();
                            methodPathAnnotation = m1.getAnnotation(Path.class).value();

                            
                            String fullPath = classPathAnnotation + methodPathAnnotation;
                            service.setPath(fullPath);
                            service.setService(m1);
                            if (m1.isAnnotationPresent(Forward.class))
                                service.setForwardTo(m1.getAnnotation(Forward.class).value());

                            servicesMap.put(fullPath, service);
                        }
                    }
                    
                    if(m1.isAnnotationPresent(OnStartup.class))
                    {
                        if(m1.getReturnType()!=void.class || m1.getParameterCount()!=0)
                        {
                            System.out.println("[Framework Error] " + m1.getName() + " with priority " + m1.getAnnotation(OnStartup.class).Priority() + " of class " + c1.getName() + " should have return type \"void\" and 0 paramters to enable @OnStartup\n");
                            continue;
                        }
                        service.setService(m1);
                        service.setRunOnStart(true);
                        service.setPriority(m1.getAnnotation(OnStartup.class).Priority());      // we will sort on the basis of this "priority" property
                        onStartupList.add(service); 
                    }

                }
            }
            servletContext.setAttribute("ServicesMap", servicesMap);

            sortOnStartupList(onStartupList);
            executeOnStartupList(onStartupList);  

            // testing
            /*
            Service service1;
            Iterator<Map.Entry<String, Service>> iterator1 = servicesMap.entrySet().iterator();
            System.out.println("");
            while (iterator1.hasNext()) {
                Map.Entry<String, Service> entry1 = iterator1.next();
                System.out.println("Key: " + entry1.getKey());
                service = entry1.getValue();
                System.out.println(service.getServiceClass());
                System.out.println(service.getPath());
                System.out.println(service.getService());
                System.out.println("Forward to: " + service.getForwardTo());
                System.out.println("");

            }
            */

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    } // end of init()
} // end of class