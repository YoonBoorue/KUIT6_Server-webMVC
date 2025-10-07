package jwp.controller.controllers;

import jwp.controller.Controller;
import jwp.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UpdateUserFormController implements Controller {
    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User)session.getAttribute("user") : null;

        String targetId = req.getParameter("userId");

        if(user != null && user.getUserId().equals(targetId)) {
            req.setAttribute("user", user);
            return "user/updateForm";
        } else {
            return "redirect:/";
        }
    }
}

