package com.name.library.services;

import com.name.library.dtos.MemberRequestDTO;
import com.name.library.dtos.MemberResponseDTO;
import com.name.library.models.MemberModel;
import com.name.library.repositories.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemberService {

  @Inject MemberRepository memberRepository;

  @PostConstruct
  void initDummyMembers() {
    List<MemberRequestDTO> dummyMembers =
        List.of(
            new MemberRequestDTO("Abd-Ur-Rahman", "abdurrahman@gmail.me"),
            new MemberRequestDTO("Ali", "ali@gmail.com"));

    for (MemberRequestDTO dto : dummyMembers) {
      addMember(dto);
    }
  }

  public List<MemberResponseDTO> getAllMembers() {
    return memberRepository.getAllMembers().stream()
        .map(
            memberModel ->
                new MemberResponseDTO(memberModel.id, memberModel.name, memberModel.email))
        .collect(Collectors.toList());
  }

  public MemberResponseDTO addMember(MemberRequestDTO newMember) {
    MemberModel newMemberModel =
        memberRepository.addMember(new MemberModel(newMember.name(), newMember.email()));

    return new MemberResponseDTO(newMemberModel.id, newMemberModel.name, newMemberModel.email);
  }
}
