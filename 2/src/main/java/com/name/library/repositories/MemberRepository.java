package com.name.library.repositories;

import com.name.library.models.MemberModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MemberRepository {

  List<MemberModel> memberModels = new ArrayList<>();

  public Optional<MemberModel> getMember(String memberId) {
    return memberModels.stream().filter(memberModel -> memberModel.id.equals(memberId)).findFirst();
  }

  public List<MemberModel> getAllMembers() {
    return memberModels;
  }

  public MemberModel addMember(MemberModel newMemberModel) {
    memberModels.add(newMemberModel);
    return newMemberModel;
  }
}
