package com.example.course.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member{

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "pw")
    private String pw;

    @Column(name = "active")
    private boolean active;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Instructor instructor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;






    public Member(Set<Roles> roles) {
        this.roles = roles;
    }



    @Override
    public String toString() {
        return "Member{" +
                "userId='" + userId + '\'' +
                ", pw='" + pw + '\'' +
                ", active=" + active +

                ", roles=" + (roles != null ? roles.stream().map(Roles::getRole).toList() : "null") +

                '}';
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Roles> roles;


    public Member(){

    }
    public Member(String userId,String pw, boolean active) {
        this.pw = pw;
        this.userId = userId;
        this.active = active;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Member orElseThrow(Object o) {
        return null;
    }
}
