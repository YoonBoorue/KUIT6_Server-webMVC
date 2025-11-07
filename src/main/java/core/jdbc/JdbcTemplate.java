package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {

    public void update(String sql, PreparedStatementSetter pstmtSetter){
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmtSetter.setParameters(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("JdbcTemplate.update 실행 중 오류", e);
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper) {
        List<T> objects = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                T object = rowMapper.mapRow(rs);
                objects.add(object);
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcTemplate.query 실행 중 오류", e);
        }
        return objects;
    }

    public T queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        ResultSet rs = null;
        T object = null;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmtSetter.setParameters(pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                object = rowMapper.mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcTemplate.queryForObject 실행 중 오류", e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
        }
        return object;
    }

    public void update(String sql, PreparedStatementSetter pstmtSetter, KeyHolder keyHolder) {
        ResultSet rs = null;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmtSetter.setParameters(pstmt);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                keyHolder.setId((int) rs.getLong(1));
            }
        }  catch (SQLException e) {
            throw new RuntimeException("JdbcTemplate.update(with KeyHolder) 실행 중 오류", e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
        }
        }
}
