package com.example.btbuoi7.Repository;

import com.example.btbuoi7.Entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentIdIsNull();
    List<Menu> findByParentId(Long parentId);
} 