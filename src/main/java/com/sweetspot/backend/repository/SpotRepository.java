package com.sweetspot.backend.repository;

import com.sweetspot.backend.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    // カテゴリで絞り込むためのメソッド
    List<Spot> findByCategory(String category);
}