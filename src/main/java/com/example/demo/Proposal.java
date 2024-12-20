package com.example.demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class Proposal {
    private Long id;
    private String sender;
    private String content;
    private AtomicInteger voteCount;
    private static final Logger logger = LoggerFactory.getLogger(Proposal.class);


    // Constructors
    public Proposal() {}

    public Proposal(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.voteCount = new AtomicInteger(0);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    // Get the current vote count
    public int getVoteCount() {
        return voteCount.get();
    }

    // Increment the vote count in a thread-safe way
    public void incrementVoteCount() {
        voteCount.incrementAndGet();
    }
}
