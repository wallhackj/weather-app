package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Service.UsersService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private final UsersService usersService = new UsersService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processGetLoginServlet, req, resp);
    }

    private void processGetLoginServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);
//      /login?login=******.com&password=*****
        String username = req.getParameter("login");
        String password = req.getParameter("password");

        var user = usersService.getUser(username);

        if (user.isPresent()){
            if (user.get().getPassword().equals(password)){
                resp.setStatus(200);
                mapper.writeValue(resp.getWriter(), user.get());
            }else {
                resp.setStatus(403);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(403, "Invalid password"));
            }
        }else {
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(404,"User not found"));
        }

    }
}
