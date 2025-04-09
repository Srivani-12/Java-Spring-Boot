package com.example.course.service;

import com.example.course.dao.CourseRepository;
import com.example.course.dao.EnrollmentRepository;
import com.example.course.dao.StudentRepository;
import com.example.course.entity.Course;
import com.example.course.entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesInstructorId(Integer instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> findCoursesByStudentId(Long studentId){
        return courseRepository.findCoursesByStudentId(studentId);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id);
    }

    public int getEnrolledStudentCount(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    public List<Course> searchCourses(String keyword){
        if(keyword == null || keyword.trim().isEmpty()){
            return courseRepository.findAll();
        }
        return courseRepository.findByNameContainingIgnoreCase(keyword);
    }



    public List<Course> searchCoursesByInstructor(Integer instructorId, String search) {
        if (search == null || search.trim().isEmpty()) {
            return courseRepository.findByInstructorId(instructorId); // All courses if no search
        }

        List<Course> courses = courseRepository.findByInstructorIdAndNameContainingIgnoreCase(instructorId, search);

        System.out.println("Search found " + courses.size() + " courses for '" + search + "'");
        return courses;
    }

    public List<Course> searchCoursesByStudentId(Long studentId,String search){
        if(search == null||search.trim().isEmpty()){
            return courseRepository.findCoursesByStudentId(studentId);
        }
        List<Course> courses = courseRepository.findByStudentIdAndNameContainingIgnoreCase(studentId,search);
        System.out.println("Search found "+courses.size()+"courses for "+search+ "");
        return courses;
    }

    @Transactional
    public Course save(Course course){
        return courseRepository.save(course);
    }

    @Transactional
    public void removeStudentFromCourse(Long courseId, Long studentId){
        Course course = courseRepository.findById(courseId);

        Student student = studentRepository.findById(studentId);

        course.getStudents().remove(student);
        courseRepository.save(course);
    }

    @Transactional
    public void removeCourseFromStudent(Long couresId, Integer studentId){
        Course course = courseRepository.findById(couresId);
        Student student = studentRepository.getById(studentId);

        student.getCourses().remove(course);
        courseRepository.save(course);
    }

    public List<Course> findUnenrolledCoursesForStudent(Student student){
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .filter(course -> !student.getCourses().contains(course))
                .collect(Collectors.toList());
    }

}