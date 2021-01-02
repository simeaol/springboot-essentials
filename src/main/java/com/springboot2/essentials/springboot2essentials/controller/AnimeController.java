package com.springboot2.essentials.springboot2essentials.controller;

import com.springboot2.essentials.springboot2essentials.service.AnimeService;
import com.springboot2.essentials.springboot2essentials.domain.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("animes")
public class AnimeController {

    private AnimeService animeService;

    @Autowired
    public AnimeController(AnimeService animeService){
        this.animeService = animeService;
    }

    @GetMapping("/heath")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body(new String("hello world!"));
    }

    @PostMapping
    public ResponseEntity<Anime> save(Anime anime){
        Objects.requireNonNull(anime, "Anime cannot be null");
        return ResponseEntity.status(HttpStatus.CREATED).body(animeService.save(anime));
    }

    @PutMapping
    public ResponseEntity<Anime> update(Anime anime, long id){
        Assert.notNull(anime, "Anime cannot be null");
        return ResponseEntity.ok().body(animeService.update(anime, id));
    }

    @PutMapping
    public ResponseEntity<Void> delete(Long id){
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Anime>> findAll(Pageable pageable){
        return ResponseEntity.ok(animeService.findAll(pageable));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Anime>> findByName(@RequestParam("name") String name){
        Assert.notNull(name, "name cannot be null");
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(animeService.findById(id));
    }



}
