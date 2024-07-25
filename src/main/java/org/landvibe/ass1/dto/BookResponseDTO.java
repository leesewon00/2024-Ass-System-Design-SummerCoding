package org.landvibe.ass1.dto;

import lombok.*;

public class BookResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class CreateResultDTO {
        private Long id;
        private String title;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class UpdateResultDTO {
        private Long id;
        private String title;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class FindResultDTO {
        private Long id;
        private String title;
    }
}
