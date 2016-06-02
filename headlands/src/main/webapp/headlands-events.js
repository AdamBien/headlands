var socket;

function connect() {
    socket = new WebSocket("ws://localhost:8080/headlands/firehose");
    socket.onopen = function (event) {
        console.log("connection opened", event);
    };
    socket.onmessage = function (event) {
        console.log(event);
    };
}



