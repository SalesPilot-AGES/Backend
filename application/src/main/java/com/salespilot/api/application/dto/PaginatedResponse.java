package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@JsonPropertyOrder({"content", "empty", "first", "last", "number", "numberOfElements", "pageable", "size", "sort", "totalElements", "totalPages", "message"})
public record PaginatedResponse<T>(
    @JsonProperty("content") List<T> content,
    @JsonProperty("empty") boolean empty,
    @JsonProperty("first") boolean first,
    @JsonProperty("last") boolean last,
    @JsonProperty("number") int number,
    @JsonProperty("numberOfElements") int numberOfElements,
    @JsonProperty("pageable") Pageable pageable,
    @JsonProperty("size") int size,
    @JsonProperty("sort") Sort sort,
    @JsonProperty("totalElements") long totalElements,
    @JsonProperty("totalPages") int totalPages,
    @JsonProperty("message") String message
) {
    public static <T> PaginatedResponse<T> from(Page<T> page, String message) {
        return new PaginatedResponse<>(
            page.getContent(),
            page.isEmpty(),
            page.isFirst(),
            page.isLast(),
            page.getNumber(),
            page.getNumberOfElements(),
            page.getPageable(),
            page.getSize(),
            page.getSort(),
            page.getTotalElements(),
            page.getTotalPages(),
            message
        );
    }
}
