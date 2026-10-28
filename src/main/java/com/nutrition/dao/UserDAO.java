package com.nutrition.dao;

import com.nutrition.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User findByUsername(String username) throws SQLException;
    User findById(int userId) throws SQLException;
    User findByEmail(String email) throws SQLException;
    int createUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean updatePassword(int userId, String newHash, String newSalt) throws SQLException;
    boolean deleteUser(int userId) throws SQLException;

    void saveUserMedicalConditions(int userId, List<Integer> conditionIds) throws SQLException;
    void saveUserAllergies(int userId, List<Integer> allergyIds) throws SQLException;
}
