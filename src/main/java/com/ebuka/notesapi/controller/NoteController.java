
package com.ebuka.notesapi.controller;

import com.ebuka.notesapi.dto.res.ApiRes;
import com.ebuka.notesapi.dto.res.NoteResponse;
import com.ebuka.notesapi.model.Note;
import com.ebuka.notesapi.service.NoteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
    @GetMapping
    public ResponseEntity<ApiRes<List<NoteResponse>>> getNotes(
            Authentication authentication) {

            List<NoteResponse> notes = noteService
                    .getMyNotes(authentication.getName())
                    .stream()
                    .map(NoteResponse::from)
                    .toList();

            return ResponseEntity.ok(
                ApiRes.success("Notes retrieved successfully", notes));

        }

    @PostMapping
    public ResponseEntity<ApiRes<NoteResponse>> createNote(
            @Valid @RequestBody Note note,
            Authentication authentication) {

        Note createdNote = noteService.createNote(
                note.getTitle(),
                note.getContent(),
                authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success(
                        "Note created successfully",
                        NoteResponse.from(createdNote)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiRes<NoteResponse>> getNoteById(
        @PathVariable Long id,
        Authentication authentication) {

        Note note = noteService.getNoteById(
            id, authentication.getName());

        return ResponseEntity.ok(
            ApiRes.success(
                "Note retrieved successfully",
                NoteResponse.from(note)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiRes<NoteResponse>> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody Note note,
            Authentication authentication) {

        Note updatedNote = noteService.updateNote(
                id,
                note.getTitle(),
                note.getContent(),
                authentication.getName());

        return ResponseEntity.ok(
                ApiRes.success(
                        "Note updated successfully",
                        NoteResponse.from(updatedNote)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiRes<Void>> deleteNote(
            @PathVariable Long id,
            Authentication authentication) {

        noteService.deleteNote(id, authentication.getName());

        return ResponseEntity.ok(
                ApiRes.success("Note deleted successfully", null));
    }
}
