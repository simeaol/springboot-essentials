package com.springboot2.essentials;

import com.springboot2.essentials.springboot2essentials.controller.AnimeController;
import com.springboot2.essentials.springboot2essentials.domain.Anime;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * There are two options to test controller:
 *          SpringBootTest -> Spring will try to load the context so all dependencies as db will be required
 *          ExtendedWith(SpringExtension.class) -> We can still using spring features without the need to load context
 */
@ExtendWith(SpringExtension.class)
public class AnimeControllerTest {

    @InjectMocks //Used to create class instances that will be used on the class test (the target class)
    private AnimeController animeController;

    @Mock //Used to create class instance that is support the target (in this case the target is AnimeController and the support class is AnimeService)
    private AnimeService animeServiceMock;

    //When using mock, you need to setup it
    @BeforeEach //in Junit4 is equivalent to @Before
    public void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeUtil.createValidAnime()));
        BDDMockito.when(animeServiceMock.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeUtil.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(AnimeUtil.createAnimeToBeSaved()))//anime to be saved does not contains ID
                .thenReturn(AnimeUtil.createValidAnime());//saved Anime should contains ID because it is auto-generated

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

    }


    @Test
    @DisplayName("return a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();

        Page<Anime> animes = animeController.findAll(PageRequest.of(1,1)).getBody();

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes.toList()).isNotEmpty();
        Assertions.assertThat(animes.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById returns anime when successful")
    public void findById_ReturnAnime_WhenSuccessful(){
        Anime validAnime = AnimeUtil.createValidAnime();
        Anime anime = animeController.findById(validAnime.getId()).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
        Assertions.assertThat(anime.getName()).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(validAnime.getName());

    }

    @Test
    @DisplayName("findByName returns anime when successful")
    public void findByName_ReturnAnime_WhenSuccessful(){
        Anime validAnime = AnimeUtil.createValidAnime();
        List<Anime> animeList = animeController.findByName(validAnime.getName()).getBody();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.size()).isGreaterThan(0);
        Assertions.assertThat(animeList.get(0)).isNotNull();

    }

    @Test
    @DisplayName("save returns anime when successful")
    public void save_ReturnAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeUtil.createAnimeToBeSaved();

        ResponseEntity<Anime> response = animeController.save(animeToBeSaved);
        Anime anime = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getName()).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(animeToBeSaved.getName());

    }

    @Test
    @DisplayName("delete removes anime when successful")
    public void delete_RemoveAnime_WhenSuccessful(){
        ResponseEntity<Void> response = animeController.delete(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(response.getBody()).isInstanceOf(Void.class);

    }


}
