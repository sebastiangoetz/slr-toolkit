package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.nio.file.Path;

@Entity
public class Repo {
    @PrimaryKey
    public int id;

    public String name;
    public Path path;
}
