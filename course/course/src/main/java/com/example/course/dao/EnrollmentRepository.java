package com.example.course.dao;

import com.example.course.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    int countByCourseId(Long courseId);

}