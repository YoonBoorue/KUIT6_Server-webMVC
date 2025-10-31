package jwp.controller;

import jwp.controller.questionControllers.CreateQuestionFormController;
import jwp.controller.questionControllers.ShowController;
import jwp.controller.userControllers.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Controller> mapper = new HashMap<>();
    private static final String VIEW_PREFIX = ""; //별도로 존재하지 않음.
    private static final String VIEW_SUFFIX = ".jsp";

    @Override
    public void init() {
        mapper.put("/", new HomeController());
        mapper.put("/user/login", new LoginController());
        mapper.put("/user/logout", new LogoutController());
        mapper.put("/user/signup", new CreateUserController());
        mapper.put("/user/update", new UpdateUserController());
        mapper.put("/user/updateForm", new UpdateUserFormController());
        mapper.put("/user/list", new ListUserController());
        mapper.put("/qna/create", new CreateQuestionFormController());
        mapper.put("/qna/show", new ShowController());
        //ListQuestion추가
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ctx = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());

        //정적 쳐내기
        if (path.isEmpty()) path = "/";
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            req.getRequestDispatcher(path).forward(req, resp);
            return;
        }

        if (path.endsWith(".html")) {
            String cleaned = path.substring(0, path.length() - ".html".length());
            resp.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
            resp.setHeader("Location", ctx + (cleaned.isEmpty() ? "/" : cleaned));
            return;
        }

        //문제 있으면 쳐내기
        Controller controller = mapper.get(path);
        if (controller == null) {
            String jsp = VIEW_PREFIX + path + VIEW_SUFFIX;
            if (getServletContext().getResource(jsp) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.getRequestDispatcher(jsp).forward(req, resp);
            return;
        }

        try {
            String view = controller.execute(req, resp);
            if (view == null) return; // 컨트롤러가 직접 응답을 끝낸 경우

            if (view.startsWith("redirect:")) {
                resp.sendRedirect(ctx + view.substring("redirect:".length()));
            } else {
                // 논리 뷰 이름("home", "user/list") → /user/***.jsp
                String logical = view.startsWith("/") ? view : ("/" + view);
                String fullPath = VIEW_PREFIX + logical + VIEW_SUFFIX;
                req.getRequestDispatcher(fullPath).forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
