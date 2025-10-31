package jwp.controller;

import jwp.dao.QuestionDao;
import jwp.model.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class HomeController implements Controller {
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Question> questions = questionDao.findAll();
        req.setAttribute("questions", questions);
        return "home";
    }
}
