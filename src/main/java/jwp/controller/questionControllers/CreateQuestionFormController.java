package jwp.controller.questionControllers;

import jwp.controller.Controller;
import jwp.dao.QuestionDao;
import jwp.model.Question;
import jwp.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateQuestionFormController implements Controller {
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!"POST".equalsIgnoreCase(req.getMethod())) {
            return "redirect:/qna/form";
        }

        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 작성자는 세션에서만 신뢰 (보안)
        String writer = loginUser.getUserId();

        String title = req.getParameter("title");
        String contents = req.getParameter("contents");

        if (title == null || title.isBlank() || contents == null || contents.isBlank()) {
            req.setAttribute("errorMessage", "모든 항목을 입력해 주세요.");
            req.setAttribute("prevTitle", title);
            req.setAttribute("prevContents", contents);
            return "redirect:/qna/form";
        }

        Question question = new Question(writer, title, contents);
        Question created = questionDao.insert(question);

        return "redirect:/qna/show?questionId=" + created.getQuestionId();
    }
}
