package me.xra1ny.essentia.database.test.entity;

import jakarta.persistence.*;
import lombok.ToString;

@ToString
@Entity(name = "test")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String username;
}
