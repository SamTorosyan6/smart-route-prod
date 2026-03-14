package com.example.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationUtil {
    public static List<Integer> getPageNumbers(int totalPages) {
        return totalPages > 0
                ? IntStream.rangeClosed(1, totalPages).boxed().toList()
                : List.of();
    }
}
