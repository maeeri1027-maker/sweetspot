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

    // 行った日付
    private LocalDate visitedDate;

    // スポット写真（Base64の長文データを保存できるよう TEXT 型に変更）
    @ElementCollection
    @CollectionTable(name = "spot_images", joinColumns = @JoinColumn(name = "spot_id"))
    @OrderColumn(name = "image_order")
    @Column(name = "image_url", columnDefinition = "TEXT") // ★ length = 500 から書き換え
    private List<String> imageUrls = new ArrayList<>();

    // 味の感想
    @Column(length = 1000)
    private String tasteReview;
}