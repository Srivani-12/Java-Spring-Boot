package com.example.course.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    public  Roles(){

    }

    public Roles(String role, Member member) {
        this.role = role;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", memberId=" + (member != null ? member.getUserId() : "null") +

                '}';
    }

    // Getters and setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public Member getMember() { return member; }

    public void setMember(Member member) { this.member = member; }
}

