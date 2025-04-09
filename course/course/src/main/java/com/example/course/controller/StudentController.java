package com.example.course.controller;

import com.example.course.entity.Course;
import com.example.course.entity.Instructor;
import com.example.course.entity.Student;
import com.example.course.service.CourseService;
import com.example.course.service.EnrollmentService;
import com.example.course.service.InstructorService;
import com.example.course.service.StudentService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final InstructorService instructorService;
    private final EnrollmentService enrollmentService;


    public StudentController(StudentService studentService, CourseService courseService, InstructorService instructorService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.enrollmentService = enrollmentService;
    }

    private Student getCurrentStudent(Authentication authentication){
        String username = authentication.getName();
        Student student = studentService.findByEmail(username);

        if(student == null){
            System.out.println("Error: student with username"+username+"not found");

        }
        return student;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model,Authentication authentication) {
        Student student = getCurrentStudent(authentication);
        Instructor instructor = instructorService.findAssignedInstructor(student.getId());

        if (student != null) {
            List<Course> assignedCourses = courseService.findCoursesByStudentId(student.getId());
            System.out.println("Student id: " + student.getId());
            System.out.println("Student name: " + student.getUsername());
            System.out.println("Student course: " + student.getCourses());
            for(Course c: student.getCourses()){
                System.out.println("Course Link:"+c.getYoutubeLink());
            }
            if (assignedCourses.isEmpty()) {
                System.out.println("No Courses found for Student " + student);

            }

            model.addAttribute("student", student);
            model.addAttribute("courses", assignedCourses);

            model.addAttribute("instructor",instructor);
            return "student-dashboard";
        }
    return "access-denied";

    }

    @GetMapping("/profile")
    public String showProfileForm(Model model,Authentication authentication){
        Student student = getCurrentStudent(authentication);

        if(student != null){
            model.addAttribute("student",student);
            return "student-form";
        }
        else{
            return "access-denied";
        }
    }

    @GetMapping("/updateProfile")
    public String updateStudent(@ModelAttribute("student")Student updatedStudent,Authentication authentication){
        Student student = getCurrentStudent(authentication);

        if(student != null){
            Student existingStudent = student;
            if(updatedStudent.getUsername()!=null && !updatedStudent.getUsername().isEmpty()){
                existingStudent.setUsername(updatedStudent.getUsername());
            }

            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setUsername(updatedStudent.getUsername());

            studentService.save(existingStudent);
            return "redirect:/student/dashboard";
        }
        return "redirect:/student/dashboard";
    }


    @GetMapping("/student")
    public String showDashboard(Model model){
        List<Student> theStudents = studentService.findAll();

        System.out.println("Students: "+theStudents);

        model.addAttribute("students",theStudents);

        return "student";
    }
    @GetMapping("/search")
    public String searchCourse(@RequestParam(required = false,defaultValue = "")String search,
                               Model model,
                               Principal principal) {
        String username = principal.getName();
        Student student = studentService.findByEmail(username);
        System.out.println("Logged-in student email : " + username);
        System.out.println("Student name: " + student.getUsername());
        System.out.println("Search Term: " + search);

        List<Course> courses;
        if (search == null || search.trim().isEmpty()) {
            courses = courseService.findCoursesByStudentId(student.getId());
        } else {
            courses = courseService.searchCoursesByStudentId(student.getId(), search);
        }
        if (courses.isEmpty()) {
            System.out.println("No courses found for search: " + search);
        }
        model.addAttribute("courses", courses);
        model.addAttribute("search", search);
        model.addAttribute("student", student);
        return "student-dashboard";


    }


    @GetMapping("/{studentId}/removeCourse/{courseId}")
    public String removeCourse(@PathVariable Long courseId, @PathVariable Integer studentId){

        courseService.removeCourseFromStudent(courseId,studentId);
        return "redirect:/student/dashboard";

    }

    @GetMapping("/course/{id}/instructor")
    public String showInstructorForCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        Instructor instructor = course.getInstructor();
        model.addAttribute("course", course);
        model.addAttribute("instructor", instructor);
        return "instructor-details";
    }


    @GetMapping("/{id}/add-course")
    public String showAddCourseForm(@PathVariable Integer id,Model model){
        Student student = studentService.findById(id);
        List<Course> courses = courseService.findUnenrolledCoursesForStudent(student);

        model.addAttribute("student",student);
        model.addAttribute("courses",courses);
        return "add-course-to-student";

    }

    @PostMapping("/{id}/add-course")
    public String addCourseToStudent(@PathVariable Long id,@RequestParam Long courseId){
        studentService.addCourse(id,courseId);
        return "redirect:/student/dashboard";

    }


}