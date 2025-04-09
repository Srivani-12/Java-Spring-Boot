package com.example.course.controller;

import com.example.course.entity.Course;
import com.example.course.entity.Instructor;
import com.example.course.service.CourseService;
import com.example.course.service.InstructorService;
import jakarta.persistence.GeneratedValue;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final InstructorService instructorService;

    public CourseController(CourseService courseService, InstructorService instructorService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
    }


    public String showCourses(Model model){
        List<Course> theCourses = courseService.findAll();

        System.out.println("Courses: "+theCourses);

        model.addAttribute("courses",theCourses);

        return "courses";
    }

    @GetMapping("/courses")
    public String showCoursesByInstructorId(Model model){

        Integer theId = 1;

        List<Course> thecourses = courseService.findCoursesInstructorId(theId);

        System.out.println("Courses of Instructor id 1:"+thecourses);

        model.addAttribute("courses",thecourses);

        return "courses-instructor";
    }
    public String listCourses(@RequestParam(value = "search",required = false)String search,Model model){
        List<Course> courses = courseService.searchCourses(search);
        model.addAttribute("course",courses);
        model.addAttribute("search",search);
        return "courses";
    }
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id , Model model, Principal principal){
        Instructor instructor = instructorService.getInstructorByEmail(principal.getName());
        Course course = courseService.findById(id);


        if(!course.getInstructor().getId().equals(instructor.getId())){
            return "error/403";
        }
        model.addAttribute("course",course);
        return "edit-course";


    }

    @Transactional
    @PostMapping("/update")
    public String updateCourse(@ModelAttribute("course") Course updatedCourse, Principal principal){
        Instructor instructor = instructorService.getInstructorByEmail(principal.getName());
        Course existingCourse = courseService.findById(updatedCourse.getId());

        if(!existingCourse.getInstructor().getId().equals(instructor.getId())){
            return "error/403";
        }
        existingCourse.setName(updatedCourse.getName());
        existingCourse.setCapacity(updatedCourse.getCapacity());
        existingCourse.setAvailableSeats(updatedCourse.getAvailableSeats());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setStartDate(updatedCourse.getStartDate());
        existingCourse.setEndDate(updatedCourse.getEndDate());

        courseService.save(existingCourse);

        return "redirect:/instructor/dashboard";
    }

}