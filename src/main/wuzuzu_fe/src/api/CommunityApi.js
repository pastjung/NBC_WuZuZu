import {apiClient} from "../Client";
import communityPost from "../Community/CommunityPost";

const startUrl = "/api/v1";

export const searchPosts = async (searchParams) => {
    const {
        page,
        pageSize,
        column,
        title,
        contents,
        keyword,
        categoryName,
    } = searchParams;

    const params = {};

    if (page) {
        params.page = page;
    }
    if (pageSize) {
        params.pageSize = pageSize;
    }
    if (column) {
        params.column = column;
    }
    if (title) {
        params.title = title;
    }
    if (contents) {
        params.contents = contents;
    }
    if (keyword) {
        params.keyword = keyword;
    }
    if (categoryName) {
        params.categoryName = categoryName;
    }

    return apiClient.get(startUrl + `/communityposts/search`, {params});
};

export const getPostDetail = async (communityPostId) => {
    return apiClient.get(startUrl + `/communityposts/${communityPostId}`);
}

export const likePost = async (communityPostId) => {
    return apiClient.post(startUrl + `/communityposts/${communityPostId}/likes`);
}

export const createCommunityPost = async (communityPost, image) => {
    const formData = new FormData();
    await formData.append('communityPost', new Blob([JSON.stringify(communityPost)], {type: 'application/json'}));

    if (image) {
        formData.append('image', image);
    }

    try {
        const response = await apiClient.post(startUrl + `/communityposts`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response;
    } catch (error) {
        console.error('Error creating post:', error);
        throw error;
    }
};

export const uploadImage = async (communityPostId, image) => {
    const formData = new FormData();
    formData.append('image', image);

    try {
        const response = await apiClient.post(
            startUrl + `/communityposts/${communityPostId}/multipart-files`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            }
        );
        return response;
    } catch (error) {
        console.error('Error uploading image:', error);
        throw error;
    }
};

export const createComment = async (communityPostId, comment) => {
    return apiClient.post(startUrl + `/comments/communityposts/${communityPostId}`, comment);
}

export const getComments = async (communityPostId) => {
    return apiClient.get(startUrl + `/comments/communityposts/${communityPostId}`);
}

export const deleteComment = async (communityPostId, comment) => {
    return apiClient.delete(startUrl + `/comments/${communityPostId}`);
}