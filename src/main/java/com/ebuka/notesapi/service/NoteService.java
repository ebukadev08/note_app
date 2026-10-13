package com.ebuka.notesapi.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ebuka.notesapi.model.Note;

@Service
public interface NoteService {
    List<Note> getAllNotes();

    Note createNote(Note note);

    Note getNoteById(Long id);

    Note updateNote(Long id, Note updatedNote);
    void deleteNote(Long id);

}