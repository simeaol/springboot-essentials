package junit5.mockito.example;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.service.AnimeService;
import com.springboot2.essentials.unit.util.AnimeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoSpyTest {

    @Spy //give the wrapper of the real implementation
    AnimeService animeService;

    @Test
    void test(){
        Anime anime = AnimeUtil.createValidAnime();
        animeService.save(anime);
        Anime anime2 = AnimeUtil.createValidAnime();
        anime2.setId(2L);
        animeService.save(anime2);

        //given
        given(animeService.findById(anyLong())).willCallRealMethod();

        //when
        Anime animeFound = animeService.findById(1L);

        //then
        assertThat(animeFound).isNotNull();
        verify(animeService, times(1)).findById(anyLong());
    }
}
