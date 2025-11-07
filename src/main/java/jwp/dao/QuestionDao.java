package jwp.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import jwp.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionDao {

    private final JdbcTemplate<Question> jdbcTemplate = new JdbcTemplate<>();

    public Question insert(Question question) {
        KeyHolder keyHolder = new KeyHolder();
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate) VALUES (?, ?, ?, ?)";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, question.getWriter());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
            pstmt.setObject(4, question.getCreatedDate());
        };
        jdbcTemplate.update(sql, pstmtSetter, keyHolder);
        return findByQuestionId(keyHolder.getId());
    }


    public void update(Question question) {
        String sql = "UPDATE QUESTIONS SET title = ?, contents = ?, createdDate = ? WHERE questionId = ?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getContents());
            pstmt.setObject(3, question.getCreatedDate());
            pstmt.setLong(4, question.getQuestionId());
        };
        jdbcTemplate.update(sql, pstmtSetter);
    }

    public void delete(int questionId) {
        String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setInt(1, questionId);
        };
        jdbcTemplate.update(sql, pstmtSetter);
    }

    public List<Question> findAll() {
        String sql = "SELECT * FROM QUESTIONS ORDER BY questionId";
        RowMapper rowMapper = rs -> new Question(rs.getInt("questionId"),
                rs.getString("writer"),
                rs.getString("title"),
                rs.getString("contents"),
                rs.getDate("createdDate"),
                rs.getInt("countOfAnswer"));
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Question findByQuestionId(int questionId) {
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer " +
                "FROM QUESTIONS WHERE questionId=?";

        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setInt(1, questionId);
        };

        RowMapper rowMapper = rs -> new Question(rs.getInt("questionId"),
                rs.getString("writer"),
                rs.getString("title"),
                rs.getString("contents"),
                rs.getDate("createdDate"),
                rs.getInt("countOfAnswer"));

        return jdbcTemplate.queryForObject(sql, pstmtSetter, rowMapper);
    }
}
