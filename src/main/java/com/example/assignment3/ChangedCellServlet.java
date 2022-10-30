package com.example.assignment3;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@WebServlet(name = "ChangedCellServlet", value = "/ChangedCellServlet")
public class ChangedCellServlet extends HttpServlet {

    SSEngine spreadsheet = SSEngine.getSSEngine();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{

            ServletContext ctx=getServletContext();
            SSEngine spreadsheet = (SSEngine)ctx.getAttribute("sEngine");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(spreadsheet.cellToJsonStr());
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            ServletContext ctx = getServletContext();
            SSEngine spreadsheet = (SSEngine) ctx.getAttribute("sEngine");
            String id = request.getParameter("id");
            String formula = request.getParameter("formula");

            //System.out.println(id + " " + formula);

            Set<Cell> modified = spreadsheet.modifyCell(id,formula);
            //Transform the modified cells to a json response
            String toRet = spreadsheet.cellToJsonStr();
            //System.out.println(toRet);

            ctx.setAttribute("sEngine", spreadsheet);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(toRet);

        }catch (Exception e){e.printStackTrace();}


     }

     public String toJson(Set<Cell> a){
         String toRet = "{";
         Cell[] modifiedArray =  new Cell[a.size()];
         a.toArray(modifiedArray);
         for(int i = 0; i < a.size()-1; i++){
             toRet += "\"" + modifiedArray[i].id + "\"" + ":" + modifiedArray[i].value + ",";
         }
         toRet += "\"" + modifiedArray[modifiedArray.length-1].id + "\"" + ":"  + modifiedArray[modifiedArray.length-1].value;
         toRet += "}";

         return toRet;
     }



}
