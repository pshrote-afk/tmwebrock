package com.thinking.machines.webrock.pojo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class SessionScope {
    private HttpSession session;

    public SessionScope(HttpSession session) {
        this.session = session;

        // Before making object using reflection
        // TMWebRock.java will do
        // SessionScope sessionScope = new SessionScope(session); // for TMWebRock.java
        // Hence, parameterized constructor
        // Then for object created through reflection:
        // obj1.setSessionScope(sessionScope); //for Service.java

    }

    public void setAttribute(String name, Object value) {
        this.session.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return this.session.getAttribute(name);
    }
}