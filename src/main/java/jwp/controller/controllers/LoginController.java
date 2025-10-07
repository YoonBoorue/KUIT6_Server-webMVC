package jwp.controller.controllers;

import core.db.MemoryUserRepository;
import jwp.controller.Controller;
import jwp.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController implements Controller {

    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        User user = MemoryUserRepository.getInstance().findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("loginError", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "redirect:/user/loginFailed";
        }

        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) oldSession.invalidate();
        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("user", user);

        return "redirect:/";
    }
}
