package com.example.course.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", instructorId=" + (instructor != null ? instructor.getId() : null) +
                ", capacity=" + capacity +
                ", availableSeats=" + availableSeats +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", youtubeLink='" + youtubeLink + '\'' +
                '}';
    }


    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public Course(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public Course(String description, LocalDate startDate, LocalDate endDate) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Transient
    private int studentCount;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    public Course() {
    }

    public Course(String name, String code, Instructor instructor, int capacity) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.capacity = capacity;
        this.availableSeats = capacity;
    }

    public Course(List<Student> students) {
        this.students = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = Math.max(availableSeats, 0);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean enrollStudent(Student student) {
        System.out.println("Attempting to enroll: " + student.getUsername());
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("Currently Enrolled: " + (students == null ? "null" : students.size()));

        if (availableSeats > 0 && students != null && !students.contains(student)) {
            students.add(student);
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean dropStudent(Student student) {
        if (students.remove(student)) {
            availableSeats++;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}
