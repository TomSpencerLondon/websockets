package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProposalService {
    private static final AtomicLong idCounter = new AtomicLong();

    private final List<Proposal> proposals = new ArrayList<>();

    // Fetch all proposals
    public List<Proposal> getAllProposals() {
        return new ArrayList<>(proposals); // Return a copy to prevent direct modification
    }

    // Save a new proposal
    public Proposal addProposal(Proposal proposal) {
        long id = idCounter.incrementAndGet();

        if (proposal.getId() == null) {
            proposal.setId(id); // Set the generated ID
        }

        proposals.add(proposal);

        return proposal;
    }

    // Add a vote to a proposal
    public void addVote(Vote vote) {
        // Find the proposal by its ID
        Optional<Proposal> optionalProposal = proposals.stream()
                .filter(p -> p.getId().equals(vote.getProposalId()))
                .findFirst();

        // Increment vote count if proposal exists
        optionalProposal.ifPresent(Proposal::incrementVoteCount);
    }

    // Fetch a specific proposal by ID
    public Proposal getProposalById(Long proposalId) {
        return proposals.stream()
                .filter(proposal -> proposal.getId().equals(proposalId))
                .findFirst()
                .orElse(null);
    }

    // Optionally, to clear the list (useful for testing or resetting)
    public void clearProposals() {
        proposals.clear();
    }
}

