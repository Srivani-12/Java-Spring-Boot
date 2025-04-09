package com.example.course.service;

import com.example.course.dao.CourseRepository;
import com.example.course.dao.EnrollmentRepository;
import com.example.course.dao.StudentRepository;
import com.example.course.entity.Course;
import com.example.course.entity.Enrollment;
import com.example.course.entity.Instructor;
import com.example.course.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private EnrollmentRepository enrollmentRepository;

    public StudentService(StudentRepository theStudentRepository,CourseRepository theCourseRepository,EnrollmentRepository theEnrollmentRepository){
        studentRepository = theStudentRepository;
        courseRepository = theCourseRepository;
        enrollmentRepository = theEnrollmentRepository;
    }

    public List<Student> findAll(){

        return studentRepository.findAll();
    }
    public List<Student> findStudentsByCourseId(Long courseId){
        return studentRepository.findByCourseId(courseId);
    }

    public Student getStudentByUsername(String username){
        return studentRepository.findByUsername(username);
    }

    public Student findById(Integer id){
        System.out.println("Searching for Student with Id: "+id);
        Student student = studentRepository.findById(id).orElse(null);
        System.out.println("Found Student: "+student);
        return student;
    }

    public Student findByEmail(String email){
        Student student = studentRepository.findByEmail(email);
        return student;
    }

    @Transactional
    public Student save(Student student){
        return studentRepository.save(student);
    }

    public void addCourse(Long studentId,Long courseId){
        Student student = studentRepository.findById(studentId);
        Course course = courseRepository.findById(courseId);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentRepository.save(enrollment);
    }



}