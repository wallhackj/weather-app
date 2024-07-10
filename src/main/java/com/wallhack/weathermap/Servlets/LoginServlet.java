package com.wallhack.weathermap.Servlets;

import com.wallhack.weathermap.Model.SessionsPOJO;
import com.wallhack.weathermap.Service.SessionsService;
import com.wallhack.weathermap.Service.UsersService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

import static com.wallhack.weathermap.Service.SessionsService.getWhenExpiersSessionTimestamp;
import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private final UsersService usersService = new UsersService();
    private final SessionsService sessionsService = new SessionsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
       doReq(this::processGetLoginServlet, req, resp);
    }

    private void processGetLoginServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

//      /login?login=******.com&password=*****
        String username = req.getParameter("login");
        String password = req.getParameter("password");

        if (isEmpty(username, password)){
            resp.setStatus(400);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400,"Username and password are required"));
            return;
        }

        var user = usersService.getUser(username);

        if (user.isPresent()){
            if (user.get().getPassword().equals(password)){
                setAndSaveSessionCookie(req, resp, user.get().getId());

                resp.setStatus(200);
                MAPPER.writeValue(resp.getWriter(), user.get());
                sessionsService.saveSession(new SessionsPOJO(getWhenExpiersSessionTimestamp() ,user.get()));
            }else {
                resp.setStatus(403);
                MAPPER.writeValue(resp.getWriter(), new ErrorResponse(403, "Invalid password"));
            }
        }else {
            resp.setStatus(404);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(404,"User not found"));
        }
    }

    private void setAndSaveSessionCookie(HttpServletRequest req, HttpServletResponse resp , long userId) {
        HttpSession session = req.getSession();
        session.setAttribute("userId", userId);
        Cookie sessionCookie = new Cookie("sessionId", UUID.randomUUID().toString());
        sessionCookie.setMaxAge(30 * 60);
        resp.addCookie(sessionCookie);
    }

}
