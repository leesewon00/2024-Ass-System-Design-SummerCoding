package org.landvibe.ass1.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Book {
    private Long id;
    private String title;
}
