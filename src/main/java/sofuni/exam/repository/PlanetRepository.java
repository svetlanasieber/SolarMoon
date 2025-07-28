package sofuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sofuni.exam.models.entity.Planet;

import java.util.Optional;

public interface PlanetRepository extends JpaRepository<Planet, Long> {


    Optional<Planet> findPlanetByName(String name);

}
