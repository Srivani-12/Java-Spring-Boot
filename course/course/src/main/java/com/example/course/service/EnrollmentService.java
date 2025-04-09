package com.example.course.service;

import com.example.course.dao.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public int getEnrolledStudentCount(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }
}
