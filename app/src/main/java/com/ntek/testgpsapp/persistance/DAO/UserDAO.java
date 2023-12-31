package com.ntek.testgpsapp.persistance.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ntek.testgpsapp.persistance.Entity.User;

import java.util.List;

@Dao
public interface UserDAO {

    // 동일 아이디 조회
    @Query("SELECT * FROM user WHERE userId = :userId")
    List<User> findByUserId(String userId);

    // 동일 아이디, 비밀번호 계정 조회
    @Query("SELECT * FROM user WHERE userId = :userId and userPw = :userPw")
    List<User> findByUserAssign(String userId, String userPw);


    // 동일 이메일 조회
    @Query("SELECT * FROM user WHERE userEmail = :userEmail")
    List<User> findByEmail(String userEmail);

    // 회원정보 저장
    @Insert
    void insertAll(User... users);
}
