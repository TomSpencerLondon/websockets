package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProposalController {

    private final ProposalService proposalService;

    // Inject the ProposalManager
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @GetMapping("/")
    public String redirectToProposalPage() {
        return "redirect:/proposal";
    }

    @GetMapping("/proposal")
    public String proposalPage(Model model) {
        List<Proposal> proposalList = proposalService.proposals();
        model.addAttribute("proposals", proposalList);
        return "proposal";
    }

    @MessageMapping("/sendProposal")
    @SendTo("/topic/proposals")
    public Proposal addProposal(Proposal proposal) {
        return proposalService.addProposal(proposal);
    }

    @MessageMapping("/sendVote")
    @SendTo("/topic/votes")
    public Proposal handleVote(Vote vote) {
        proposalService.addVote(vote);
        return proposalService.getProposalById(vote.getProposalId());
    }
}
