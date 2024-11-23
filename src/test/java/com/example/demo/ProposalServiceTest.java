package com.example.demo;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProposalServiceTest {

    private ProposalService proposalService;

    @BeforeEach
    public void setUp() {
        proposalService = new ProposalService();
        proposalService.addProposal(new Proposal("User 1", "Proposal 1"));
    }

    @Test
    public void testConcurrentVoting() throws InterruptedException {
        Proposal proposal = proposalService.proposals().get(0);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        int numberOfVotes = 1000;
        Vote vote = new Vote();
        vote.setProposalId(proposal.getId());
        for (int i = 0; i < numberOfVotes; i++) {
            executorService.submit(() -> {
                proposalService.addVote(vote);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(numberOfVotes, proposal.getVoteCount(),
                "Votes should be " + numberOfVotes + " after concurrent voting");
    }
}


