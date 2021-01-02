package com.springboot2.essentials;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import com.springboot2.essentials.springboot2essentials.service.AnimeService;
import com.springboot2.essentials.unit.util.AnimeUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private AnimeUtil utilMock;

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeRepository.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeUtil.createValidAnime());

//        Mockito.when(animeRepository.save(AnimeUtil.createValidAnime()))
//                .thenReturn(AnimeUtil.createValidUpdatedAnime());

        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn((Page<Anime>) List.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeRepository.findAnimeByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeUtil.createValidAnime()));

        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("save return anime when successful")
    public void save_ReturnAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeUtil.createAnimeToBeSaved();

        Anime savedAnime = animeRepository.save(animeToBeSaved);

        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("findAll return list of animes when successful")
    public void findAll_ReturnListOfAnimes_WhenSuccessful(){
        List<Anime> animeList = animeService.findAll(PageRequest.of(1,1)).getContent();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.size()).isGreaterThan(0);

    }

    @Test
    @DisplayName("findBId return anime when successful")
    public void findById_ReturnAnime_WhenSuccessful(){
        Long expectedId = 1L;
        Anime animeFound = animeService.findById(1L);

        Assertions.assertThat(animeFound).isNotNull();
        Assertions.assertThat(animeFound.getId()).isNotNull();
        Assertions.assertThat(animeFound.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes anime and return empty when successful")
    public void delete_ReturnEmpty_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws exception when anime does not exist")
    public void delete_ThrowException_WhenNotFound(){
        //Mockito with this condition will be applied only for this specific test case
        BDDMockito.when(utilMock.findAnimeOrThrowException(ArgumentMatchers.anyLong(), ArgumentMatchers.any(AnimeRepository.class)))
                .thenThrow(new RuntimeException("Anime not found"));
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> animeService.delete(10));
    }
}
