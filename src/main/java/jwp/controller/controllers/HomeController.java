package jwp.controller.controllers;

import jwp.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HomeController implements Controller {
    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        return "home";
    }
}
