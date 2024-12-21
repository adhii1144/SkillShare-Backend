package com.example.skillshare.Reporistry;

import com.example.skillshare.model.Users;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface Signin_Repo extends JpaRepository<Users,Integer> {
        Users findByEmail(String email);

//        @Transactional
//        @Modifying
//        @Query("update Users u set u.password = ?2 where u.email=?1")
//        void updatePassword(String email, String password);

        @Modifying
        @Query("UPDATE Users u SET u.password = :password WHERE u.email = :email")
        @Transactional
        void updatePassword(@Param("email") String email, @Param("password") String password);

        //Optional<Users> findByUsername(String username);

        @Query("SELECT u FROM Users u WHERE u.Name = :name")
        Optional<Users> findByName(@Param("name") String name);

//        List<Users> findByNameContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndLocationContainingIgnoreCase(
//                String name, String bio, String location);

}
