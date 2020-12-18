package com.springboot2.essentials.springboot2essentials.repository;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends PagingAndSortingRepository<Anime, Long> {

    List<Anime> findAnimeByName(String name);

}
