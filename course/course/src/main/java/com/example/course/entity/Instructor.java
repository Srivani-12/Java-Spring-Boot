package com.example.course.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructor")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "department")
    private String department;

    public void setUsername(String username) {
        this.username = username;
    }

    @OneToMany(mappedBy = "instructor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    // Default Constructor
    public Instructor() {
    }

    @OneToOne
    @JoinColumn(name = "email", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Member user;

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public Instructor(Member user) {
        this.user = user;
    }

    // Parameterized Constructor
    public Instructor(String username, String email, String department) {
        this.username = username;
        this.email = email;
        this.department = department;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
    }

    // Helper method to add a course
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            course.setInstructor(this); // Maintain bidirectional consistency
        }
    }

    // Helper method to remove a course
    public void removeCourse(Course course) {
        if (course != null && courses.remove(course)) {
            course.setInstructor(null); // Prevent dangling references
        }
    }

    public boolean isPresent() {
        return true;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", user=" + (user != null ? user.getUserId() : null) +
                '}';
    }

}
