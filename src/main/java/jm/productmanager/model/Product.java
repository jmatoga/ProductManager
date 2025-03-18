package jm.productmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "Product name should be between 3 and 20 characters and contain only letters and numbers")
    private String name;

    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Product price cannot be negative")
    private Double price;

    @Column(nullable = false)
    @Min(value = 0, message = "Product quantity cannot be negative")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    private LocalDateTime createdAt;
}
