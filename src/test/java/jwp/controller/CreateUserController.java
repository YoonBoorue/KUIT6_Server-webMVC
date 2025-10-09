package jwp.controller;

import core.db.MemoryUserRepository;
import jwp.controller.controllers.CreateUserController;
import jwp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateUserControllerTest {

    private CreateUserController controller;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private MemoryUserRepository repo;

    @BeforeEach
    void setUp() {
        controller = new CreateUserController();
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        repo = MemoryUserRepository.getInstance();
        repo.clear();

        // 파라미터 설정
        when(req.getParameter("userId")).thenReturn("testUser");
        when(req.getParameter("password")).thenReturn("pass123");
        when(req.getParameter("name")).thenReturn("테스트유저");
        when(req.getParameter("email")).thenReturn("test@example.com");
    }

    @Test
    @DisplayName("정상 입력이면 저장되고 redirect:/ 반환")
    void handle_success_persists_and_redirects_root() throws ServletException, IOException {
        String view = controller.handle(req, resp);

        assertThat(view).isEqualTo("redirect:/");
        User saved = repo.findUserById("testUser");
        assertThat(saved).isNotNull();
        assertThat(saved.getPassword()).isEqualTo("pass123");
        assertThat(saved.getName()).isEqualTo("테스트유저");
        assertThat(saved.getEmail()).isEqualTo("test@example.com");

        verifyNoMoreInteractions(resp); // 현재 구현: resp 사용 없음
    }

    @Nested
    @DisplayName("파라미터 경계/누락 케이스 → redirect:/user/form & 미저장")
    class ParamEdges {

        @Test
        @DisplayName("name == null → redirect:/user/form, 저장 안 됨")
        void null_name() throws ServletException, IOException {
            when(req.getParameter("name")).thenReturn(null);
            when(req.getParameter("userId")).thenReturn("u-null-name");

            String view = controller.handle(req, resp);

            assertThat(view).isEqualTo("redirect:/user/form");
            assertThat(repo.findUserById("u-null-name")).isNull();
            verify(req).setAttribute(eq("errorMessage"), any());
        }

        @Test
        @DisplayName("email == \"\" → redirect:/user/form, 저장 안 됨")
        void empty_email() throws ServletException, IOException {
            when(req.getParameter("email")).thenReturn("");
            when(req.getParameter("userId")).thenReturn("u-empty-email");

            String view = controller.handle(req, resp);

            assertThat(view).isEqualTo("redirect:/user/form");
            assertThat(repo.findUserById("u-empty-email")).isNull();
            verify(req).setAttribute(eq("errorMessage"), any());
        }

        @Test
        @DisplayName("userId가 공백 문자열 → redirect:/user/form, 저장 안 됨")
        void blank_userId() throws ServletException, IOException {
            when(req.getParameter("userId")).thenReturn("  ");
            when(req.getParameter("name")).thenReturn("공백유저");

            String view = controller.handle(req, resp);

            assertThat(view).isEqualTo("redirect:/user/form");
            assertThat(repo.findUserById("  ")).isNull();
            verify(req).setAttribute(eq("errorMessage"), any());
        }
    }

    @Test
    @DisplayName("중복 userId → redirect:/user/form, 기존 데이터 보존(덮어쓰기 금지)")
    void duplicate_does_not_overwrite_and_redirects_form() throws ServletException, IOException {
        // 1차: 정상 생성
        String first = controller.handle(req, resp);
        assertThat(first).isEqualTo("redirect:/");
        User original = repo.findUserById("testUser");
        assertThat(original).isNotNull();

        // 2차: 같은 userId로 다른 값 시도 → 중복 처리
        when(req.getParameter("password")).thenReturn("changed");
        when(req.getParameter("name")).thenReturn("변경유저");
        when(req.getParameter("email")).thenReturn("changed@example.com");

        String second = controller.handle(req, resp);

        assertThat(second).isEqualTo("redirect:/user/form");
        User kept = repo.findUserById("testUser");
        assertThat(kept.getPassword()).isEqualTo("pass123");
        assertThat(kept.getName()).isEqualTo("테스트유저");
        assertThat(kept.getEmail()).isEqualTo("test@example.com");
        verify(req, atLeastOnce()).setAttribute(eq("errorMessage"), any());
    }
}
