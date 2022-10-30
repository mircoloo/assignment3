package com.example.assignment3;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {

    SSEngine spreadsheetEngine = SSEngine.getSSEngine();
    public void contextInitialized(ServletContextEvent event){

        try{

            System.out.println("Initialized context");
            ServletContext ctx=event.getServletContext();
            ctx.setAttribute("sEngine", spreadsheetEngine);

        }catch(Exception e){e.printStackTrace();}
    }

    public void contextDestroyed(ServletContextEvent event){}



}
