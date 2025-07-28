package sofuni.exam.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.PlanetSeedDTO;
import sofuni.exam.models.entity.Planet;
import sofuni.exam.repository.PlanetRepository;
import sofuni.exam.service.PlanetService;
import sofuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;


@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;
    private final Gson gson;

    private final ValidationUtil validationUtil;

    private static final String PLANET_FILE_PATH = "src/main/resources/files/json/planets.json";

    public PlanetServiceImpl(PlanetRepository planetRepository, Gson gson, ValidationUtil validationUtil) {
        this.planetRepository = planetRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return planetRepository.count() > 0;
    }

    @Override
    public String readPlanetsFileContent() throws IOException {
        return Files
                .readString(Path.of(PLANET_FILE_PATH));
    }

    @Override
    public String importPlanets() throws IOException {

        StringBuilder build = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();

        Arrays.stream(objectMapper.readValue(readPlanetsFileContent(), PlanetSeedDTO[].class))
                .filter(planetSeedDTO -> {
                    boolean isValid = validationUtil.isValid(planetSeedDTO);

                    Optional<Planet> planetByName = planetRepository.findPlanetByName(planetSeedDTO.getName());
                    if (planetByName.isPresent()) {
                        isValid = false;
                    }


                    build.append(isValid
                                    ? String.format("Successfully imported planet %s"
                                    , planetSeedDTO.getName())
                                    : "Invalid planet")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(planetSeedDTO -> modelMapper.map(planetSeedDTO, Planet.class))
                .forEach(planetRepository::save);

        return build.toString();
    }
}
