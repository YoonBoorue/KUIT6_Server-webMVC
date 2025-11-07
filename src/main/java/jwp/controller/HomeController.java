package jwp.controller;

import jwp.dao.QuestionDao;
import jwp.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QuestionDao questionDao;

    @GetMapping("/")
    public String home(Model model) {
        List<Question> questions = questionDao.findAll();
        model.addAttribute("questions", questions);
        return "home";   // /WEB-INF/jsp/home.jsp 이런 식으로 뷰리졸버가 찾게
    }
}
