package com.example.assignment3;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "CheckTimestampServlet", value = "/CheckTimestampServlet")
public class CheckTimestampServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Richiesta Get");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        SSEngine spreadsheet = (SSEngine) ctx.getAttribute("sEngine");

        String objarray = request.getParameter("objarray");
        HashMap<String, Long> timestamps = new HashMap<String, Long>();

        //Gson gson = new Gson();
        // System.out.println(gson.toJson(objarray));
        /*try{
            System.out.println(objarray);
            Gson gson = new Gson();
            String personJson = gson.toJson(objarray);
            System.out.println("GSONIED:" + personJson);
        }catch (Exception e){
            System.out.println(e);
        }*/



        if(spreadsheet.isTimestampedDifferent( timestamps )){
            //System.out.println(objarray);


        }
        String toRet = spreadsheet.cellToJsonStr();
        ctx.setAttribute("sEngine", spreadsheet);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(toRet);



    }
}
