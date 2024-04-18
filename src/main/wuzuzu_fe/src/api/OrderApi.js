import {apiClient} from "../Client";

const startUrl = "/api/v1/orders";

export const createOrder = async (request, imp_uid) => {
    return apiClient.post(startUrl + `/${imp_uid}`, request);
}
