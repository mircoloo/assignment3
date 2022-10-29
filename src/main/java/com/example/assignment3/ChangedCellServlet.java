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
        //request.getRequestDispatcher("index.jsp").forward(request, response);
        System.out.println("changed cell request");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("changed cell request post");
        String id = request.getParameter("id");
        String formula = request.getParameter("formula");

        //System.out.println(id + " " + formula);

        Set<Cell> modified = spreadsheet.modifyCellAndPrint(id,formula);

        //JSONbject js = new JSONbject();
        HashMap<String, String> json = new HashMap<String, String>();
        String toRet = "{";
        for(Cell c : modified){
            System.out.println(c.id + " " + c.value);
            String jsonString = new JSONObject();
            toRet += c.id + ":" + c.value + ",";
                    jsonString.put("JSON1", "Hello World!")
                    .put("JSON2", "Hello my World!")
                    .put("JSON3", new JSONObject().put("key1", "value1"))
                    .toString();
        }
        toRet += "}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(toRet);
     }
}
