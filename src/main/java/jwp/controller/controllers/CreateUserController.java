package jwp.controller.controllers;

import core.db.MemoryUserRepository;
import jwp.controller.Controller;
import jwp.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUserController implements Controller {
    @Override
    public String handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        if (userId == null || userId.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            req.setAttribute("errorMessage", "모든 항목을 입력해 주세요.");
            return "redirect:/user/form"; // 실패 시 form으로,
        }

        MemoryUserRepository repo = MemoryUserRepository.getInstance();
        if (repo.findUserById(userId.trim()) != null) {
            req.setAttribute("errorMessage", "이미 사용 중인 ID입니다.");
            return "redirect:/user/form"; // 실패 시 form으로,
        }
        User user = new User(userId.trim(), password.trim(), name.trim(), email.trim());
        repo.addUser(user);

        return "redirect:/";
    }
}
