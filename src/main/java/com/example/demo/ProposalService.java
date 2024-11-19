package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProposalService {
    private static final AtomicLong idCounter = new AtomicLong();

    private final List<Proposal> proposals = new CopyOnWriteArrayList<>();

    // Fetch all proposals
    public List<Proposal> proposals() {
        return Collections.unmodifiableList(proposals);
    }

    // Save a new proposal
    public Proposal addProposal(Proposal proposal) {
        if (proposal.getId() == null) {
            proposal.setId(idCounter.incrementAndGet());
        }

        proposals.add(proposal);

        return proposal;
    }

    public void addVote(Vote vote) {
        proposals.stream()
                .filter(p -> p.getId().equals(vote.getProposalId()))
                .findFirst().ifPresent(Proposal::incrementVoteCount);
    }

    public Proposal getProposalById(Long proposalId) {
        return proposals.stream()
                .filter(proposal -> proposal.getId().equals(proposalId))
                .findFirst()
                .orElse(null);
    }
}

