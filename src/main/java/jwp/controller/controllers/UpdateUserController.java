package jwp.controller.controllers;

import core.db.MemoryUserRepository;
import jwp.controller.Controller;
import jwp.dao.UserDao;
import jwp.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UpdateUserController implements Controller {
    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            return "redirect:/";
        }

        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        User updatedUser = new User(currentUser.getUserId(), password, name, email);

        UserDao userDao = new UserDao();
        userDao.update(updatedUser);
        session.setAttribute("user", updatedUser);

        return "redirect:/user/list";
    }
}
