package de.davidtiede.slrtoolkit.util;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.davidtiede.slrtoolkit.database.Entry;

public class BibTexParser {
    private BibTeXParser parser;
    private static BibTexParser bibTexParser;
    private BibTeXDatabase bibTeXDatabase;
    File file;

    private BibTexParser() {
        try {
            parser = new BibTeXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBibTeXDatabase(File file) throws FileNotFoundException, ParseException {
        Reader reader = new FileReader(file.getAbsolutePath());
        this.bibTeXDatabase = parser.parse(reader);
        this.file = file;
    }

    public Map<Key, BibTeXEntry> getBibTeXEntries() {
        return bibTeXDatabase.getEntries();
    }

    public List<BibTeXObject> getObjects() {
        return bibTeXDatabase.getObjects();
    }

    public BibTeXObject getBibTexObject(Key key) {
        return bibTeXDatabase.resolveEntry(key);
    }

    public void removeObject(BibTeXObject object) {
        BibTeXFormatter formatter = new BibTeXFormatter();
        bibTeXDatabase.removeObject(object);
        try {
            Writer writer = new FileWriter(this.file.getAbsolutePath());
            formatter.format(bibTeXDatabase, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addObjectToFile(BibTeXObject object)  {
        BibTeXFormatter formatter = new BibTeXFormatter();
        bibTeXDatabase.addObject(object);
        try {
            Writer writer = new FileWriter(this.file.getAbsolutePath());
            formatter.format(bibTeXDatabase, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String safeGetField(BibTeXEntry entry, Key key) {
        if (entry.getField(key) != null) {
            return entry.getField(key).toUserString();
        }
        return "";
    }

    public Map<Entry, String> parseBibTexFile(File file) throws FileNotFoundException, ParseException {
        HashMap<Entry, String> entryTaxMap = new HashMap<>();
        setBibTeXDatabase(file);
        Map<Key, BibTeXEntry> entryMap = getBibTeXEntries();

        for(Key key: entryMap.keySet()) {
            BibTeXEntry bibTeXEntry = entryMap.get(key);
            assert bibTeXEntry != null;
            Entry entry = new Entry(key.toString(), safeGetField(bibTeXEntry, BibTeXEntry.KEY_TITLE));
            entry.setAuthor(safeGetField(bibTeXEntry, BibTeXEntry.KEY_AUTHOR));
            entry.setYear(safeGetField(bibTeXEntry, BibTeXEntry.KEY_YEAR));
            entry.setMonth(safeGetField(bibTeXEntry, BibTeXEntry.KEY_MONTH));
            entry.setJournal(safeGetField(bibTeXEntry, BibTeXEntry.KEY_JOURNAL));
            entry.setVolume(safeGetField(bibTeXEntry, BibTeXEntry.KEY_VOLUME));
            entry.setUrl(safeGetField(bibTeXEntry, BibTeXEntry.KEY_URL));
            entry.setKeywords(safeGetField(bibTeXEntry, new Key("keywords")));
            entry.setDoi(safeGetField(bibTeXEntry, new Key("doi")));
            entry.setAbstractText(safeGetField(bibTeXEntry, new Key("abstract")));
            entry.setType(safeGetField(bibTeXEntry, BibTeXEntry.KEY_TYPE));

            String classes = safeGetField(bibTeXEntry, new Key("classes"));

            entryTaxMap.put(entry, classes);
        }

        return entryTaxMap;
    }

    //make BibTexParser Singleton
    public static BibTexParser getBibTexParser()
    {
        if (bibTexParser == null)
            bibTexParser = new BibTexParser();

        return bibTexParser;
    }
}