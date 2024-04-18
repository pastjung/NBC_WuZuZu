import {createOrder} from "../api/OrderApi";

const pg = {
    kakao: 'kakaopay'
}

const pay_method = {
    card: 'card'
}

export const requestPay = async ({user, salePost, count}) => {
    const {IMP} = window;
    IMP.init(`${process.env.REACT_APP_IMP}`);

    IMP.request_pay({
        pg: pg.kakao,
        pay_method: pay_method.card,
        merchant_uid: new Date().getTime(),
        name: salePost.goods,
        amount: salePost.price * count,
        buyer_email: user.email,
        buyer_name: user.userName,
    }, async (rsp) => {
        try {
            // const {data} = await axios.post(
            //     'http://localhost:8080/verify-iamport/' + rsp.imp_uid);
            const {data} = await createOrder({
                salePostId: salePost.salePostId,
                count: count,
                amount: salePost.price * count
            }, rsp.imp_uid);
            console.log(data);
        } catch (error) {
            console.error('Error while verifying payment:', error);
            alert('결제 실패');
        }
    });
};