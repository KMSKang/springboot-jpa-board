package com.board.www.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class CommonUtils {
    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String getDateFormat(LocalDateTime createdAt, String pattern) {
        return createdAt.format(DateTimeFormatter.ofPattern(pattern, Locale.KOREAN));
    }
}
