package com.example.course.service;

import com.example.course.dao.MemberRepository;
import com.example.course.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Member findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }
}
