package de.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity
public class Repo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String local_path;
    private String remote_url;
    private String username;
    private String token;
    private String git_name;
    private String git_email;
    private String textAbstract;
    private String taxonomyDescription;


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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
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

    public String getGit_email() {
        return git_email;
    }

    public String getTextAbstract() {
        return textAbstract;
    }

    public void setTextAbstract(String textAbstract) {
        this.textAbstract = textAbstract;
    }

    public String getTaxonomyDescription() {
        return taxonomyDescription;
    }

    public void setTaxonomyDescription(String taxonomyDescription) {
        this.taxonomyDescription = taxonomyDescription;
    }

    public void setRemote_url(String remote_url) {
        this.remote_url = remote_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGit_name(String git_name) {
        this.git_name = git_name;
    }

    public void setGit_email(String git_email) {
        this.git_email = git_email;
    }
}
