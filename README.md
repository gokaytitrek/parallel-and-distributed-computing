# parallel-and-distributed-computing #

Implementation of the parallel and distributed computing methods. These methods are written with pure Java by me. Some of them are need performance improvements. 

## Implemented Algorithms/Methods

* Socket programming
* Server-Client AND P2P communication
* Distrubuted Hash Table (DHT)
* Causally Ordered Multicast Communication
* Totally Ordered Multicast

## Extra Information about Causally Ordered Multicast Communication ##
I used JAVA MulticastSocket class -which uses User Datagram Protocol- to
connect nodes to each other.  A created node joins to specified group on the system
over the network. Messages cannot be processed when a new node joins to the system
after communication started. In this case, all receiving messages added to queue.
Because I don’t have a centralized structure, I couldn’t find how to transfer messages to
the new node so I could not fix that problem yet. There isn’t a constant number of nodes,
so for holding the count of nodes, I have static variable in main class. I take advantages
of GUI to follow the protocol easily. I also use some additional functionality for simulating
delayed messages.
