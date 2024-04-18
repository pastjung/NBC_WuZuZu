import {apiClient} from "../Client";

const startUrl = "/api/v1";

export const getRoomMessages = async (chatRoomId) => {
    return apiClient.get(startUrl + `/chat-rooms/${chatRoomId}/messages`);
};