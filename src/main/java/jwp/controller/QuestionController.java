package jwp.controller;

import jwp.dao.QuestionDao;
import jwp.model.Question;
import jwp.model.User;
import jwp.util.UserSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QuestionController {

    private final QuestionDao questionDao;

    /** (1) 질문 작성 폼 */
    @GetMapping("/form")
    public String showCreateForm(HttpSession session) {
        if (UserSessionUtils.isLogined(session)) {
            return "qna/form";          // /WEB-INF/jsp/qna/form.jsp 이런 식으로 매핑될 것
        }
        return "redirect:/user/loginForm";
    }

    /** (2) 질문 등록 */
    @PostMapping("/create")
    public String create(@RequestParam String writer,
                         @RequestParam String title,
                         @RequestParam String contents) {
        Question question = new Question(writer, title, contents, 0);
        Question savedQuestion = questionDao.insert(question);
        System.out.println("saved question id = " + savedQuestion.getQuestionId());
        return "redirect:/";
    }

    /** (3) 질문 보기 */
    @GetMapping("/show")
    public String show(@RequestParam("questionId") int questionId,
                       Model model) {
        Question question = questionDao.findByQuestionId(questionId);
        model.addAttribute("question", question);
        return "qna/show";
    }

    /** (4) 수정 폼 */
    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("questionId") int questionId,
                                 HttpSession session,
                                 Model model) {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/user/loginForm";
        }
        Question question = questionDao.findByQuestionId(questionId);
        User user = UserSessionUtils.getUserFromSession(session);
        if (!question.isSameUser(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        model.addAttribute("question", question);
        return "qna/updateForm";
    }

    /** (5) 실제 수정 */
    @PostMapping("/update")
    public String update(@RequestParam("questionId") int questionId,
                         @RequestParam String title,
                         @RequestParam String contents,
                         HttpSession session) {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        User user = UserSessionUtils.getUserFromSession(session);
        Question question = questionDao.findByQuestionId(questionId);
        if (!question.isSameUser(user)) {
            throw new IllegalArgumentException("로그인된 유저와 질문 작성자가 다르면 질문을 수정할 수 없습니다.");
        }
        question.updateTitleAndContents(title, contents);
        questionDao.update(question);
        return "redirect:/";
    }



}
