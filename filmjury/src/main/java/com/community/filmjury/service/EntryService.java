package com.community.filmjury.service;

import com.community.filmjury.entity.Entry;
import com.community.filmjury.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    // Create a new entry
    public Entry createEntry(Entry entry) {
        return entryRepository.save(entry);
    }

    // Get all entries
    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    // Get entry by ID
    public Entry getEntryById(Integer id) {
        Optional<Entry> entry = entryRepository.findById(id);

        if (entry.isPresent()) {
            return entry.get();
        }

        throw new RuntimeException("Entry not found with ID: " + id);
    }

    // Update an existing entry
    public Entry updateEntry(Integer id, Entry updatedEntry) {

        Entry existingEntry = getEntryById(id);

        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setGenre(updatedEntry.getGenre());
        existingEntry.setVideoLink(updatedEntry.getVideoLink());

        return entryRepository.save(existingEntry);
    }

    // Delete an entry
    public void deleteEntry(Integer id) {

        if (!entryRepository.existsById(id)) {
            throw new RuntimeException("Entry not found with ID: " + id);
        }

        entryRepository.deleteById(id);
    }
}