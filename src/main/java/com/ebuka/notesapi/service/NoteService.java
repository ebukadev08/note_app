package com.ebuka.notesapi.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ebuka.notesapi.model.Note;

@Service
public interface NoteService {

    Note createNote(String title, String content, String email);

    List<Note> getMyNotes(String email);

    Note getNoteById(Long id, String email);

    Note updateNote(Long noteId, String email, String title, String content);

    void deleteNote(Long NoteId, String email);

}