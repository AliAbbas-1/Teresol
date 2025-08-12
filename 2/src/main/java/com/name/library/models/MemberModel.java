package com.name.library.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {

  public String id;
  public String name;
  public String email;
  public List<String> borrowedBookModels;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  public Instant joinedAt;

  public MemberModel(String name, String email) {
    this.id = IdModel.generate(IdModel.Type.MEMB);

    this.name = name;
    this.email = email;

    this.borrowedBookModels = new ArrayList<>();
    this.joinedAt = Instant.now();
  }
}
