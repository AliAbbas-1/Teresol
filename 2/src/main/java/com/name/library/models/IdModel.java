package com.name.library.models;

import java.util.EnumMap;
import java.util.Map;

public class IdModel {

  public enum Type {
    BOOK,
    MEMB,
    LEND
  }

  private static final Map<Type, Integer> counters = new EnumMap<>(Type.class);

  static {
    for (Type type : Type.values()) {
      counters.put(type, 0);
    }
  }

  public static String generate(Type type) {
    int count = counters.get(type);
    counters.put(type, count + 1);

    String formattedNumber = String.format("%04d", count);
    return type.name() + "-" + formattedNumber;
  }
}
