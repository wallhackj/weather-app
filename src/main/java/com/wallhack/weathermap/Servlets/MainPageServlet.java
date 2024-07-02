package com.wallhack.weathermap.Servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.headerSetter;
import static com.wallhack.weathermap.utils.ExtraUtils.prepareResponse;

@WebServlet(value = "/index")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        headerSetter(resp);

        try {
            processGetMainPageServlet(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void processGetMainPageServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

        
    }
}
