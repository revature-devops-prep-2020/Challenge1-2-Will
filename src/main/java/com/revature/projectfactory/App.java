package com.revature.projectfactory;

import java.io.File;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class App {

    public static void main(String[] args) {
        Tomcat server = new Tomcat();
        server.setPort(80);
        server.setBaseDir(new File("target/tomcat").getAbsolutePath());
        server.getConnector();
        server.addWebapp("", new File("src/main/static").getAbsolutePath());
        try {
            server.start();
            server.getServer().await();
        } catch (LifecycleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println();
        }
    }
}