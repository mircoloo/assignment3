package com.example.assignment3;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "IndexServlet", value = "/")
public class IndexServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            ServletContext ctx = getServletContext();
            SSEngine spreadsheet = (SSEngine) ctx.getAttribute("sEngine");
            //System.out.println(spreadsheet.cellMap);
        }catch (Exception e){e.printStackTrace();}

        //request.setAttribute("");
        request.getRequestDispatcher("homePage.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("called index post");
    }
}
