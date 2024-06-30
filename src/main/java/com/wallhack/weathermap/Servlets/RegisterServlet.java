package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Service.UsersService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/register")
@Slf4j
public class RegisterServlet extends HttpServlet {
    private final UsersService usersService = new UsersService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processPostRegisterServlet(req, resp);
    }

    private void processPostRegisterServlet(HttpServletRequest req, HttpServletResponse resp) {
        prepareResponse(resp);
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {

            if (isEmpty(username, password)){
                resp.setStatus(400);
                log("Invalid username/password");
                mapper.writeValue(resp.getWriter(), new ErrorResponse(400,"Username and password are required"));
            }

            if (usersService.getUser(username).isEmpty()){
                log("Username already exists");
                resp.setStatus(409);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(409,"Username already exists"));
            }else usersService.registerUser(username, password);
        }catch (Exception e){
            resp.setStatus(500);
            handleResponseError(resp, log, mapper, e, 500, "Internal Server Error");
        }
    }
}
