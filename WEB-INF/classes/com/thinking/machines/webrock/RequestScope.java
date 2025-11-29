package com.thinking.machines.webrock;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RequestScope {
    private HttpServletRequest request;

    public RequestScope(HttpServletRequest request) {
        this.request = request;

        // Before making object using reflection
        // TMWebRock.java will do
        // RequestScope requestScope = new RequestScope(request); // for TMWebRock.java
        // Hence, parameterized constructor
        // Then for object created through reflection:
        // obj1.setRequestScope(requestScope); //for Service.java

    }

    public void setAttribute(String name, Object value) {
        this.request.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return this.request.getAttribute(name);
    }
}