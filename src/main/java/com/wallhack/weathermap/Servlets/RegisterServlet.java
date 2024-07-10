package com.wallhack.weathermap.Servlets;

import com.wallhack.weathermap.Service.UsersService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {
    private final UsersService usersService = new UsersService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processPostRegisterServlet, req, resp);
    }

    private void processPostRegisterServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);
        String username = req.getParameter("login");
        String password = req.getParameter("password");

        if (isEmpty(username, password)){
            resp.setStatus(400);
            log("Invalid username/password");
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400,"Username and password are required"));
            return;
        }

        if (usersService.getUser(username).isPresent()){
            resp.setStatus(409);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(409,"Username already exists"));
            return;
        }

        usersService.registerUser(username, password);
        var registeredUser  = usersService.getUser(username).orElse(null);

        if (registeredUser != null) {
            resp.setStatus(200);
            MAPPER.writeValue(resp.getWriter(), registeredUser);
        } else {
            resp.setStatus(404);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(404, "Failed to retrieve registered user"));
        }

    }
}
