package com.example.course.dao;

import com.example.course.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,String> {


    @Query("SELECT m FROM Member m JOIN FETCH m.roles WHERE m.userId = :userId")
    Member findByUserId(@Param("userId") String userId);

}
