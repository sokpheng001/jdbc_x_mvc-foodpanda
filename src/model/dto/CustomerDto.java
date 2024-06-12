package model.dto;


import lombok.Builder;

@Builder
public record CustomerDto(
        Integer id,
        String name,
        String email
) {
}
