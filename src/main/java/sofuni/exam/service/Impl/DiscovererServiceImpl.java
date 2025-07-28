package sofuni.exam.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.DiscovererSeedDTO;
import sofuni.exam.models.entity.Discoverer;
import sofuni.exam.repository.DiscovererRepository;
import sofuni.exam.service.DiscovererService;
import sofuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class DiscovererServiceImpl implements DiscovererService {

    private DiscovererRepository discovererRepository;

    private final Gson gson;
    private ValidationUtil validationUtil;

    private ModelMapper modelMapper;
    private static final String DISCOVERER_FILE_PATH = "src/main/resources/files/json/discoverers.json";

    public DiscovererServiceImpl(DiscovererRepository discovererRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.discovererRepository = discovererRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return discovererRepository.count() > 0;
    }

    @Override
    public String readDiscovererFileContent() throws IOException {
        return Files.readString(Path.of(DISCOVERER_FILE_PATH));
    }

    @Override
    public String importDiscoverers() throws IOException {

        StringBuilder build = new StringBuilder();

        Arrays.stream(gson.fromJson(readDiscovererFileContent(), DiscovererSeedDTO[].class))
                .filter(discovererSeedDTO -> {
                    boolean isValid = validationUtil.isValid(discovererSeedDTO);

//                    Discoverer discovererByFirstName = discovererRepository.findDiscovererByFirstName(discovererSeedDTO.getFirstName());
//                    Discoverer discovererByLastName = discovererRepository.findDiscovererByLastName(discovererSeedDTO.getLastName());
//
//                    if(discovererByLastName != null) {
//                        isValid = false;
//                    }

                    Optional<Discoverer> discovererByNames = discovererRepository.findByFirstNameAndLastName(discovererSeedDTO.getFirstName(),
                            discovererSeedDTO.getLastName());

                    if (discovererByNames.isPresent()) {
                        isValid = false;
                    }

                    build.append(isValid
                    ? String.format("Successfully imported discoverer %s %s",
                            discovererSeedDTO.getFirstName(),
                            discovererSeedDTO.getLastName())
                            : "Invalid discoverer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(discovererSeedDTO -> modelMapper.map(discovererSeedDTO, Discoverer.class))
                .forEach(discovererRepository::save);

        return build.toString();
    }
}
