package sofuni.exam.areImported;
//TestPlanetServiceAreImportedFalse

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sofuni.exam.repository.PlanetRepository;
import sofuni.exam.service.Impl.PlanetServiceImpl;


@ExtendWith(MockitoExtension.class)
public class TestPlanetServiceAreImportedFalse {


    @InjectMocks
    private PlanetServiceImpl planetService;
    @Mock
    private PlanetRepository planetRepository;

    @Test
    void areImportedShouldReturnFalse() {
        Mockito.when(planetRepository.count()).thenReturn(0L);
//        boolean areImported = planetRepository.count() == 0;
        Assertions.assertFalse(planetService.areImported());
    }
}