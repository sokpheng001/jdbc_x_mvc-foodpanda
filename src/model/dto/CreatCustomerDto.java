package model.dto;

import lombok.Builder;

@Builder
public record CreatCustomerDto(
        String name,
        String email,
        String password
) {
}
