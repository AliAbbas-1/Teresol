 package com.name.library.services;

import java.util.List;
import java.util.Optional;

import com.name.library.dto.MemberDto;

public interface MemberService {

    List<MemberDto> getAllMembers();

    Optional<MemberDto> getMemberById(String id);

    MemberDto addMember(MemberDto memberDto);

    Optional<MemberDto> updateMember(String id, MemberDto memberDto);

    void deleteMember(String id);

}