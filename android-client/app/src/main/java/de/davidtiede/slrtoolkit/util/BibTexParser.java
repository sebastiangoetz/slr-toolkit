package de.davidtiede.slrtoolkit.util;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BibTexParser {
    private BibTeXParser parser;
    private static BibTexParser bibTexParser;
    private BibTeXDatabase bibTeXDatabase;
    File file;

    private BibTexParser() {
        try {
            parser = new BibTeXParser();
        } catch (Exception e) {
            System.out.println("An error occured trying to set the BibTeXParser!");
        }
    }

    public void setBibTeXDatabase(File file) throws FileNotFoundException, ParseException {
        Reader reader = new FileReader(file.getAbsolutePath());
        this.bibTeXDatabase = parser.parse(reader);
        this.file = file;
    }

    public Map<Key, BibTeXEntry> getBibTeXEntries() {
        Map<Key, BibTeXEntry> entryMap= bibTeXDatabase.getEntries();
        return entryMap;
    }

    public List<BibTeXObject> getObjects() {
        List<BibTeXObject> entryList= bibTeXDatabase.getObjects();
        return entryList;
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
            System.out.println("Deleted item!");
        } catch (IOException e) {
            System.out.println("Didn't work!");
            e.printStackTrace();
        }

    }

    public static BibTexParser getBibTexParser()
    {
        if (bibTexParser == null)
            bibTexParser = new BibTexParser();

        return bibTexParser;
    }
}