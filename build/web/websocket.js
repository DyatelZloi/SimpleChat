
var socket = new WebSocket("ws://localhost:8080/WebChat/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    var message = JSON.parse(event.data);
    if (message.action === "add") {
        printMessageElement(message);
    }
    if (message.action === "remove") {
        document.getElementById(message.id).remove();
    }
}

function addMessage(message) {
    var MessageAction = {
        action: "add",
        message: message
    };
    socket.send(JSON.stringify(MessageAction));
}

function removeMessage(element) {
    var id = element;
    var MessageAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(MessageAction));
}

function printMessageElement(message) {
    var content = document.getElementById("messages");
    
    var messageDiv = document.createElement("div");
    messageDiv.setAttribute("id", message.id);
    content.appendChild(messageDiv);

    var messageMy = document.createElement("div");
    messageMy.innerHTML = message.message;
    messageDiv.appendChild(messageMy);

    var MessageDevice = document.createElement("span");
    MessageDevice.innerHTML = "<a href=\"#\" OnClick=removeMessage(" + message.id + ")>Удалить сообщение</a>";
    messageDiv.appendChild(MessageDevice);
}

function formSubmit() {
    var form = document.getElementById("addMessageForm");
    var message = form.elements["message"].value;
    document.getElementById("addMessageForm").reset();
    addMessage(message);
}
