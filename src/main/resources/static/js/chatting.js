let currentRoomId = 0;
let messageList;
let messageInput;

// STOMP 클라이언트 초기화
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket' // 엔드포인트
});

// 연결 성공 시 콜백 함수
stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    setConnected(true);
    getRoomMessages(currentRoomId);
    stompClient.subscribe(`/topic/chat-rooms/${currentRoomId}`,
        onMessageReceived); // 서버로부터 받은 메시지(Body)를 보여줌
};

// 웹소켓 오류 발생 시 콜백 함수
stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

// STOMP 메시지 브로커에서 오류 발생 시 콜백 함수
stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

// 방에 있는 모든 메시지
function getRoomMessages(roomId) {
    $.ajax({
        url: `/api/v1/chat-rooms/${roomId}/messages`,
        type: "GET",
        dataType: "json",
        success: function (response) {
            const messages = response.data;

            // 메시지 목록 처리 로직 작성
            messages.forEach(function (message) {
                const messageRow = `
                    <tr>
                        <td>${message.userName}</td>
                        <td>${message.content}</td>
                    </tr>
                `;
                messageList.append(messageRow);
            });
        },
        error: function (xhr, status, error) {
            console.error(
                "Failed to retrieve room messages. Status: " + status);
        }
    });
}

// 메시지 수신
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageRow = `
        <tr>
            <td>${message.userName}</td>
            <td>${message.content}</td>
        </tr>
    `;
    messageList.append(messageRow);
}

// 연결에 따른 UI 설정
function setConnected(connected) {
    if(connected) {
        $("#room-list").hide();
        $("#chat-area").show();
    }
    else if(!connected) {
        $("#room-list").show();
        $("#chat-area").hide();
    }

    messageList.html("");
}

// 메세지 보내는 함수
function sendMessage() {
    const content = messageInput.val();
    stompClient.publish({
        destination: `/app/chat-rooms/${currentRoomId}/messages`,
        headers: {Authorization: getToken()},
        body: JSON.stringify({'content': content})
    });
    messageInput.val("");
}

// STOMP 에 웹소켓 연결 활성화
function connect() {
    stompClient.activate();
}

// 웹소켓 연결 비활성화 및 연결 상태 false 로 설정
function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// 방 목록 가져오기 함수
function getRoomList() {
    $.ajax({
        url: "/api/v1/chat-rooms",
        type: "GET",
        dataType: "json",
        success: function (rooms) {
            let roomListUl = $("#find-rooms-list");
            roomListUl.empty();
            rooms.forEach(function (room) {
                let li = $("<li></li>");
                let roomInfo = $("<div></div>").html(`
                    방 번호: ${room.chatRoomId}<br>
                    방 이름: ${room.chatRoomName}<br>
                    방장: ${room.userName}<br>
                    설명: ${room.description}
                `);
                let enterButton = $("<button></button>").text("입장").addClass(
                    "btn btn-primary btn-sm");
                enterButton.click(function () {
                    enterRoom(room.chatRoomId);
                });
                li.append(roomInfo);
                li.append(enterButton);
                roomListUl.append(li);
            });
        },
        error: function (xhr, status, error) {
            console.error(
                "Failed to retrieve room messages. Status: " + status);
        }
    });
}

function enterRoom(roomId) {
    $.ajax({
        url: "/api/v1/chat-rooms/" + roomId,
        type: "POST",
        dataType: "json",
        success: function (response) {
            console.log("방 입장 성공:", response);
            currentRoomId = roomId;
            connect();
        },
        error: function (xhr, status, error) {
            console.error("방 입장 실패. Status: " + status);
        }
    });
}

// 내 채팅방 찾기
function getMyRoomList() {
    $.ajax({
        url: "/api/v1/chat-rooms/my-rooms",
        type: "GET",
        dataType: "json",
        success: function (rooms) {
            let roomListUl = $("#my-rooms-list");
            roomListUl.empty();
            rooms.forEach(function (room) {
                let li = $("<li></li>");
                let roomInfo = $("<div></div>").html(`
                    방 번호: ${room.chatRoomId}<br>
                    방 이름: ${room.chatRoomName}<br>
                    방장: ${room.userName}<br>
                    설명: ${room.description}
                `);
                let enterButton = $("<button></button>").text("입장").addClass(
                    "btn btn-primary btn-sm");
                enterButton.click(function () {
                    currentRoomId = room.chatRoomId;
                    connect();
                });
                li.append(roomInfo);
                li.append(enterButton);
                roomListUl.append(li);
            });
        },
        error: function (xhr, status, error) {
            console.error(
                "Failed to retrieve room messages. Status: " + status);
        }
    });
}

// 방 생성 함수
function createRoom() {
    let roomName = $("#new-room-name").val();
    let roomDesc = $("#new-room-desc").val();

    $.ajax({
        url: "/api/v1/chat-rooms",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            chatRoomName: roomName,
            description: roomDesc,
            coverImage: "",
            chatRoomTags: [""]
        }),
        success: function (createdRoom) {
            $("#new-room-name").val("");
            $("#new-room-desc").val("");
            getRoomList();
        },
        error: function (error) {
            console.error("Failed to create room: " + error);
        }
    });
}

$(function () {
    messageList = $("#messageList");
    messageInput = $("#messageInput");

    $("#exitRoom").click(function () {
        disconnect();
    });

    $("#sendMessage").click(function () {
        sendMessage();
    })

    $("#create-room").click(function () {
        createRoom();
    })

    $("#my-rooms-btn").click(function () {
        $("#my-rooms-list").show();
        $("#find-rooms-list").hide();
        getMyRoomList();
    });

    $("#find-rooms-btn").click(function () {
        $("#my-rooms-list").hide();
        $("#find-rooms-list").show();
        getRoomList();
    });
});