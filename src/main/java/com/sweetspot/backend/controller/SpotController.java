package com.sweetspot.backend.controller;

import com.sweetspot.backend.entity.Spot;
import com.sweetspot.backend.service.SpotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin(origins = "*") // Reactからの通信を許可
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    // ① 一覧取得・カテゴリ絞り込み（GET http://localhost:8080/api/spots?category=カフェ）
    @GetMapping
    public List<Spot> getAllSpots(@RequestParam(required = false) String category) {
        return spotService.getAllSpots(category);
    }

    // ② 1件取得（GET http://localhost:8080/api/spots/1）
    @GetMapping("/{id}")
    public Spot getSpotById(@PathVariable Long id) {
        return spotService.getSpotById(id);
    }

    // ③ 新規登録（POST http://localhost:8080/api/spots）
    @PostMapping
    public Spot createSpot(@RequestBody Spot spot) {
        return spotService.createSpot(spot);
    }

    // ④ 更新（PUT http://localhost:8080/api/spots/1）
    @PutMapping("/{id}")
    public Spot updateSpot(@PathVariable Long id, @RequestBody Spot spot) {
        return spotService.updateSpot(id, spot);
    }

    // ⑤ 削除（DELETE http://localhost:8080/api/spots/1）
    @DeleteMapping("/{id}")
    public void deleteSpot(@PathVariable Long id) {
        spotService.deleteSpot(id);
    }
}