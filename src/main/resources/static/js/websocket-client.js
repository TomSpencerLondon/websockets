let stompClient = null;
let socket = null;

// Connect to the WebSocket server
function connect() {
    socket = new SockJS('/proposal');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        subscribeToTopics();
    });
}

// Subscribe to proposal and vote topics
function subscribeToTopics() {
    stompClient.subscribe('/topic/proposals', (messageOutput) => {
        showProposal(JSON.parse(messageOutput.body));
    });

    stompClient.subscribe('/topic/votes', (messageOutput) => {
        updateVoteCount(JSON.parse(messageOutput.body));
    });
}

// Send a proposal
function sendProposal() {
    const inputElement = document.getElementById('proposal-input');
    const content = inputElement.value.trim();

    if (content) {
        stompClient.send("/app/sendProposal", {}, JSON.stringify({
            'sender': 'User',
            'content': content,
            'voteCount': 0
        }));
        inputElement.value = '';
    }
}

// Send a vote for a specific proposal
function sendVote(proposalId) {
    stompClient.send("/app/sendVote", {}, JSON.stringify({
        'proposalId': proposalId
    }));
}

// Display a proposal in the list
function showProposal(proposal) {
    const proposalList = document.getElementById('proposal-list');

    // Create a new proposal item
    const proposalElement = document.createElement('li');
    proposalElement.classList.add('message-item', 'flex', 'justify-between', 'items-center', 'p-2', 'border-b', 'border-gray-300');
    proposalElement.setAttribute('data-proposal-id', proposal.id);

    // Proposal details
    const detailsDiv = document.createElement('div');
    detailsDiv.innerHTML = `
        <span class="font-semibold text-blue-500">${proposal.sender}</span>:
        <span class="text-gray-800">${proposal.content}</span>
        <span class="text-gray-500 italic"> (ID: ${proposal.id})</span>
    `;

    // Vote count display
    const voteCountSpan = document.createElement('span');
    voteCountSpan.id = `vote-count-${proposal.id}`;
    voteCountSpan.classList.add('ml-4', 'text-sm', 'text-gray-700');
    voteCountSpan.setAttribute('data-vote-count', `${proposal.voteCount || 0}`);
    voteCountSpan.textContent = `Votes: ${proposal.voteCount || 0}`;

    // Vote button
    const voteButton = document.createElement('button');
    voteButton.textContent = 'Vote Yes';
    voteButton.classList.add('ml-4', 'bg-green-500', 'text-white', 'px-2', 'py-1', 'rounded');
    voteButton.onclick = () => sendVote(proposal.id);

    // Append elements
    proposalElement.appendChild(detailsDiv);
    proposalElement.appendChild(voteCountSpan);
    proposalElement.appendChild(voteButton);
    proposalList.appendChild(proposalElement);
}

// Update vote count in real-time
function updateVoteCount(vote) {
    const voteCountElement = document.getElementById(`vote-count-${vote.id}`);
    if (voteCountElement) {
        const currentCount = parseInt(voteCountElement.getAttribute('data-vote-count'), 10) || 0;
        const updatedCount = currentCount + 1;

        voteCountElement.setAttribute('data-vote-count', updatedCount);

        voteCountElement.textContent = `Votes: ${updatedCount}`;
    }
}


// Event listeners for proposal submission
function addEventListeners() {
    document.getElementById('submit-proposal').addEventListener('click', () => sendProposal());
    document.getElementById('proposal-input').addEventListener('keyup', (event) => {
        if (event.key === 'Enter') sendProposal();
    });
}

// Initialize on page load
connect();
addEventListeners();
