package jwp.controller.userController;

import jwp.dao.UserDao;
import jwp.model.User;
import jwp.util.UserSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserDao userDao;

    @GetMapping("/form")
    public String showCreateForm() {
        return "user/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam String userId,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        User user = new User(userId, password, name, email);
        userDao.insert(user);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("users", userDao.findAll());
        return "user/list";
    }

    @GetMapping("/loginForm")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userDao.findByUserId(userId);
        if (user != null && user.isSameUser(new User(userId, password))) {
            session.setAttribute("user", user);
            return "redirect:/";
        }
        return "redirect:/users/loginFailed";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam String userId,
                                 HttpSession session,
                                 Model model) {
        User sessionUser = (User) session.getAttribute("user");
        User targetUser = userDao.findByUserId(userId);

        if (sessionUser != null && targetUser != null && sessionUser.equals(targetUser)) {
            model.addAttribute("user", targetUser);
            return "user/updateForm";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@RequestParam String userId,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String email) {
        User modifiedUser = new User(userId, password, name, email);
        userDao.update(modifiedUser);
        return "redirect:/users/list";
    }
}
