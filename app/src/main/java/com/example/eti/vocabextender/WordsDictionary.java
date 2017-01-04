package com.example.eti.vocabextender;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.eti.vocabextender.db.DataBaseHelper;
import com.example.eti.vocabextender.db.WordDBDAO;
import com.example.eti.vocabextender.model.Category;
import com.example.eti.vocabextender.model.Word;

import java.util.ArrayList;

public class WordsDictionary extends WordDBDAO {

    public WordsDictionary(Context context) {
        super(context);
    }

    private long update(Word word) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.WORD_COLUMN, word.getWord());
        values.put(DataBaseHelper.MEMORIZED_VALUE, word.getMemorizedValue());
        values.put(DataBaseHelper.DEFINITION, word.getDefinition());
        values.put(DataBaseHelper.CATEGORY, word.getCategory().toString());

        return (long) database.update(DataBaseHelper.WORDS_TABLE, values,
                DataBaseHelper.ID_COLUMN + " =?",
                new String[]{String.valueOf(word.getId())});
    }

    private ArrayList<Word> getWords(String selection, String[] selectionArgs) {
        ArrayList<Word> words = new ArrayList<>();

        Cursor cursor = database.query(DataBaseHelper.WORDS_TABLE,
                new String[]{DataBaseHelper.ID_COLUMN,
                        DataBaseHelper.WORD_COLUMN,
                        DataBaseHelper.MEMORIZED_VALUE,
                        DataBaseHelper.DEFINITION,
                        DataBaseHelper.CATEGORY},
                selection,
                selectionArgs,
                null, null, null);

        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(0));
            word.setWord(cursor.getString(1));
            word.setMemorizedValue(cursor.getDouble(2));
            word.setDefinition(cursor.getString(3));
            word.setCategory(Category.valueOf(cursor.getString(4)));

            words.add(word);
        }

        return words;
    }

    private Word GetWordById(int id) {
        ArrayList<Word> words = getWords(DataBaseHelper.ID_COLUMN + "=?", new String[]{String.valueOf(id)});

        return words.isEmpty() ? null : words.get(0);
    }

    public ArrayList<Word> GetWordsFromCategory(Category category, int wordId, int count) {
        String where = DataBaseHelper.CATEGORY + "=? AND " + DataBaseHelper.ID_COLUMN + "!=?";
        ArrayList<Word> allWords = getWords(where, new String[]{category.toString(), String.valueOf(wordId)});
        ArrayList<Word> filteredWords = new ArrayList<>();

        if (allWords.size() <= count) return allWords;

        while (count > 0) {
            int index = (int) (Math.random() * allWords.size());
            filteredWords.add(allWords.get(index));
            allWords.remove(index);
            count--;
        }

        return filteredWords;
    }

    public ArrayList<Word> GetWordsToLearn(int count) {
        ArrayList<Word> allWords = getWords(DataBaseHelper.MEMORIZED_VALUE + ">=1.0", null);
        ArrayList<Word> filteredWords = new ArrayList<>();

        if (allWords.size() <= count) return allWords;

        while (count > 0) {
            int index = (int) (Math.random() * allWords.size());
            filteredWords.add(allWords.get(index));
            allWords.remove(index);
            count--;
        }

        return filteredWords;
    }

    public ArrayList<Word> GetWordsToRepeat() {
        return getWords(DataBaseHelper.MEMORIZED_VALUE + "<1.0", null);
    }

    public void MarkCorrectAnswer(int id) {
        Word word = GetWordById(id);
        if (word == null) return;

        word.setMemorizedValue(word.getMemorizedValue() * 0.96);
        update(word);
    }

    public void MarkIncorrectAnswer(int id) {
        Word word = GetWordById(id);
        if (word == null) return;

        double newMemorizedValue = word.getMemorizedValue() * 1.14;
        word.setMemorizedValue(newMemorizedValue < 1.0 ? newMemorizedValue : 0.99999999);
        update(word);
    }

    public void MarkAsLearned(int id) {
        Word word = GetWordById(id);
        if (word == null) return;

        word.setMemorizedValue(0.99999999);
        update(word);
    }
}
