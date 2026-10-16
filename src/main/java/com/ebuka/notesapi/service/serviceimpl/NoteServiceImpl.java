
package com.ebuka.notesapi.service.serviceimpl;

import com.ebuka.notesapi.model.Note;
import com.ebuka.notesapi.model.User;
import com.ebuka.notesapi.repository.NoteRepository;
import com.ebuka.notesapi.repository.UserRepository;
import com.ebuka.notesapi.service.NoteService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    // Dependencies
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    // Constructor
    public NoteServiceImpl(
            NoteRepository noteRepository,
            UserRepository userRepository) {

        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    // Create a note
    @Override
    public Note createNote(String title, String content, String email) {

        User user = getUserByEmail(email);

        if (!user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Please verify your account first");
        }

        Note note = new Note(title, content);
        note.setUser(user);

        return noteRepository.save(note);
    }

    // Retrieve all notes belonging to a user
    @Override
    @Transactional(readOnly = true)
    public List<Note> getMyNotes(String email) {

        User user = getUserByEmail(email);

        return noteRepository.findByUserId(user.getId());
    }

    // Retrieve one note belonging to a user
    @Override
    @Transactional(readOnly = true)
    public Note getNoteById(Long noteId, String email) {

        return getMyNote(noteId, email);
    }

    // Update a note
    @Override
    public Note updateNote(
            Long noteId,
            String title,
            String content,
            String email) {

        Note note = getMyNote(noteId, email);

        note.setTitle(title);
        note.setContent(content);

        return noteRepository.save(note);
    }

    // Delete a note
    @Override
    public void deleteNote(Long noteId, String email) {

        Note note = getMyNote(noteId, email);

        noteRepository.delete(note);
    }

    // Find a user by email
    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // Find a note and confirm that it belongs to the user
    private Note getMyNote(Long noteId, String email) {

        User user = getUserByEmail(email);

        return noteRepository.findById(noteId)
                .filter(note ->
                        note.getUser().getId().equals(user.getId()))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }
}
