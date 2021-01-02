package com.springboot2.essentials.integration;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import com.springboot2.essentials.unit.util.AnimeUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * To avoid setting up the database @MockBean can be used to mock database. Mock bean provides
     *MockBean is an annotation used to add mock object to spring application context.
     * If there is an existing Bean that matches the same type defined it will be replaced by this mock
     * If there is no existing Bean that matches the same type a new one will be created
     * This annotation is from Spring package used to add facilities to mockito for this specific scenario
     */
    @MockBean
    private AnimeRepository animeRepositoryMock;

    //When using mock, you need to setup it
    @BeforeEach //in Junit4 is equivalent to @Before
    public void setup(){
        //@formatter:off
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeUtil.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeUtil.createValidAnime());

//        Mockito.when(animeRepository.save(AnimeUtil.createValidAnime()))
//                .thenReturn(AnimeUtil.createValidUpdatedAnime());

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn((Page<Anime>) List.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findAnimeByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeUtil.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeUtil.createValidAnime()));

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
        //@formatter:on

    }


    @Test
    @DisplayName("return a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful(){

        String expectedName = AnimeUtil.createValidAnime().getName();

        //@formatter:off
        Page<List<Anime>> animes = testRestTemplate
                .exchange("/animes", HttpMethod.GET, null,
                        new ParameterizedTypeReference<Page<List<Anime>>>() {}).getBody();
        //@formatter:on

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes.toList()).isNotEmpty();
        Assertions.assertThat(animes.toList().get(0).get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById returns anime when successful")
    public void findById_ReturnAnime_WhenSuccessful(){
        long id = 1L;
        Anime anime = testRestTemplate.getForObject("/animes/"+id, Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(id);
        Assertions.assertThat(anime.getName()).isNotNull();

    }

    @Test
    @DisplayName("findByName returns anime when successful")
    public void findByName_ReturnAnime_WhenSuccessful(){
        String name = "South Park";
        //@formatter:off
        List<Anime> animeList = testRestTemplate.exchange("/search?name=" + name, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {}).getBody();
        //@formatter:on

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.size()).isGreaterThan(0);
        Assertions.assertThat(animeList.get(0)).isNotNull();

    }

    @Test
    @DisplayName("save returns anime when successful")
    public void save_ReturnAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeUtil.createAnimeToBeSaved();

        //@formatter:off
        ResponseEntity<Anime> response = testRestTemplate.exchange("/animes", HttpMethod.POST,
                new HttpEntity<>(animeToBeSaved, createJsonHeader()), Anime.class);
        //@formatter:on
        Anime anime = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getName()).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(animeToBeSaved.getName());

    }

    private HttpHeaders createJsonHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("delete removes anime when successful")
    public void delete_RemoveAnime_WhenSuccessful(){
        long id = 1L;
        ResponseEntity<Void> response = testRestTemplate.exchange("/animes/"+id, HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(response.getBody()).isInstanceOf(Void.class);

    }

}
