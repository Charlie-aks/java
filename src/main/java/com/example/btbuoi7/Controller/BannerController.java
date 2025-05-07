package com.example.btbuoi7.Controller;

import com.example.btbuoi7.DTO.BannerDTO;
import com.example.btbuoi7.Service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @GetMapping
    public ResponseEntity<List<BannerDTO>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerDTO> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(bannerService.getBannerById(id));
    }

    @PostMapping
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(bannerService.createBanner(bannerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(@PathVariable Long id, @RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(bannerService.updateBanner(id, bannerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok().build();
    }
} 