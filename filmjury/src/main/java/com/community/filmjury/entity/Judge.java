package com.community.filmjury.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "judges")
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    @ManyToMany
    @JoinTable(
            name = "judge_entries",
            joinColumns = @JoinColumn(name = "judge_id"),
            inverseJoinColumns = @JoinColumn(name = "entry_id")
    )
    private Set<Entry> assignedEntries = new HashSet<>();

    public Judge() {
    }

    public Judge(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Entry> getAssignedEntries() {
        return assignedEntries;
    }

    public void setAssignedEntries(Set<Entry> assignedEntries) {
        this.assignedEntries = assignedEntries;
    }
}