package jwp.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import jwp.model.User;

import java.util.List;

public class UserDao {
    private final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<User>();

    public void inset(User user){

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        jdbcTemplate.update(sql, pss);
    }

    public void update(User user){
        String sql = "UPDATE USERS SET password =?, name=?, email=? WHERE id=?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
            };
        jdbcTemplate.update(sql,pstmtSetter);
    }

    public void delete(User user){

        String sql = "DELETE FROM USERS WHERE userid=?";
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.executeUpdate();
        };
        jdbcTemplate.update(sql,pss);
    }

    public List<User> findAll(){

        String sql = "SELECT * FROM USERS";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userid"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userid){

        String sql = "SELECT * FROM USERS WHERE userid=?";
        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, userid);
        };
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userid"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        return  jdbcTemplate.queryForObject(sql, pstmtSetter, rowMapper).orElse(null);
    }
}
