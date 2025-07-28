package sofuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sofuni.exam.models.entity.Moon;
import sofuni.exam.models.enums.Type;

import java.util.List;

@Repository
public interface MoonRepository extends JpaRepository<Moon, Long> {

    Moon findMoonByName(String name);

    List<Moon> findMoonsByPlanetTypeAndRadiusBetweenOrderByName(Type planetType, double radius, double radius2);

    @Query("select m from Moon m join Planet p on p.id = m.planet.id where p.type = 'GAS_GIANT'" +
            "and m.radius between 700 and 2000 order by m.name")
    List<Moon> findMoonsOrbitsGasGiantsAndRadiusBetween700and2000();
}
