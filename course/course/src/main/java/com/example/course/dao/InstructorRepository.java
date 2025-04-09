package com.example.course.dao;

import com.example.course.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,Integer> {

    public List<Instructor> findAll();

    default Instructor getById(Integer id) {
        return findById(id).orElse(null); // Returns null if not found
    }

    public Instructor findByUsername(String username);

    public Instructor findByEmail(String email);

    @Query("SELECT i FROM Instructor i JOIN i.courses c JOIN c.students s WHERE s.id = :studentId")
    List<Instructor> findInstructorsByStudentId(Long studentId);

    Instructor findById(Long otherUserId);
}