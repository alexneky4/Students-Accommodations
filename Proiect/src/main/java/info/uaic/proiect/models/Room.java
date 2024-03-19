package info.uaic.proiect.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "capacity")
    private int capacity;

    @ManyToOne
    @JoinColumn(name="dormitory_id")
    private Dormitory dormitory;

    public Room(long id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public Room(int capacity) {
        this.capacity = capacity;
    }

    public Room() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Dormitory getDormitory() {
        return dormitory;
    }

    public void setDormitory(Dormitory dormitory) {
        this.dormitory = dormitory;
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }
}
