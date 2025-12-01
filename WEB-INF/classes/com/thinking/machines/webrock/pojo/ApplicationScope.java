package com.thinking.machines.webrock.pojo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ApplicationScope {
    private ServletContext servletContext;

    public ApplicationScope(ServletContext servletContext) {
        this.servletContext = servletContext;

        // Before making object using reflection
        // TMWebRock.java will do
        // ApplicationScope applicationScope = new ApplicationScope(servletContext); // for TMWebRock.java
        // Hence, parameterized constructor
        // Then for object created through reflection:
        // obj1.setApplicationScope(applicationScope); //for Service.java

    }

    public void setAttribute(String name, Object value) {
        this.servletContext.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return this.servletContext.getAttribute(name);
    }
}