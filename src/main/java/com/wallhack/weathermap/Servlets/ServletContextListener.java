package com.wallhack.weathermap.Servlets;

import com.wallhack.weathermap.Service.SessionsService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;

import java.util.Timer;
import java.util.TimerTask;

import static com.wallhack.weathermap.utils.ExtraUtils.MAPPER;

@WebListener
public class ServletContextListener implements jakarta.servlet.ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MAPPER.findAndRegisterModules();

        SessionsService sessionsService = new SessionsService();
        Timer timer = new Timer(true);

        TimerTask deleteExpiredSessionsTask = new TimerTask() {
            @Override
            public void run() {
                sessionsService.deleteExpiredSessions();
            }
        };

        timer.scheduleAtFixedRate(deleteExpiredSessionsTask, 0, 60 * 1000);
    }



}
