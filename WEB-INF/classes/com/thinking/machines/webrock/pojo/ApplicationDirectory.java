package com.thinking.machines.webrock.pojo;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ApplicationDirectory {

    private File directory;

    public ApplicationDirectory(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return this.directory;
    }

}