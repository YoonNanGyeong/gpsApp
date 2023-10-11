package com.ntek.testgpsapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ntek.testgpsapp.Entity.User;

import java.util.List;

@Dao
public interface UserDAO {
    // 회원 정보 목록 조회
    @Query("SELECT * FROM user")
    List<User> getUserList();

    // 동일 아이디 조회
    @Query("SELECT * FROM user WHERE uid = :userId")
    List<User> findByUserId(String userId);

    // 동일 비밀번호 조회
    @Query("SELECT * FROM user WHERE pw = :pw")
    List<User> findByPw(String pw);

    // 동일 이메일 조회
    @Query("SELECT * FROM user WHERE pw = :email")
    List<User> findByEmail(String email);

    // 회원정보 저장
    @Insert
    void insertAll(User... users);
}
