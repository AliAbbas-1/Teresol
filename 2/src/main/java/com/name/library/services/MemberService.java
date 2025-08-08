package com.name.library.services;

import com.name.library.dtos.MemberCreateRequestDTO;
import com.name.library.dtos.MemberResponseDTO;
import com.name.library.models.MemberModel;
import com.name.library.repositories.MemberRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemberService {
    @Inject
    MemberRepository memberRepository;

    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.getAllMembers().stream()
                .map(memberModel -> new MemberResponseDTO(
                        memberModel.id,
                        memberModel.name,
                        memberModel.email
                ))
                .collect(Collectors.toList());
    }

    public void addMember(MemberCreateRequestDTO newMember) {
        memberRepository.addMember(new MemberModel(
                newMember.name(),
                newMember.email(),
                newMember.password()
        ));
    }
}
