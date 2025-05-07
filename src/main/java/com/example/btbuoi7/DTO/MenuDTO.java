package com.example.btbuoi7.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private Long id;
    private String name;
    private String description;
    private String url;
    private String icon;
    private Integer displayOrder;
    private Long parentId;
    private Boolean isActive;
    private List<MenuDTO> children;
} 