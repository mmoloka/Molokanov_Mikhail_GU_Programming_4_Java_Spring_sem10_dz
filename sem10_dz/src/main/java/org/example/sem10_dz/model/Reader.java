package org.example.sem10_dz.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "readers")
@Data
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Reader(String name) {

        this.name = name;
    }

    public Reader() {
    }
}
