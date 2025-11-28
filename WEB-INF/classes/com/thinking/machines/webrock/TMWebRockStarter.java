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

    private void sortOnStartupList(onStartUpList)
    {

    }
    private void executeOnStartupList(onStartupList)
    {
       
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
                        service.setService(m1);
                        service.setRunOnStartup(true);
                        service.setPriority(m1.getAnnotation(OnStartup.class).Priority());      // we will sort on the basis of this "priority" property
                        onStartupList.add(service); 
                    }

                }
            }
            servletContext.setAttribute("ServicesMap", servicesMap);

            sortOnStartupList(onStartUpList);
            executeOnStartupList(onStartupList);  

            // testing
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

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    } // end of init()
} // end of class