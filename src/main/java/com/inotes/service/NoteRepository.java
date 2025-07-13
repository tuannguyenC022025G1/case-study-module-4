package com.inotes.service;

import com.inotes.model.Note;
import com.inotes.model.NoteType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    private final DataSource dataSource;

    public NoteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Note note) {
        try (Connection conn = dataSource.getConnection()) {
            if (note.getId() == 0) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO notes (title, content, type_id) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, note.getTitle());
                stmt.setString(2, note.getContent());
                stmt.setInt(3, note.getTypeId());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    note.setId(rs.getInt(1));
                }
            } else {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE notes SET title = ?, content = ?, type_id = ? WHERE id = ?"
                );
                stmt.setString(1, note.getTitle());
                stmt.setString(2, note.getContent());
                stmt.setInt(3, note.getTypeId());
                stmt.setInt(4, note.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM notes WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Note> searchNotes(String keyword) {
        List<Note> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT n.id, n.title, n.content, n.type_id, t.name FROM notes n JOIN note_types t ON n.type_id = t.id WHERE n.title LIKE ? OR n.content LIKE ?"
            );
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NoteDB note = new NoteDB(this);
                note.setId(rs.getInt("id"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setTypeId(rs.getInt("type_id"));
                results.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<NoteType> getAllNoteTypes() {
        List<NoteType> noteTypes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM note_types");
            while (rs.next()) {
                NoteType type = new NoteType();
                type.setId(rs.getInt("id"));
                type.setName(rs.getString("name"));
                noteTypes.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteTypes;
    }
}