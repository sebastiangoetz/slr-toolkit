package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Repo {
    @PrimaryKey
    public int id;
    public String name;
    public String local_path;
    public String remote_url;
    public String username;
    public String git_name;
    public String git_email;
}
