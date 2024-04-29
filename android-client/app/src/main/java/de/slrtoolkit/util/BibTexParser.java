package de.slrtoolkit.util;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import de.slrtoolkit.database.BibEntry;

public class BibTexParser {
    private static BibTexParser bibTexParser;
    File file;
    private BibTeXParser parser;
    private BibTeXDatabase bibTeXDatabase;

    private BibTexParser() {
        try {
            parser = new BibTeXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //make BibTexParser Singleton
    public static BibTexParser getBibTexParser() {
        if (bibTexParser == null)
            bibTexParser = new BibTexParser();

        return bibTexParser;
    }

    public void setBibTeXDatabase(File file) throws FileNotFoundException, ParseException {
        Reader reader = new FileReader(file.getAbsolutePath());
        this.bibTeXDatabase = parser.parse(reader);
        this.file = file;
    }

    public Map<Key, BibTeXEntry> getBibTeXEntries() {
        return bibTeXDatabase.getEntries();
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

    public void addObjectToFile(BibTeXObject object) {
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

    public BibTeXDatabase parse(String s) throws ParseException {
        StringReader r = new StringReader(s);
        return parser.parse(r);
    }

    public Map<BibEntry, String> parseBibTexFile(File file) throws FileNotFoundException, ParseException {
        HashMap<BibEntry, String> entryTaxMap = new HashMap<>();
        setBibTeXDatabase(file);
        Map<Key, BibTeXEntry> entryMap = getBibTeXEntries();

        for (Key key : entryMap.keySet()) {
            BibTeXEntry bibTeXEntry = entryMap.get(key);
            assert bibTeXEntry != null;
            BibEntry bibEntry = new BibEntry(key.toString(), safeGetField(bibTeXEntry, BibTeXEntry.KEY_TITLE));
            bibEntry.setAuthor(safeGetField(bibTeXEntry, BibTeXEntry.KEY_AUTHOR));
            bibEntry.setYear(safeGetField(bibTeXEntry, BibTeXEntry.KEY_YEAR));
            bibEntry.setMonth(safeGetField(bibTeXEntry, BibTeXEntry.KEY_MONTH));
            bibEntry.setBooktitle(safeGetField(bibTeXEntry, BibTeXEntry.KEY_BOOKTITLE));
            bibEntry.setJournal(safeGetField(bibTeXEntry, BibTeXEntry.KEY_JOURNAL));
            bibEntry.setVolume(safeGetField(bibTeXEntry, BibTeXEntry.KEY_VOLUME));
            bibEntry.setUrl(safeGetField(bibTeXEntry, BibTeXEntry.KEY_URL));
            bibEntry.setKeywords(safeGetField(bibTeXEntry, new Key("keywords")));
            bibEntry.setDoi(safeGetField(bibTeXEntry, new Key("doi")));
            bibEntry.setAbstractText(safeGetField(bibTeXEntry, new Key("abstract")));
            bibEntry.setType(safeGetField(bibTeXEntry, BibTeXEntry.KEY_TYPE));

            String classes = safeGetField(bibTeXEntry, new Key("classes"));

            entryTaxMap.put(bibEntry, classes);
        }

        return entryTaxMap;
    }

    public BibEntry translate(@NotNull BibTeXEntry bibTeXEntry) {
        BibEntry bibEntry = new BibEntry(bibTeXEntry.getKey().toString(), safeGetField(bibTeXEntry, BibTeXEntry.KEY_TITLE));
        bibEntry.setAuthor(safeGetField(bibTeXEntry, BibTeXEntry.KEY_AUTHOR));
        bibEntry.setYear(safeGetField(bibTeXEntry, BibTeXEntry.KEY_YEAR));
        bibEntry.setMonth(safeGetField(bibTeXEntry, BibTeXEntry.KEY_MONTH));
        bibEntry.setBooktitle(safeGetField(bibTeXEntry, BibTeXEntry.KEY_BOOKTITLE));
        bibEntry.setJournal(safeGetField(bibTeXEntry, BibTeXEntry.KEY_JOURNAL));
        bibEntry.setVolume(safeGetField(bibTeXEntry, BibTeXEntry.KEY_VOLUME));
        bibEntry.setUrl(safeGetField(bibTeXEntry, BibTeXEntry.KEY_URL));
        bibEntry.setKeywords(safeGetField(bibTeXEntry, new Key("keywords")));
        bibEntry.setDoi(safeGetField(bibTeXEntry, new Key("doi")));
        bibEntry.setAbstractText(safeGetField(bibTeXEntry, new Key("abstract")));
        bibEntry.setType(safeGetField(bibTeXEntry, BibTeXEntry.KEY_TYPE));
        return bibEntry;
    }
}