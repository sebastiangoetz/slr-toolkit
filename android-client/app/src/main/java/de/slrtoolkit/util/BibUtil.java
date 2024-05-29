package de.slrtoolkit.util;

import android.util.Log;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.KeyValue;
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

public class BibUtil {
    private static BibUtil bibUtil;
    File file;
    private BibTeXParser parser;
    private BibTeXDatabase bibTeXDatabase;

    private BibUtil() {
        try {
            parser = new BibTeXParser();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "could not instantiate parser", e);
        }
    }

    //make BibTexParser Singleton
    public static BibUtil getInstance() {
        if (bibUtil == null)
            bibUtil = new BibUtil();

        return bibUtil;
    }

    /**Removes the respective class from the classes string.
     *
     * @param classesString the full classification string
     * @param name the class to remove from it
     */
    public static String removeClassFromClassification(String classesString, String name) {
        //TODO
        return classesString;
    }

    public static String addClassToClassification(String classesString, String name) {
        //TODO
        classesString = "{" + name + "}";
        return classesString;
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
            Log.e(this.getClass().getName(), "could not remove object from file", e);
        }

    }

    public void addObjectToFile(BibTeXObject object) {
        BibTeXFormatter formatter = new BibTeXFormatter();
        bibTeXDatabase.addObject(object);
        try {
            Writer writer = new FileWriter(this.file.getAbsolutePath());
            formatter.format(bibTeXDatabase, writer);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "could not add object to file", e);
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
            bibEntry.setClasses(classes);

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
        bibEntry.setClasses(safeGetField(bibTeXEntry, new Key("classes")));
        return bibEntry;
    }

    /**Updates a bibEntry in the .bib file. Currently, only a changed classification is supported.
     * That is, only the classes-attribute can change.
     *
      * @param bibEntry the entry in it's new version
     */
    public void update(BibEntry bibEntry) {
        BibTeXEntry orig = bibTeXDatabase.resolveEntry(new Key(bibEntry.getKey()));
        if(orig != null) { //update
            bibTeXDatabase.removeObject(orig);
            orig.removeField(new Key("classes"));
            orig.addField(new Key("classes"), new KeyValue(bibEntry.getClasses()));
            bibTeXDatabase.addObject(orig);
            BibTeXFormatter f = new BibTeXFormatter();
            try {
                f.format(bibTeXDatabase,new FileWriter(file));
            } catch (IOException e) {
                Log.e(this.getClass().getName(), "could not write bib file", e);
            }
        } else {
            Log.e(this.getClass().getName(), "could not update bib entry in file");
        }
    }
}