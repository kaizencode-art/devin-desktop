# DEVIN P2P
> Peer-to-Peer communication layer for DEVIN

## Objective

This module provides the foundation for peer-to-peer communication between DEVIN instances. It aims to enable the synchronization of environment variables across multiple nodes in a distributed fashion.

The architecture is designed to be transport-agnostic, allowing for different communication implementations while keeping the core logic decoupled from the underlying networking stack.

## Tech Stack

* **Language & Runtime:** Java 21+
* **Framework:** Vert.x Hazelcast (planned/experimental)
* **Testing:** JUnit 5
* **Models:** Java `record` (immutable) for `Message` and `NodeId`

## Architecture

The module is built around a few core components:

* **DevinP2P:** The main entry point that coordinates sending and receiving messages.
* **P2PTransport:** An interface defining how messages are moved across the network.
* **P2PMessageHandler:** An interface to handle incoming payloads.
* **NodeId:** A unique identifier for each peer in the network.
* **Message:** A record containing the sender's `NodeId` and a byte array payload.

### Internal Components

For testing and development, internal implementations are provided:
* **RecordingTransport:** A mock transport that records sent messages and allows simulating incoming traffic.
* **RecordingMessageHandler:** A mock handler that stores received messages for verification.

## Usage

```java
var transport = new RecordingTransport(); // Or a real implementation
var handler = message -> System.out.println("Received: " + new String(message.payload()));
var p2p = new DevinP2P(transport, handler);

p2p.send("Hello P2P!".getBytes());
```
