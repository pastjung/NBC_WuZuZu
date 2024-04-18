import {apiClient} from "../Client";

const startUrl = "/api/v1";

export const getMyRooms = async () => {
    return apiClient.get(startUrl + `/chat-rooms/my-rooms`);
};

export const getAllRooms = async () => {
    return apiClient.get(startUrl + `/chat-rooms`);
}

export const createChatRoom = async (chatRoom) => {
    return apiClient.post(startUrl + `/chat-rooms`, chatRoom);
}