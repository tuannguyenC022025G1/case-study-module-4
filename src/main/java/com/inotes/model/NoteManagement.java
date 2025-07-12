package com.inotes.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NoteManagement {
    private Note note;
    private String storeType;

    public NoteManagement() {
        note = new NoteDB();
        storeType = "DB";
    }

    public void changeNoteStore(String storeType) {
        this.storeType = storeType;
        this.note = storeType.equals("DB") ? new NoteDB() : new NoteFile();
    }

    public List<Note> searchNotes(String keyword) {
        List<Note> results = new ArrayList<>();
        if (storeType.equals("DB")) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/case_study_module_4", "root", "password")) {
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT n.id, n.title, n.content, n.type_id, t.name FROM notes n JOIN note_types t ON n.type_id = t.id WHERE n.title LIKE ? OR n.content LIKE ?"
                );
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    NoteDB note = new NoteDB();
                    note.setId(rs.getInt("id"));
                    note.setTitle(rs.getString("title"));
                    note.setContent(rs.getString("content"));
                    note.setTypeId(rs.getInt("type_id"));
                    results.add(note);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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