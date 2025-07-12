package com.inotes.model;

import java.sql.*;

public class NoteDB implements Note {
    private int id;
    private String title;
    private String content;
    private int typeId;
    private Connection conn;

    public NoteDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/case_study_module_4", "root", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM note_types WHERE id = ?");
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    @Override
    public void save() {
        try {
            if (id == 0) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO notes (title, content, type_id) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                stmt.setString(1, title);
                stmt.setString(2, content);
                stmt.setInt(3, typeId);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } else {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE notes SET title = ?, content = ?, type_id = ? WHERE id = ?"
                );
                stmt.setString(1, title);
                stmt.setString(2, content);
                stmt.setInt(3, typeId);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM notes WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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