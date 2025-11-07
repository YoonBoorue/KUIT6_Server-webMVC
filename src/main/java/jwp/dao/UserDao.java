package jwp.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import jwp.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        jdbcTemplate.update(sql, pstmtSetter);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
        jdbcTemplate.update(sql, pstmtSetter);
    }

    public void delete(User user) {
        String sql = "DELETE FROM USERS WHERE userId = ?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
        };
        jdbcTemplate.update(sql, pstmtSetter);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        RowMapper rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, userId);
        };

        RowMapper rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        return jdbcTemplate.queryForObject(sql, pstmtSetter, rowMapper);
    }
}
