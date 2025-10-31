package jwp.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import jwp.holder.KeyHolder;
import jwp.model.Question;

import java.util.List;

public class QuestionDao {
    private static final QuestionDao instance = new QuestionDao();
    private final JdbcTemplate<Question> jdbcTemplate = new JdbcTemplate<>();

    private QuestionDao() {}

    public static QuestionDao getInstance() {
        return instance;
    }

    public Question insert(Question question){
        KeyHolder keyHolder = new KeyHolder();
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, question.getWriter());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
        };
        jdbcTemplate.update(sql, pss, keyHolder);
        return findByQuestionId((long) keyHolder.getId());
    }

    public void update(Question question){
        String sql = "UPDATE QUESTIONS SET writer =?, title =?, contents =?, createdDate =?, countOfAnswer =?, WHERE id=?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, question.getWriter());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(question.getCreatedDate()));
            pstmt.setInt(5, question.getCountOfAnswer());
            pstmt.setLong(6, question.getQuestionId());
        };
        jdbcTemplate.update(sql,pstmtSetter);
    }

    public void delete(Question question){

        String sql = "DELETE FROM QUESTIONS WHERE questionId=?";
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setLong(1, question.getQuestionId());
            pstmt.executeUpdate();
        };
        jdbcTemplate.update(sql,pss);
    }

    public List<Question> findAll(){

        String sql = "SELECT * FROM QUESTIONS";
        RowMapper<Question> rowMapper = rs -> new Question(rs.getLong("questionId"),
                rs.getString("writer"),
                rs.getString("title"),
                rs.getString("contents"),
                rs.getTimestamp("createdDate").toLocalDateTime(),
                rs.getInt("countOfAnswer"));
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Question findByQuestionId(Long questionId){

        String sql = "SELECT * FROM QUESTIONS WHERE questionId=?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setLong(1, questionId);
        };
        RowMapper<Question> rowMapper = rs -> new Question(rs.getLong("questionId"),
                rs.getString("writer"),
                rs.getString("title"),
                rs.getString("contents"),
                rs.getTimestamp("createdDate").toLocalDateTime(),
                rs.getInt("countOfAnswer"));
        return  jdbcTemplate.queryForObject(sql, pstmtSetter, rowMapper).orElse(null);
    }
}
