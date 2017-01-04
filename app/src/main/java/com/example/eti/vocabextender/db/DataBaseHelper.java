package com.example.eti.vocabextender.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vocabulary_extender";
    private static final int DATABASE_VERSION = 1;

    public static final String WORDS_TABLE = "words";

    public static final String ID_COLUMN = "id";
    public static final String WORD_COLUMN = "word";
    public static final String MEMORIZED_VALUE = "memorized_value";
    public static final String DEFINITION = "definition";
    public static final String CATEGORY = "category";

    private static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE "
            + WORDS_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + WORD_COLUMN + " TEXT, " + MEMORIZED_VALUE + " DOUBLE, "
            + DEFINITION + " TEXT, " + CATEGORY + " TEXT" + ")";

    private static DataBaseHelper instance;

    private static final Map<String, String> VERBS = new HashMap<>();
    private static final Map<String, String> NOUNS = new HashMap<>();
    private static final Map<String, String> ADJECTIVES = new HashMap<>();
    static {
        VERBS.put("abash", "to make ashamed");
        VERBS.put("abate", "to put an end to");
        VERBS.put("abbreviate", "to make shorter");
        VERBS.put("abdicate", "to surrender, renounce or relinquish");
        VERBS.put("abridge", "to shorten in duration");
        VERBS.put("abrogate", "to abolish by authoritative action");
        VERBS.put("abscess", "to form such a collection of pus");
        VERBS.put("acclaim", "applaud");
        VERBS.put("accuse", "to find fault with, to blame");
        VERBS.put("acquire", "to get");
        NOUNS.put("acme", "the top or highest point, pinnacle, culmination");
        NOUNS.put("acknowledgment", "the act of acknowledging, admission");
        NOUNS.put("acreage", "size, as measured in acres");
        NOUNS.put("acrophobia", "fear of heights");
        NOUNS.put("actuary", "registrar, clerk");
        NOUNS.put("adjuration", "a grave warning");
        NOUNS.put("adoration", "an act of religious worship");
        NOUNS.put("aficionado", "a fan or devotee");
        NOUNS.put("ailment", "a disease, sickness");
        NOUNS.put("aliment", "that which nourishes, food");
        ADJECTIVES.put("albeit", "although, even if");
        ADJECTIVES.put("akin", "related by blood, similar");
        ADJECTIVES.put("agnostic", "the doubt of a higher power/being");
        ADJECTIVES.put("agog", "in eager desire, eager, astir");
        ADJECTIVES.put("agile", "active, quick and coordinated");
        ADJECTIVES.put("afresh", "anew");
        ADJECTIVES.put("aforesaid", "a previously stated statement");
        ADJECTIVES.put("affable", "pleasant, easy to approach");
        ADJECTIVES.put("aesthetic", "having a sense of beauty");
        ADJECTIVES.put("advisory", "able to give advice");
    }

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEE_TABLE);
        db.beginTransaction();
        addVerbs(db);
        addNouns(db);
        addAdjectives(db);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void addVerbs(SQLiteDatabase db) {
        for (Map.Entry<String, String> word : VERBS.entrySet()) {
            db.execSQL("INSERT INTO words(word, memorized_value, definition, category) " +
                    "VALUES ('" + word.getKey() + "', 1.0, '" + word.getValue() + "', 'VERB')");
        }
    }

    private void addNouns(SQLiteDatabase db) {
        for (Map.Entry<String, String> word : NOUNS.entrySet()) {
            db.execSQL("INSERT INTO words(word, memorized_value, definition, category) " +
                    "VALUES ('" + word.getKey() + "', 1.0, '" + word.getValue() + "', 'NOUN')");
        }
    }

    private void addAdjectives(SQLiteDatabase db) {
        for (Map.Entry<String, String> word : ADJECTIVES.entrySet()) {
            db.execSQL("INSERT INTO words(word, memorized_value, definition, category) " +
                    "VALUES ('" + word.getKey() + "', 1.0, '" + word.getValue() + "', 'ADJECTIVE')");
        }
    }
}
