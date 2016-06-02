var socket;

function connect() {
    socket = new WebSocket("ws://localhost:8080/headlands/firehose");
    socket.onmessage = function (event) {
        console.log(event.data);
    };
}



