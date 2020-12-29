package com.springboot2.essentials.unit.jpa;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

//@RunWith(SpringRunner.class) //Used to provide a bridge between springboot version-2 test features and JUnit. I the current version the annotation used is @ExtendedWith(SpringExtension.class)

@DataJpaTest
@DisplayName("Anime repo tests")
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) //Shows the test name in rest resolution without underscore
public class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("save anime when successful test case")
    public void save_PersistAnime_WhenSuccessful(){

        Anime anime = createAnime();

        Anime savedAnime = animeRepository.save(anime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("update anime when successful test case")
    public void update_PersistAnime_WhenSuccessful(){

        Anime anime = createAnime();

        Anime savedAnime = animeRepository.save(anime);

        savedAnime.setName("South Park");

        Anime updatedAnime = animeRepository.save(savedAnime);

        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("delete anime when successful test case")
    public void delete_PersistAnime_WhenSuccessful(){

        Anime anime = createAnime();

        Anime savedAnime = animeRepository.save(anime);

        animeRepository.delete(savedAnime);

        Optional<Anime> animeRemoved = animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeRemoved.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("find anime by name when successful test case")
    public void find_PersistAnime_By_Name_WhenSuccessful(){

        Anime anime = createAnime();
        Anime savedAnime = animeRepository.save(anime);

        List<Anime> animeList = animeRepository.findAnimeByName(savedAnime.getName());

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(savedAnime);
    }

    @Test
    @DisplayName("find anime by name return empty when not found")
    public void findByName_return_Empty_WhenAnimeNotFound(){

        Anime anime = createAnime();
        Anime savedAnime = animeRepository.save(anime);

        List<Anime> animeList = animeRepository.findAnimeByName("South Park");

        Assertions.assertThat(animeList).isEmpty();
        Assertions.assertThat(animeList).doesNotContain(savedAnime);
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    public void save_throwConstrainViolationException_WhenNameIsEmpty_Using_assertThatThrownBy(){
        Anime anime = new Anime();

        //Testing Exception using assertThatThrownBy
        Assertions.assertThatThrownBy(() -> animeRepository.save(anime))
        .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    public void save_throwConstrainViolationException_WhenNameIsEmpty_Using_assertThatExceptionOfType(){
        Anime anime = new Anime();

        //Testing Exception using assertThatExceptionOfType
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> animeRepository.save(anime))
                .withMessageContaining("The name of this anime cannot be empty");

    }

    private Anime createAnime(){
        return Anime.builder()
                .name("Rick and Marty")
                .build();
    }

    @Test
    @DisplayName("Assumptions test. https://junit.org/junit5/docs/5.0.0/api/org/junit/jupiter/api/Assumptions.html")
    //"In direct contrast to failed assertions, failed assumptions do not result in a test failure; rather,
        // a failed assumption results in a test being aborted.
    void testAssumptions(){
        Assumptions.assumeThat(Boolean.TRUE).isTrue();
    }
}
