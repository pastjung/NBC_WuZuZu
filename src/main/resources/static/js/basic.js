const host = 'http://' + window.location.host;

$(function() {
    const token = getToken();

    if (token !== undefined && token !== '') {
        // Ajax 요청 시 헤더에 토큰 설정
        $.ajaxSetup({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', token);
            }
        });
    } else {
        window.location.href = host + `/login`;
    }
})

function getToken() {

    let token = Cookies.get('Authorization');

    if(token === undefined) {
        return '';
    }

    return token;
}