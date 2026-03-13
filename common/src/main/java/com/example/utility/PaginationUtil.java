package com.example.utility;


import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class PaginationUtil {
    private PaginationUtil() {
    }

    public static List<Integer> getPageNumbers(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages).boxed().toList();
        }
        return Collections.emptyList();
    }

}
