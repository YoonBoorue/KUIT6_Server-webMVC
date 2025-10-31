package jwp.controller.questionControllers;

import jwp.controller.Controller;
import jwp.dao.QuestionDao;
import jwp.model.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowController implements Controller {
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("questionId");
        if (idParam == null || idParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "questionId is required");
            return null;
        }
        long qid = Long.parseLong(idParam);
        Question targetQuestion = questionDao.findByQuestionId(qid);

        if (targetQuestion == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Question not found");
            return null;
        }
        req.setAttribute("question",targetQuestion);
        return "qna/show";
    }
}
