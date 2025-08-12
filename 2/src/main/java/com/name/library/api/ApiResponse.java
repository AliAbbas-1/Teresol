package com.name.library.api;

import jakarta.annotation.Nullable;

public record ApiResponse<T>(String status, @Nullable T data, @Nullable ApiError error) {}
