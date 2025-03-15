package jm.productmanager.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products_history")
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EFieldName fieldName;

    @Column(nullable = false)
    private String newValue;

    @Column(nullable = false)
    private String oldValue;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
