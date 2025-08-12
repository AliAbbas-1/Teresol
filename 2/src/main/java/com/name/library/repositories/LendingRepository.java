package com.name.library.repositories;

import com.name.library.models.LendingModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class LendingRepository {

  List<LendingModel> lendingModels = new ArrayList<>();

  public Optional<LendingModel> getLendingModel(UUID lendingId) {
    return lendingModels.stream()
        .filter(lendingModel -> lendingModel.lendingId.equals(lendingId))
        .findFirst();
  }

  public List<LendingModel> getAllLendingModels() {
    return lendingModels;
  }

  public void addLendingModel(LendingModel lendingModel) {
    lendingModels.add(lendingModel);
  }
}
