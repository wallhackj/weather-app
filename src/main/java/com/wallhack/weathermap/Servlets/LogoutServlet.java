package com.wallhack.weathermap.Servlets;

import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processGetLogoutServlet, req, resp);
    }

    private void processGetLogoutServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookies = new Cookie("sessionId",null);
        cookies.setPath("/");
        cookies.setMaxAge(0);
        resp.addCookie(cookies);

        resp.setStatus(200);
        MAPPER.writeValue(resp.getWriter(), new ErrorResponse(200, "Logout successful"));
    }
}
