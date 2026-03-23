package com.group3.bikehub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int currentPage;
    int totalPage;
    int pageSize;
    long totalElements;

    @Builder.Default
    List<T> data = Collections.emptyList();

    @Builder.Default
    Map<String, Object> meta = new HashMap<>();
}
