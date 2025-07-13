package com.inotes.service;

import com.inotes.model.Note;
import com.inotes.model.NoteType;

public class NoteDB implements Note {
    private int id;
    private String title;
    private String content;
    private int typeId;
    private final NoteRepository noteRepository;

    public NoteDB(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getType() {
        try {
            for (NoteType type : noteRepository.getAllNoteTypes()) {
                if (type.getId() == typeId) {
                    return type.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void save() {
        noteRepository.save(this);
    }

    @Override
    public void delete() {
        noteRepository.delete(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}