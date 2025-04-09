package com.example.course.controller;


import com.example.course.entity.Course;
import com.example.course.entity.Instructor;
import com.example.course.entity.Student;
import com.example.course.service.CourseService;
import com.example.course.service.EnrollmentService;
import com.example.course.service.InstructorService;
import com.example.course.service.StudentService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final InstructorService instructorService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;



    public InstructorController(InstructorService instructorService, CourseService courseService, StudentService studentService, EnrollmentService enrollmentService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }


    // Helper method to get the currently logged-in instructor


    private Instructor getCurrentInstructor(Authentication authentication) {
        String username = authentication.getName();
        Instructor instructor = instructorService.getInstructorByEmail(username);

        if (instructor == null) {
            System.out.println("Error: Instructor with username " + username + " not found.");
        }

        return instructor;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        Instructor instructor = getCurrentInstructor(authentication);

        if (instructor.isPresent()) {
            List<Course> assignedCourses = courseService.findCoursesInstructorId(instructor.getId());
            System.out.println("Instructor ID: " + instructor.getId());
            System.out.println("Instructor Name: " + instructor.getUsername());
            System.out.println("Courses Assigned: " + assignedCourses.size());

            if (assignedCourses.isEmpty()) {
                System.out.println("No courses found for instructor.");
            } else {
                for (Course course : assignedCourses) {
                    int studentCount = enrollmentService.getEnrolledStudentCount(course.getId());
                    course.setStudentCount(studentCount); // Assuming Course has a transient field `studentCount`
                    System.out.println("Course ID: " + course.getId() + ", Name: " + course.getName() + ", Students Enrolled: " + studentCount);
                }
            }

            model.addAttribute("instructor", instructor);
            model.addAttribute("courses", assignedCourses);

            return "instructor-dashboard";
        }
        return "access-denied";
    }


    @GetMapping("/course/{courseId}/students")
    public String viewStudentsCourse(@PathVariable Long courseId,Model model,Authentication authentication){


        Instructor instructor = getCurrentInstructor(authentication);
        if(instructor.isPresent()){
            Course course = courseService.findById(courseId);

            if(course.getInstructor().getId() == instructor.getId()) {
                List<Student> enrolledStudents = studentService.findStudentsByCourseId(courseId);
                model.addAttribute("course", course);
                model.addAttribute("students", enrolledStudents);
                return "course-students";
            }else{
                return "access-denied";
            }
        }else{
            return "access-denied";
        }
    }
    @GetMapping("/editProfile")
    public String showUpdateForm(Model model,Authentication authentication){

        Instructor instructor = getCurrentInstructor(authentication);

        if(instructor.isPresent()){
            model.addAttribute("instructor",instructor);
            return "instructor-form";

        }
        else{
            return "access-denied";
        }
    }


    @PostMapping("/updateProfile")
    public String updateInstructor(@ModelAttribute("instructor") Instructor updatedInstructor, Authentication authentication) {
        Instructor instructor = getCurrentInstructor(authentication);

        if (instructor.isPresent()) {
            Instructor existingInstructor = instructor;

            // Prevent null values
            if (updatedInstructor.getUsername() != null && !updatedInstructor.getUsername().isEmpty()) {
                existingInstructor.setUsername(updatedInstructor.getUsername());
            }

            existingInstructor.setEmail(updatedInstructor.getEmail());
            existingInstructor.setDepartment(updatedInstructor.getDepartment());

            instructorService.save(existingInstructor);
            return "redirect:/instructor/dashboard";
        }
        return "redirect:/instructor/dashboard";
    }
    @GetMapping("/search")
    public String searchCourse(@RequestParam(required = false, defaultValue = "") String search,
                               Model model,
                               Principal principal) {
        // Get logged-in instructor
        String username = principal.getName();
        Instructor instructor = instructorService.getInstructorByEmail(username);

        System.out.println("Logged-in instructor email: " + username);
        System.out.println("Instructor ID: " + instructor.getId());
        System.out.println("Search Term: " + search);

        // If no search term, return all courses
        List<Course> courses;
        if (search == null || search.trim().isEmpty()) {
            courses = courseService.findCoursesInstructorId(instructor.getId()); // Get all courses
        } else {
            courses = courseService.searchCoursesByInstructor(instructor.getId(), search);
        }

        if (courses.isEmpty()) {
            System.out.println("No courses found for search: " + search);
        }

        // ✅ Fix: Set studentCount for each course
        for (Course course : courses) {
            int studentCount = enrollmentService.getEnrolledStudentCount(course.getId());
            course.setStudentCount(studentCount);
        }


        System.out.println("Courses found: " + courses.size());
        model.addAttribute("courses", courses);
        model.addAttribute("search", search);
        model.addAttribute("instructor", instructor);

        return "instructor-dashboard"; // Return to the dashboard
    }

    @GetMapping("/students/{id}")
    public String showEnrolledStudents(@PathVariable Long id, Model model){
        Course course = courseService.findById(id);
        List<Student> students = course.getStudents();

        model.addAttribute("course",course);
        model.addAttribute("students",students);
        return "enrolled-students";
    }

    @GetMapping("/course/{courseId}/removeStudent/{studentId}")
    public String removeStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) {
        courseService.removeStudentFromCourse(courseId, studentId);
        return "redirect:/instructor/dashboard";
    }

    @GetMapping("/course/create")
    public String showCreateCourseForm(Model model,Authentication authentication){
        Instructor instructor = getCurrentInstructor(authentication);

        if(instructor!=null){
            Course course = new Course();
            model.addAttribute("course",course);
            model.addAttribute("instructor",instructor);
            return "create-course-form";
        }
        return "access-denied";
    }
    @PostMapping("/course/save")
    public String saveNewCourse(@ModelAttribute("course") Course course,Authentication authentication){
        Instructor instructor = getCurrentInstructor(authentication);
        if(instructor!=null){
            course.setInstructor(instructor);
            courseService.save(course);

            return "redirect:/instructor/dashboard";
        }
        return "access-denied";
    }


}
