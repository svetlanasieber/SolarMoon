package sofuni.exam.service.Impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.MoonSeedRootDTO;
import sofuni.exam.models.entity.Discoverer;
import sofuni.exam.models.entity.Moon;
import sofuni.exam.models.entity.Planet;
import sofuni.exam.repository.DiscovererRepository;
import sofuni.exam.repository.MoonRepository;
import sofuni.exam.repository.PlanetRepository;
import sofuni.exam.service.MoonService;
import sofuni.exam.util.ValidationUtil;
import sofuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class MoonServiceImpl implements MoonService {

    private final MoonRepository moonRepository;

    private final PlanetRepository planetRepository;

    private final DiscovererRepository discovererRepository;
    private ValidationUtil validationUtil;

    private XmlParser xmlParser;
    private ModelMapper modelMapper;

    private static final String MOON_FILE_PATH = "src/main/resources/files/xml/moons.xml";

    public MoonServiceImpl(MoonRepository moonRepository, PlanetRepository planetRepository, DiscovererRepository discovererRepository, ValidationUtil validationUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.moonRepository = moonRepository;
        this.planetRepository = planetRepository;
        this.discovererRepository = discovererRepository;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return moonRepository.count() > 0;
    }

    @Override
    public String readMoonsFileContent() throws IOException {
        return Files.readString(Path.of(MOON_FILE_PATH));
    }

    @Override
    public String importMoons() throws IOException, JAXBException {

        StringBuilder build = new StringBuilder();

        xmlParser.fromFile(MOON_FILE_PATH, MoonSeedRootDTO.class)
                .getMoonSeedDTOList()
                .stream()
                .filter(moonSeedDTO -> {
                    boolean isValid = validationUtil.isValid(moonSeedDTO);

                    Moon moon = moonRepository.findMoonByName(moonSeedDTO.getName());
                    if (moon != null) {
                        isValid = false;
                    }

                    build.append(isValid
                                    ? String.format("Successfully imported moon %s",
                                    moonSeedDTO.getName())
                                    : "Invalid moon")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(moonSeedDTO -> {
                    Moon moon = modelMapper.map(moonSeedDTO, Moon.class);


                    Planet planet = planetRepository.findById(moonSeedDTO.getPlanet()).orElse(null);
                    Discoverer discoverer = discovererRepository.findById(moonSeedDTO.getDiscoverer()).orElse(null);

                    moon.setPlanet(planet);
                    moon.setDiscoverer(discoverer);
//                .map(moonSeedDTO -> modelMapper.map(moonSeedDTO, Moon.class))

                    return moon;
                })
                .forEach(moonRepository::save);

        return build.toString();
    }

    @Override
    public String exportMoons() {

        StringBuilder build = new StringBuilder();

//        List<Moon> foundMoons = moonRepository.findMoonsByPlanetTypeAndRadiusBetweenOrderByName(Type.GAS_GIANT, 700,2000);
        List<Moon> foundMoons = moonRepository.findMoonsOrbitsGasGiantsAndRadiusBetween700and2000();

        foundMoons.forEach(m -> {
            build.append(String.format("""
                                    ***Moon %s is a natural satellite of %s and has a radius of %.2f km.
                                    ****Discovered by %s %s
                                    """,
                    m.getName(),
                    m.getPlanet().getName(),
                    m.getRadius(),
                    m.getDiscoverer().getFirstName(),
                    m.getDiscoverer().getLastName()))
                    .append(System.lineSeparator());

        });
        return build.toString();
    }


}
