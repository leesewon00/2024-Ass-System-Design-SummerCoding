package org.landvibe.ass1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class BookRequestDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class CreateDTO {
        @NotBlank
        private String title;
    }
}
