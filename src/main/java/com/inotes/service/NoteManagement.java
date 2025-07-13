package com.inotes.service;

import com.inotes.model.Note;
import com.inotes.model.NoteType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class NoteManagement {
    private Note note;
    private String storeType;
    private final NoteRepository noteRepository;

    public NoteManagement(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        note = new NoteDB(noteRepository);
        storeType = "DB";
    }

    public void changeNoteStore(String storeType) {
        this.storeType = storeType;
        this.note = storeType.equals("DB") ? new NoteDB(noteRepository) : new NoteFile();
    }

    public List<Note> searchNotes(String keyword) {
        List<Note> results = new ArrayList<>();
        if (storeType.equals("DB")) {
            results = noteRepository.searchNotes(keyword);
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader("notes.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(keyword)) {
                        String[] parts = line.split("\\|");
                        NoteFile note = new NoteFile();
                        note.setId(Integer.parseInt(parts[0]));
                        note.setTitle(parts[1]);
                        note.setContent(parts[2]);
                        note.setTypeId(Integer.parseInt(parts[3]));
                        results.add(note);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public void addNote(String title, String content, int typeId) {
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(typeId);
        note.save();
    }

    public void deleteNote(int id) {
        if (note instanceof NoteDB) {
            ((NoteDB) note).setId(id);
        } else {
            ((NoteFile) note).setId(id);
        }
        note.delete();
    }
}