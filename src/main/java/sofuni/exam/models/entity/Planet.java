package sofuni.exam.models.entity;


import jakarta.persistence.*;
import sofuni.exam.models.enums.Type;

import java.util.Set;

@Entity
@Table(name = "planets")
public class Planet extends BaseEntity {

    private String name;

    private Type type;
    private int diameter;
    private double orbitalPeriod;

    private Long distanceFromSun;

//    private Set<Moon> moons;

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    @Column(nullable = false)
    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Column(name = "orbital_period", nullable = false)
    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(double orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    @Column(name = "distance_from_sun", nullable = false)
    public Long getDistanceFromSun() {
        return distanceFromSun;
    }

    public void setDistanceFromSun(Long distanceFromSun) {
        this.distanceFromSun = distanceFromSun;
    }

//    @OneToMany(mappedBy = "planet", fetch = FetchType.EAGER)
//    public Set<Moon> getMoons() {
//        return moons;
//    }
//
//    public void setMoons(Set<Moon> moons) {
//        this.moons = moons;
//    }
}
