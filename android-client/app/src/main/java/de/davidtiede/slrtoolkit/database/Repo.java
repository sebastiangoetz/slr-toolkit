package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Repo {
    @PrimaryKey(autoGenerate = true) private int id;
    private String name;
    private String local_path;
    private String remote_url;
    private String username;
    private String token;
    private String git_name;
    private String git_email;

    public Repo(String remote_url, String username, String token, String git_name, String git_email) {
        this.remote_url = remote_url;
        this.username = username;
        this.token = token;
        this.git_name = git_name;
        this.git_email = git_email;
        this.name = "Unnamed";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocal_path() {
        return local_path;
    }

    public String getRemote_url() {
        return remote_url;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getGit_name() {
        return git_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public String getGit_email() {
        return git_email;
    }

}
