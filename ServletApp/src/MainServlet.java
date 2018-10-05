import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1 L;
    private Map < String, String > pairs = new HashMap < String, String > ();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE html>\n<form action=MainServlet method=POST>Key:<input name=key>Value:<input name=value><input type=submit value=Submit></form>");
        writer.println("<table><tr><th>Key</th><th>Value</th></tr>");
        for (Map.Entry < String, String > entry: pairs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            writer.println("<tr><td>" + key + "</td><td>" +
                value + "</td></tr>");
        }

        writer.println("</table>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        pairs.put(key, value);
        doGet(request, response);
    }

}
