package com.example.course.service;

import com.example.course.dao.InstructorRepository;
import com.example.course.entity.Instructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    public Instructor getInstructorByUsername(String username) {
        return instructorRepository.findByUsername(username);
    }
    public Instructor getInstructorByEmail(String email) {
        return instructorRepository.findByEmail(email);
    }

    public Instructor findById(Integer id) {
        System.out.println("Searching for Instructor with ID: " + id);
        Instructor instructor = instructorRepository.findById(id).orElse(null);
        System.out.println("Found Instructor: " + instructor);
        return instructor;
    }

    @Transactional
    public Instructor save(Instructor instructor) {  // Return the saved entity
        return instructorRepository.save(instructor);
    }

    public Instructor findAssignedInstructor(Long studentId){
       List<Instructor> instructors = instructorRepository.findInstructorsByStudentId(studentId);
    return instructors.isEmpty() ? null:instructors.get(0);
    }

}
