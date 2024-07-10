package com.wallhack.weathermap.Servlets;

import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.Service.SessionsService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/logout")
public class LogoutServlet extends HttpServlet {
    private final SessionsService sessionsService = new SessionsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processGetLogoutServlet, req, resp);
    }

    private void processGetLogoutServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

        HttpSession session = req.getSession(false);
        if (session != null) {
            long userId = (long) session.getAttribute("userId");
            SessionsPOJO sessionsPOJO = sessionsService.getSessionByUserId(userId).orElse(null);
            if (sessionsPOJO != null){
                sessionsService.deleteSession(sessionsPOJO.getId());
                session.invalidate();
            }else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                MAPPER.writeValue(resp.getWriter(), new ErrorResponse(404, "Session not found"));
            }
        }

        Cookie cookies = new Cookie("sessionId",null);
        cookies.setPath("/");
        cookies.setMaxAge(0);
        resp.addCookie(cookies);

        resp.setStatus(200);
        MAPPER.writeValue(resp.getWriter(), new ErrorResponse(200, "Logout successful"));
    }
}
