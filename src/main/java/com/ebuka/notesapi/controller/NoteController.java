
package com.ebuka.notesapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

    @GetMapping("/api/notes")
    public String getNotes() {
        return "Notes API is working!";
    }
}
