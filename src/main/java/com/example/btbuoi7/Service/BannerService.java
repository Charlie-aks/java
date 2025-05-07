package com.example.btbuoi7.Service;

import com.example.btbuoi7.DTO.BannerDTO;
import com.example.btbuoi7.Entity.Banner;
import com.example.btbuoi7.Repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {
    @Autowired
    private BannerRepository bannerRepository;

    public List<BannerDTO> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BannerDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
        return convertToDTO(banner);
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {
        Banner banner = convertToEntity(bannerDTO);
        Banner savedBanner = bannerRepository.save(banner);
        return convertToDTO(savedBanner);
    }

    public BannerDTO updateBanner(Long id, BannerDTO bannerDTO) {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        existingBanner.setTitle(bannerDTO.getTitle());
        existingBanner.setImageUrl(bannerDTO.getImageUrl());
        existingBanner.setDescription(bannerDTO.getDescription());
        existingBanner.setLink(bannerDTO.getLink());
        existingBanner.setDisplayOrder(bannerDTO.getDisplayOrder());
        existingBanner.setIsActive(bannerDTO.getIsActive());

        Banner updatedBanner = bannerRepository.save(existingBanner);
        return convertToDTO(updatedBanner);
    }

    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new RuntimeException("Banner not found");
        }
        bannerRepository.deleteById(id);
    }

    private BannerDTO convertToDTO(Banner banner) {
        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setImageUrl(banner.getImageUrl());
        dto.setDescription(banner.getDescription());
        dto.setLink(banner.getLink());
        dto.setDisplayOrder(banner.getDisplayOrder());
        dto.setIsActive(banner.getIsActive());
        return dto;
    }

    private Banner convertToEntity(BannerDTO dto) {
        Banner banner = new Banner();
        banner.setTitle(dto.getTitle());
        banner.setImageUrl(dto.getImageUrl());
        banner.setDescription(dto.getDescription());
        banner.setLink(dto.getLink());
        banner.setDisplayOrder(dto.getDisplayOrder());
        banner.setIsActive(dto.getIsActive());
        return banner;
    }
} 