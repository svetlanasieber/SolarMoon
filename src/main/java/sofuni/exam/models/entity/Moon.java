package sofuni.exam.models.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "moons")
public class Moon extends BaseEntity {

    private String name;

    private double radius;

    private int distanceFromPlanet;

    private LocalDate discovered;

    private Planet planet;

    private Discoverer discoverer;

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Column(name = "distance_from_planet")
    public int getDistanceFromPlanet() {
        return distanceFromPlanet;
    }

    public void setDistanceFromPlanet(int distanceFromPlanet) {
        this.distanceFromPlanet = distanceFromPlanet;
    }

    @Column(nullable = false)
        public LocalDate getDiscovered() {
        return discovered;
    }

    public void setDiscovered(LocalDate discovered) {
        this.discovered = discovered;
    }

    @ManyToOne
    @JoinColumn(name = "planet_id", nullable = false)
    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }
    @ManyToOne
    @JoinColumn(name = "discoverer_id", nullable = false)
    public Discoverer getDiscoverer() {
        return discoverer;
    }

    public void setDiscoverer(Discoverer discoverer) {
        this.discoverer = discoverer;
    }
}
