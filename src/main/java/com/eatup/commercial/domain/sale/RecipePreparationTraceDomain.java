package com.eatup.commercial.domain.sale;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipe_preparation_traces")
public class RecipePreparationTraceDomain {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "sale_id", nullable = false)
    private UUID saleId;

    @Column(name = "sale_detail_id", nullable = false)
    private UUID saleDetailId;

    @Column(name = "recipe_id", nullable = false)
    private UUID recipeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipePreparationTraceStatus status;

    @Column(nullable = false)
    private String observation;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;
}
