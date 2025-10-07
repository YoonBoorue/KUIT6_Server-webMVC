package jwp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    String handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
