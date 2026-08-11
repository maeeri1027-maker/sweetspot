package com.sweetspot.backend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "spots")
@Getter
@Setter
@NoArgsConstructor
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String address;

    private String websiteUrl;
    private String snsUrl;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String category;

    // 新規追加項目
    private String status;   // 「行った」「気になる」「時間があれば」
    private String memo;     // 自由記述メモ

    // ★ 追加：行った日付（例: 2026-08-10）
    private LocalDate visitedDate;

    // スポット写真（最大5枚。先頭 = ホーム画面に表示するメイン写真）
    @ElementCollection
    @CollectionTable(name = "spot_images", joinColumns = @JoinColumn(name = "spot_id"))
    @OrderColumn(name = "image_order")
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>();

    // 味の感想
    @Column(length = 1000)
    private String tasteReview;
}