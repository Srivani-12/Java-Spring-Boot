package com.example.course.service;

import com.example.course.dao.InstructorRepository;
import com.example.course.dao.MemberRepository;
import com.example.course.dao.StudentRepository;
import com.example.course.entity.Instructor;
import com.example.course.entity.Member;
import com.example.course.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    public void registerNewMember(Member member,String roleName,String username,String email){
        memberRepository.save(member);

        if(roleName.equals("ROLE_STUDENT")){
            Student student = new Student();
            student.setUsername(username);
            student.setUser(member);
            student.setEmail(email);
            studentRepository.save(student);
        }
        else if (roleName.equals("ROLE_INSTRUCTOR")) {
            Instructor instructor = new Instructor();
            instructor.setUser(member);
            instructor.setUsername(username);
            instructor.setEmail(email);
            instructor.setDepartment("Computer Science");
            instructorRepository.save(instructor);
        }
    }
}
