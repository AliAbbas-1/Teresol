package com.name.library.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.name.library.dto.MemberDto;
import com.name.library.services.MemberService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemberServiceImpl implements MemberService {

    private final Map<String, MemberDto> members = new ConcurrentHashMap<>();
 
    @Override
    public List<MemberDto> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    @Override
    public Optional<MemberDto> getMemberById(String id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public MemberDto addMember(MemberDto memberDto) {
        String newId = UUID.randomUUID().toString();
        memberDto.id = newId;
        members.put(newId, memberDto);
        return memberDto;
    }

    @Override
    public Optional<MemberDto> updateMember(String id, MemberDto memberDto) {
        if (members.containsKey(id)) {
            memberDto.id = id;
            members.put(id, memberDto);
            return Optional.of(memberDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteMember(String id) {
        members.remove(id);
    }
}