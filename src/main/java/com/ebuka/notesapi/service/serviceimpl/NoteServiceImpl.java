package com.ebuka.notesapi.service.serviceimpl;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ebuka.notesapi.model.Note;
import com.ebuka.notesapi.repository.NoteRepository;
import com.ebuka.notesapi.service.NoteService;


@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    @Override
    public Note updateNote(Long id, Note updatedNote) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Note not found"
                    ));

        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());

        return noteRepository.save(existingNote);

    }

    @Override
    public void deleteNote(Long id) {
        Note existingNote = noteRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Note not found"
                ));
        noteRepository.delete(existingNote);

       

}
}
