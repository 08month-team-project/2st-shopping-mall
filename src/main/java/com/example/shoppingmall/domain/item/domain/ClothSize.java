package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.ClothingSize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ClothSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cloth_size_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_name", nullable = false)
    private ClothingSize sizeName;

    protected ClothSize(ClothingSize sizeName) {
        this.sizeName = sizeName;
    }

    public static ClothSize of(ClothingSize sizeName) {
        return new ClothSize(sizeName);
    }
}
