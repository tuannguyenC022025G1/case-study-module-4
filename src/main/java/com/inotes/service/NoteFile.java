package com.inotes.service;

import com.inotes.model.Note;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteFile implements Note {
    private int id;
    private String title;
    private String content;
    private int typeId;
    private static final String FILE_PATH = "notes.txt";

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
        return switch (typeId) {
            case 1 -> "Personal";
            case 2 -> "Work";
            case 3 -> "Study";
            default -> "Unknown";
        };
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void save() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (id == 0) {
            id = lines.size() + 1;
            lines.add(id + "|" + title + "|" + content + "|" + typeId);
        } else {
            lines.set(id - 1, id + "|" + title + "|" + content + "|" + typeId);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(id + "|")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
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