# Project: Chat Client-Server (Assembly x64 + Java)

This project demonstrates a chat system where:
- The **client** is written in **Assembly x64 for Windows**, using **WinSock**.
- The **server** is built in **Java**, allowing communication with multiple clients.
- Communication happens through a **VPN**, specifically **Radmin**.

---

## :hammer: Technologies Used

- Assembly x64 (MASM)
- Visual Studio 2022
- WinSock 2
- Java 17+
- Radmin VPN

---

## :electric_plug: How It Works

1. The Java server listens for connections on port 12345.
2. The Assembly client connects via TCP using WinSock.
3. The server broadcasts any received message to all connected clients.
4. The IP used is the **Radmin VPN IP** of the machine that starts the server.

---

## :rocket: How to Run the Project

### 1. Compile the Assembly Client

> Requirements:
> - Visual Studio 2022 with C++/Assembly support

1. Create a new **Assembly x64 project** in Visual Studio.
2. Add the provided `main.asm` code.
3. Link the following libraries:

```
msvcrt.lib
ucrt.lib
legacy_stdio_definitions.lib
ws2_32.lib
kernel32.lib
```

4. Build and run the project. A console window will open for input.

### 2. Run the Java Server

> Requirements:
> - JDK installed **or use the pre-built `ChatServer.exe`**


run the precompiled `ChatServer.exe`.

The server will start listening on `localhost:12345`, but must be reachable through **Radmin VPN** using the host machine's virtual IP.

### 3. Run the Java Client

You can either:
- Compile `ChatClient.java`, or
- Use the provided `ChatClient.exe`

---

## :computer: Java Server Structure

### `ChatServer.java`
- Uses `ServerSocket` to handle multiple client connections.
- Clients are stored in a thread-safe list.
- Each client is assigned a thread to listen and respond.
- `/apelido <name>` command sets a nickname.

### `ChatClient.java`
- Simple Java client for local or VPN testing.
- Connects to the server and sends/receives messages.

---

## :scroll: Assembly Client Explanation

1. **WSAStartup**: initializes the networking API.
2. **socket**: creates a TCP socket.
3. **connect**: connects to the server (via Radmin VPN) on port 12345.
4. **ReadConsoleA**: reads user input from the console.
5. **send / recv**: sends messages to the server and receives responses.
6. **printf**: prints the received messages.

---

## :warning: Notes

- The Assembly client does not support nicknames or formatted messages.
- Communication is in raw text via TCP, ending with `\n`.
- The IP to connect must be the **Radmin VPN IP** of the server host.

---

## :camera: Visual Examples (optional)

1. Assembly client sending messages
2. Group chat with multiple connected clients

---

## :bookmark: Credits

This project was created by Samir Tajra as a challenge to integrate Assembly and Java in a real-time networked chat system using VPN.

---


