package com.springboot2.essentials.unit.util;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import org.springframework.stereotype.Component;

@Component
public final class AnimeUtil {
    private AnimeUtil(){}

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("South Park")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1L)
                .name("South Park")
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .id(1L)
                .name("South Park 2")
                .build();
    }

    public Anime findAnimeOrThrowException(long id, AnimeRepository animeRepository){
        return animeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found Exception"));
    }
}
