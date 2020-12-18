package com.springboot2.essentials.springboot2essentials;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnimeService {

    private AnimeRepository animeRepository;

    public Anime save(Anime anime){
        return animeRepository.save(anime);
    }

    public Page<Anime> findAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> findByName(String name){
        return animeRepository.findAnimeByName(name);
    }

    public Anime update(Anime anime, long id){
        Optional<Anime> animeFound = animeRepository.findById(id);
        animeFound.map(a -> {
            Anime updatedAnime = animeRepository.save(anime);
            return updatedAnime;
        }).orElseThrow(() -> new EntityNotFoundException("Cannot found anime. Operation not performed"));
        return null; //TODO: will never be called
    }

    public void delete(long id){
        Optional<Anime> animeFound = animeRepository.findById(id);
        animeFound.map(a -> {
            animeRepository.delete(a);
            return Optional.empty();
        }).orElseThrow(() -> new EntityNotFoundException("Anime with id not found."));
    }

    public Anime findById(Long id) {
        return animeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Anime not found"));
    }
}
