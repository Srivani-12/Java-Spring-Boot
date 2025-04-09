package com.example.course.dao;

import com.example.course.entity.Course;
import com.example.course.entity.Instructor;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {  // Ensure Integer ID

    List<Course> findByInstructorId(Integer instructorId);


    List<Course> findByInstructor(Instructor instructor);

    public Course findById(Long id);  // Changed to Integer

    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findCoursesInstructorId(@Param("instructorId") Integer instructorId);

    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByInstructorIdAndNameContainingIgnoreCase(Integer instructorId, String name);

    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.name LIKE %:search%")
    List<Course> searchCoursesByInstructor(@Param("instructorId") Integer instructorId, @Param("search") String search);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Course> findByStudentIdAndNameContainingIgnoreCase(@Param("studentId") Long studentId, @Param("name") String name);

}
