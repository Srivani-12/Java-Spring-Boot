package com.example.course.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate = LocalDate.now();


    // Default Constructor


    // Parameterized Constructor
    // Add this constructor
    public Enrollment() {
        this.enrollmentDate = LocalDate.now();
    }

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now(); // set here
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @PrePersist
    protected void onCreate() {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = LocalDate.now();
        }
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", courseId=" + (course != null ? course.getId() : "null") +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
