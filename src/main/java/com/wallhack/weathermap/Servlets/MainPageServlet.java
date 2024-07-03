package com.wallhack.weathermap.Servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/index")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processGetMainPageServlet, req, resp);
    }

    private void processGetMainPageServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

        
    }
}
