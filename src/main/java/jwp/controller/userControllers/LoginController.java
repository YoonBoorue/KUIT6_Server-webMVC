package jwp.controller.userControllers;

import jwp.controller.Controller;
import jwp.dao.UserDao;
import jwp.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        UserDao userDao = new UserDao();
        User user = userDao.findByUserId(userId);
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
