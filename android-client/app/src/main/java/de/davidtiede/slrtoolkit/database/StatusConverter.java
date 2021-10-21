package de.davidtiede.slrtoolkit.database;

import androidx.room.TypeConverter;

public class StatusConverter {

    @TypeConverter
    public static Entry.Status toStatus(int status) {
        if (status == Entry.Status.OPEN.getCode()) {
            return Entry.Status.OPEN;
        } else if (status == Entry.Status.KEEP.getCode()) {
            return Entry.Status.KEEP;
        } else if (status == Entry.Status.DISCARD.getCode()) {
            return Entry.Status.DISCARD;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static Integer toInteger(Entry.Status status) {
        return status.getCode();
    }
}
