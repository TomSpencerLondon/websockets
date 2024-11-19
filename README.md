## Websockets Chat

```mermaid
sequenceDiagram
participant Client as Client (React/JS)
participant WebSocket as STOMP WebSocket
participant Server as Spring Boot Application
participant Controller as ChatController
participant Broker as Message Broker

    Client->>+WebSocket: Establish WebSocket connection (SockJS)
    WebSocket->>+Server: Connect to /chat endpoint
    Server-->>-WebSocket: Connection Established
    
    Client->>+WebSocket: Subscribe to /topic/messages
    WebSocket->>Broker: Register subscription

    Client->>+WebSocket: Send message to /app/sendMessage
    WebSocket->>+Server: Forward message to /sendMessage
    Server->>+Controller: Handle message in @MessageMapping("/sendMessage")
    
    Controller->>+Broker: Broadcast message to /topic/messages
    Broker-->>-WebSocket: Message dispatched to subscribers

    WebSocket-->>-Client: Receive message from /topic/messages
    Client-->>Client: Display message on UI

```

<img width="1724" alt="image" src="https://github.com/user-attachments/assets/f96c6e84-0de8-4d48-90af-857426528ab4">

# console

```bash
Opening Web Socket...
stomp.min.js:8 Web Socket Opened...
stomp.min.js:8 >>> CONNECT
accept-version:1.1,1.0
heart-beat:10000,10000


stomp.min.js:8 <<< CONNECTED
version:1.1
heart-beat:0,0


stomp.min.js:8 connected to server undefined
stomp.min.js:8 >>> SUBSCRIBE
id:sub-0
destination:/topic/messages


stomp.min.js:8 >>> SEND
destination:/app/sendMessage
content-length:35

{"sender":"User","content":"hello"}
stomp.min.js:8 <<< MESSAGE
destination:/topic/messages
content-type:application/json
subscription:sub-0
message-id:dvqspa0k-0
content-length:35

{"sender":"User","content":"hello"}
stomp.min.js:8 <<< MESSAGE
destination:/topic/messages
content-type:application/json
subscription:sub-0
message-id:dvqspa0k-2
content-length:45

{"sender":"User","content":"hi from cognito"}
```


This test is quite interesting for concurrency:
```java

public class ProposalServiceTest {

    private ProposalService proposalService;

    @BeforeEach
    public void setUp() {
        proposalService = new ProposalService();
        proposalService.addProposal(new Proposal("User 1", "Proposal 1"));
    }

    @RepeatedTest(1000)
    public void testConcurrentVoting() throws InterruptedException {
        // Get the proposal to test
        Proposal proposal = proposalService.proposals().get(0);

        // Increase the number of threads to create higher contention
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        // Simulate a high number of concurrent votes
        int numberOfVotes = 1000;
        Vote vote = new Vote();
        vote.setProposalId(proposal.getId());
        for (int i = 0; i < numberOfVotes; i++) {
            executorService.submit(() -> proposalService.addVote(vote));
        }

        // Shutdown the executor and wait for all tasks to complete
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // Assert that the vote count matches the number of submitted votes
        assertEquals(numberOfVotes, proposal.getVoteCount(),
                "Votes should be " + numberOfVotes + " after concurrent voting");
    }
}
```

We can then change the Proposal class voteCount to int to make the test fail. 
Another way to make the test pass would be to use synchronised keyword on ProposalService addVote.
The project as a whole is a relatively interesting exploration of websockets, server client interaction
and concurrency.