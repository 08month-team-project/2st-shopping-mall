package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class ClothingSize {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "clothing_size_id")
    private Long id;

    @Column(nullable = false, name = "size_name")
    @Enumerated(STRING)
    private ClothingSizeName sizeName;

}
