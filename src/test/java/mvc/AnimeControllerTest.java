package mvc;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.service.AnimeService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
//@SpringJUnitWebConfig(classes = {FakeConfig.class}, locations = {"classpath:xml-file-config.xml"})//locations: used to locate xml bean config to be injected into context
public class AnimeControllerTest {

    @InjectMocks
    AnimeControllerTest controller;

    @Mock
    AnimeService animeService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        given(animeService.findAll(null)).willReturn(null);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown(){
        reset(animeService);
    }

    @Test
    void testControllerShowAnimes() throws Exception {
        mockMvc.perform(get("/animes.html"))//GetMapping value
                .andExpect(status().isOk())//expected http response status
                .andExpect(model().attributeExists("animes"))//attribute add to view
                .andExpect(view().name("animes/animeList"));//view name
    }

    @Test
    void testGetCreationFormTest() throws Exception {
         mockMvc.perform(get("/animes/new"))
                 .andExpect(status().isOk())
                 .andExpect(model().attributeExists("anime"))
                 .andExpect(view().name("animes/createOrUpdateAnimeForm"));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        var ex = """
            @GetMapping("/animes)
            public processFindForm(Anime anime, BindingResult result, Model model){
                if(anime.getName() ==  null) anime.setName("");
                 List<Anime> found = animeRepository.findByName(anime.getName());
                 if(found.isEmpty()){
                    result.rejectValue("name", "not Found", "not Found");
                    return "animes/findAnimes";
                 }
                 
                 else if(found.size() == 1){
                    anime = found.iterator().next();
                    return "redirect:/animes/" + anime.getId();
                 }
                 
                 model.put("animes", found);
                 return "animes/findAnimes";
                 ...
            }
            """;
        mockMvc.perform(get("/animes")
                    .param("name", "not find ME!")) //will bind "not find ME" into  Anime.name object provided
                .andExpect(status().isOk())
                .andExpect(view().name("animes/findAnimes"));

    }

    @Test
    void testFindByName() throws Exception {
        Anime anime = new Anime();
        anime.setId(1L);
        anime.setName("South Park");
        given(animeService.findByName("South Park")).willReturn(Lists.newArrayList(anime));

        mockMvc.perform(get("/animes")
                    .param("name", "South Park"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animes/1"));//1 because the anime id was set to one

        then(animeService).should().findByName(anyString());

    }

    @Test
    void testValidFormPost() throws Exception {
        mockMvc.perform(post("/animes/new")
                    .param("id", "1")
                    .param("name", "South Park"))
                .andExpect(status().isCreated())
                .andExpect(view().name("animes/createOrUpdateAnimeForm"));

    }

    @Test
    void testNotValidFormPost() throws Exception {
        var ex = """
                @PostMapping("animes/new")
                public String processCreation(Anime anime, BindingResult result){
                    if(result.hasErrors()){
                        return "createOrUpdateAnimeForm";
                    }
                    animeService.save(anime);
                    retunr "redirect:/animes/"+anime.getId();
                }
                """;
        mockMvc.perform(post("/animes/new")
                    .param("id", null)
                    .param("name", null))
                .andExpect(status().is4xxClientError())
                .andExpect(model().attributeHasErrors("id"))
                .andExpect(model().attributeHasFieldErrors("anime", "id", "name"))
                .andExpect(view().name("animes/findAnimes"));
    }

    @Test
    void testUpdateAnime() throws Exception {
        mockMvc.perform(put("/animes/{id}", 1)
                    .param("name", "Ricky & Marty"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/animes/{id}"));
    }
}
