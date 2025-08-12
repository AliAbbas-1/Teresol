package com.name.library.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MemberModel {

  public UUID id;
  public String name;
  public String email;
  public String password; // this is here for no reason
  public List<UUID> borrowedBookModels;
  public Date joinedAt;

  public MemberModel(String name, String email, String password) {
    this.id = UUID.randomUUID();

    this.name = name;
    this.email = email;
    this.password = password;

    this.borrowedBookModels = new ArrayList<>();
    this.joinedAt = new Date();
  }
}
