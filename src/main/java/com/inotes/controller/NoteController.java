package com.inotes.controller;

import com.inotes.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private NoteManagement noteManagement = new NoteManagement();

    @GetMapping
    public String listNotes(Model model) {
        List<Note> notes = noteManagement.searchNotes("");
        model.addAttribute("notes", notes);
        return "listNotes";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<NoteType> noteTypes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/case_study_module_4", "root", "password")) {
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
        model.addAttribute("noteTypes", noteTypes);
        return "addNote";
    }

    @PostMapping("/add")
    public String addNote(@RequestParam String title, @RequestParam String content, @RequestParam int typeId, @RequestParam String storeType) {
        noteManagement.changeNoteStore(storeType);
        noteManagement.addNote(title, content, typeId);
        return "redirect:/notes";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam int id, Model model) {
        List<Note> notes = noteManagement.searchNotes("");
        Note note = notes.stream().filter(n -> {
            if (n instanceof NoteDB) return ((NoteDB) n).getId() == id;
            else return ((NoteFile) n).getId() == id;
        }).findFirst().orElse(null);
        model.addAttribute("note", note);
        return "noteDetail";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam int id) {
        noteManagement.deleteNote(id);
        return "redirect:/notes";
    }

    @GetMapping("/search")
    public String searchNotes(@RequestParam String keyword, Model model) {
        List<Note> notes = noteManagement.searchNotes(keyword);
        model.addAttribute("notes", notes);
        return "searchNotes";
    }
}