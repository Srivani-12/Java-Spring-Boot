package com.example.course.service;

import com.example.course.dao.MemberRepository;
import com.example.course.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(username);
        if(member == null){
            throw new UsernameNotFoundException("user not found");
        }
        Set<SimpleGrantedAuthority> authorities = member.getRoles()
                .stream().map(role->new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toSet());
        return new User(member.getUserId(),member.getPw(),authorities);


    }
}
