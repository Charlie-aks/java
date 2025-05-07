package com.example.btbuoi7.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String description;
    private String link;
    private Integer displayOrder;
    private Boolean isActive;
} 