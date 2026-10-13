
package com.ebuka.notesapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ebuka.notesapi.model.Note;
import com.ebuka.notesapi.service.NoteService;

import jakarta.validation.Valid;

@RestController
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/api/notes")
    public List<Note> getNotes() {
        return noteService.getAllNotes();
    }
    @PostMapping("/api/notes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteService.createNote(note);
    }
    @GetMapping("/api/{id}/notes")
    public Note getNoteById(@PathVariable Long id){
        return noteService.getNoteById(id);
    }
    @PutMapping("/api/{id}/notes")
    public Note updateNote(@PathVariable Long id, @Valid @RequestBody Note note){
        return  noteService.updateNote(id, note);
    }
    @DeleteMapping("/api/{id}/notes")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
    

}

