package com.inotes.controller;

import com.inotes.model.Note;
import com.inotes.model.NoteType;
import com.inotes.service.NoteDB;
import com.inotes.service.NoteFile;
import com.inotes.service.NoteManagement;
import com.inotes.service.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteManagement noteManagement;
    private final NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteManagement noteManagement, NoteRepository noteRepository) {
        this.noteManagement = noteManagement;
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public String listNotes(Model model) {
        List<Note> notes = noteManagement.searchNotes("");
        model.addAttribute("notes", notes);
        return "listNotes";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("noteTypes", noteRepository.getAllNoteTypes());
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