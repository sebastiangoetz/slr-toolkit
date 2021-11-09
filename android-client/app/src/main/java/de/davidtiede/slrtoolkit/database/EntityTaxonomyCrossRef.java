package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;

@Entity(primaryKeys = {"taxonomyId", "id"})
public class EntityTaxonomyCrossRef {
    public int taxonomyId;
    public int id;
}
