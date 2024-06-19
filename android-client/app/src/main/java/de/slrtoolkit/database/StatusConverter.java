package de.slrtoolkit.database;

import androidx.room.TypeConverter;

public class StatusConverter {

    @TypeConverter
    public static BibEntry.Status toStatus(int status) {
        if (status == BibEntry.Status.OPEN.getCode()) {
            return BibEntry.Status.OPEN;
        } else if (status == BibEntry.Status.KEEP.getCode()) {
            return BibEntry.Status.KEEP;
        } else if (status == BibEntry.Status.DISCARD.getCode()) {
            return BibEntry.Status.DISCARD;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static Integer toInteger(BibEntry.Status status) {
        return status.getCode();
    }
}
