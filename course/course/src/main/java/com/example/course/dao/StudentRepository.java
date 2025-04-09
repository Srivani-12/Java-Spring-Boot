package com.example.course.dao;

import com.example.course.entity.Course;
import com.example.course.entity.Instructor;
import com.example.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    // that's it ... no need to write any code LOL!

    //add a method to sort by last name
    public List<Student> findAll();


    List<Student> findByCourses(Course course);

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);

    Student findById(long id);

    public Student findByUsername(String username);


    public Student findByEmail(String email);

    default Student getById(Integer id){
        return findById(id).orElse(null);
    }


}

