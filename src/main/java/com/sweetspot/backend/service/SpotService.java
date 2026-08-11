package com.sweetspot.backend.service;

import com.sweetspot.backend.entity.Spot;
import com.sweetspot.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotService {

    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public List<Spot> getAllSpots(String category) {
        if (category != null && !category.isEmpty()) {
            return spotRepository.findByCategory(category);
        }
        return spotRepository.findAll();
    }

    public Spot getSpotById(Long id) {
        return spotRepository.findById(id).orElse(null);
    }

    public Spot createSpot(Spot spot) {
        return spotRepository.save(spot);
    }

    public Spot updateSpot(Long id, Spot spotDetails) {
        Spot spot = spotRepository.findById(id).orElse(null);
        if (spot != null) {
            spot.setName(spotDetails.getName());
            spot.setDescription(spotDetails.getDescription());
            spot.setAddress(spotDetails.getAddress());
            spot.setLatitude(spotDetails.getLatitude());
            spot.setLongitude(spotDetails.getLongitude());
            spot.setWebsiteUrl(spotDetails.getWebsiteUrl());
            spot.setSnsUrl(spotDetails.getSnsUrl());
            spot.setCategory(spotDetails.getCategory());
            
            // 新規追加項目のセット
            spot.setStatus(spotDetails.getStatus());
            spot.setMemo(spotDetails.getMemo());
            spot.setTasteReview(spotDetails.getTasteReview());

            // ★ 追記：行った日付のセット
            spot.setVisitedDate(spotDetails.getVisitedDate());

            // 写真（先頭 = メイン写真、以降 = サブ写真）を丸ごと入れ替え
            spot.getImageUrls().clear();
            if (spotDetails.getImageUrls() != null) {
                spot.getImageUrls().addAll(spotDetails.getImageUrls());
            }
            
            return spotRepository.save(spot);
        }
        return null;
    }

    public void deleteSpot(Long id) {
        spotRepository.deleteById(id);
    }
}