package com.ebuka.notesapi.repository;
import com.ebuka.notesapi.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long>{

}
