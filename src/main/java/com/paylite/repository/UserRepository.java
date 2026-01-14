package com.paylite.repository;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;


@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public void createUser(String username, String password) {
        String sql = """
            INSERT INTO users (username, password, balance)
            VALUES (?, ?, ?)
        """;
        jdbcTemplate.update(sql, username, password, new BigDecimal("1000.00"));
    }
    
    public Long validateUser(String username, String password) {
        String sql = """
            SELECT user_id
            FROM users
            WHERE username = ? AND password = ?
        """;

        try {
            return jdbcTemplate.queryForObject(sql, Long.class, username, password);
        } catch (Exception e) {
            return null; // invalid credentials
        }
    }

    public BigDecimal getBalance(Long userId) {
        String sql = "SELECT balance FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }
    
    public BigDecimal getBalanceForUpdate(Long userId) {
        String sql = """
            SELECT balance
            FROM users
            WHERE user_id = ?
            FOR UPDATE
        """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    public Long getUserIdByUsername(String username) {
        try {
            String sql = "SELECT user_id FROM users WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, Long.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateBalance(Long userId, BigDecimal amountDelta) {
        String sql = """
            UPDATE users
            SET balance = balance + ?
            WHERE user_id = ?
        """;
        jdbcTemplate.update(sql, amountDelta, userId);
    }
    
    public void updatePhoneNumber(Long userId, String phoneNumber) {
        String sql = """
            UPDATE users
            SET phone_number = ?
            WHERE user_id = ?
        """;
        jdbcTemplate.update(sql, phoneNumber, userId);
    }

}
