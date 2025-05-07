package com.example.btbuoi7.Service;

import com.example.btbuoi7.DTO.MenuDTO;
import com.example.btbuoi7.Entity.Menu;
import com.example.btbuoi7.Repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<MenuDTO> getAllMenus() {
        List<Menu> rootMenus = menuRepository.findByParentIdIsNull();
        return rootMenus.stream()
                .map(this::convertToDTOWithChildren)
                .collect(Collectors.toList());
    }

    public MenuDTO getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        return convertToDTOWithChildren(menu);
    }

    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = convertToEntity(menuDTO);
        Menu savedMenu = menuRepository.save(menu);
        return convertToDTO(savedMenu);
    }

    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        existingMenu.setName(menuDTO.getName());
        existingMenu.setDescription(menuDTO.getDescription());
        existingMenu.setUrl(menuDTO.getUrl());
        existingMenu.setIcon(menuDTO.getIcon());
        existingMenu.setDisplayOrder(menuDTO.getDisplayOrder());
        existingMenu.setParentId(menuDTO.getParentId());
        existingMenu.setIsActive(menuDTO.getIsActive());

        Menu updatedMenu = menuRepository.save(existingMenu);
        return convertToDTO(updatedMenu);
    }

    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("Menu not found");
        }
        // Check if this menu has children
        List<Menu> children = menuRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("Cannot delete menu with children. Please delete children first.");
        }
        menuRepository.deleteById(id);
    }

    private MenuDTO convertToDTOWithChildren(Menu menu) {
        MenuDTO dto = convertToDTO(menu);
        List<Menu> children = menuRepository.findByParentId(menu.getId());
        if (!children.isEmpty()) {
            dto.setChildren(children.stream()
                    .map(this::convertToDTOWithChildren)
                    .collect(Collectors.toList()));
        } else {
            dto.setChildren(new ArrayList<>());
        }
        return dto;
    }

    private MenuDTO convertToDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setDescription(menu.getDescription());
        dto.setUrl(menu.getUrl());
        dto.setIcon(menu.getIcon());
        dto.setDisplayOrder(menu.getDisplayOrder());
        dto.setParentId(menu.getParentId());
        dto.setIsActive(menu.getIsActive());
        return dto;
    }

    private Menu convertToEntity(MenuDTO dto) {
        Menu menu = new Menu();
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());
        menu.setUrl(dto.getUrl());
        menu.setIcon(dto.getIcon());
        menu.setDisplayOrder(dto.getDisplayOrder());
        menu.setParentId(dto.getParentId());
        menu.setIsActive(dto.getIsActive());
        return menu;
    }
} 