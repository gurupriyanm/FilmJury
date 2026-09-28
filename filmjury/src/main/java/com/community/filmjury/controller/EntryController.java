package com.community.filmjury.controller;

import com.community.filmjury.entity.Entry;
import com.community.filmjury.service.EntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Entry> createEntry(@RequestBody Entry entry) {
        Entry savedEntry = entryService.createEntry(entry);
        return ResponseEntity.ok(savedEntry);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Entry>> getAllEntries() {
        return ResponseEntity.ok(entryService.getAllEntries());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Entry> getEntryById(@PathVariable Integer id) {
        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Entry> updateEntry(
            @PathVariable Integer id,
            @RequestBody Entry entry) {

        return ResponseEntity.ok(
                entryService.updateEntry(id, entry)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable Integer id) {

        entryService.deleteEntry(id);

        return ResponseEntity.ok("Entry deleted successfully");
    }
}