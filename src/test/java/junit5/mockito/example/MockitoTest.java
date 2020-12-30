package junit5.mockito.example;

import com.springboot2.essentials.springboot2essentials.domain.Anime;
import com.springboot2.essentials.springboot2essentials.repository.AnimeRepository;
import com.springboot2.essentials.springboot2essentials.service.AnimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoTest {

    @Mock
    AnimeRepository animeRepository;

    @Mock
    AnimeService animeService;

    /**
     * Mock using MockitoAnnotations
     */
    @Mock
    Map<String, String> mapMock; //similar with inline mock

    /**
     * BDD approach style that people are using includes given-when-then
     *
     * 'given' is similar to 'when'
     * 'verify' is similar to 'then'
     *
     * PS: when using TDD use 'when' and 'verify'
     *     when using BDD use 'given" and 'then' (and 'when' should handle the result of given itself)
     */
    @Test
    void bddTestPattern(){
        //given
        Anime anime = Anime.builder().build();
        given(animeRepository.findById(1L)).willReturn(Optional.of(anime));

        //when
        Anime foundAnime = animeService.findById(1L);

        //then
        assertEquals(anime, foundAnime);
        then(animeRepository).should().findById(anyLong());
        then(animeRepository).shouldHaveNoInteractions();
    }

    @Test
    void testInlineMock(){
        Map mapMock = Mockito.mock(Map.class);

        assertEquals(mapMock.size(), 0);
    }

    @Test
    void verifyTest(){
        mapMock.put("keypair", "value");

        mapMock.remove("keypair");
        mapMock.remove("keypair");
        verify(mapMock, times(2)).remove("keypair"); //verify will allow to test `remove` twice or the number of times specified, otherwise, exception will be thrown

        verify(mapMock).remove("keypair");//when times is not specified, the default wanted number of invocations times is 1
        verify(mapMock, atLeast(2)).remove("keypair");
        verify(mapMock, atMost(2)).remove("keypair");
        verify(mapMock, atLeastOnce()).remove("keypair");
        verify(mapMock, never()).remove("key");//verify will never be call along with remove(key)

        //Verification in BDD approach
        then(mapMock).should().remove(mapMock.remove("keypair"));
        then(mapMock).should(times(2)).remove(mapMock.remove("keypair"));
        then(mapMock).shouldHaveNoMoreInteractions();

        then(mapMock).shouldHaveNoInteractions();

    }
}
