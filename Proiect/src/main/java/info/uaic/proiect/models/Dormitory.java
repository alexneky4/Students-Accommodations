package info.uaic.proiect.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Dormitory")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dormitory_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "left_places")
    private int leftPlaces;

    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL)
    private List<Room> rooms;

    public Dormitory() {
    }

    public Dormitory(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Dormitory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getLeftPlaces() {
        return leftPlaces;
    }

    public void setLeftPlaces(int leftPlaces) {
        this.leftPlaces = leftPlaces;
    }
}
