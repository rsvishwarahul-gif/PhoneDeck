import { WebSocketServer, WebSocket } from "ws";
import os from "node:os";

const PORT = 8765;

const server = new WebSocketServer({ port: PORT });

function localIPv4(): string[] {
  const result: string[] = [];
  for (const interfaces of Object.values(os.networkInterfaces())) {
    for (const item of interfaces ?? []) {
      if (item.family === "IPv4" && !item.internal) result.push(item.address);
    }
  }
  return result;
}

console.log(`\nPhoneDeck receiver running on port ${PORT}`);
console.log("Mac LAN IP(s):", localIPv4().join(", ") || "not found");
console.log("Waiting for Android controller...\n");

server.on("connection", (socket: WebSocket) => {
  console.log("Phone connected.");

  socket.send(JSON.stringify({
    type: "hello",
    name: "PhoneDeck Mac Receiver",
    version: "0.1.0"
  }));

  socket.on("message", (raw) => {
    try {
      const message = JSON.parse(raw.toString());

      if (message.type === "command") {
        console.log(
          `[COMMAND] ${message.id ?? "unknown"} ${message.label ?? ""}`
        );

        // Intentionally log-only in this starter.
        // Connect this layer to macOS automation after testing.
      }
    } catch {
      console.log("Received invalid JSON.");
    }
  });

  socket.on("close", () => console.log("Phone disconnected."));
});

server.on("error", (error) => {
  console.error("WebSocket server error:", error);
});
