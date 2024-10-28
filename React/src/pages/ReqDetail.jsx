import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import noImage from "../assets/no-image-svgrepo-com.svg";
import button from "bootstrap/js/src/button.js";
import QRCodeModal from "../components/QRCodeModal.jsx";
import {approvePurchase, deletePurchase, getPurchaseDetail, rejectPurchase} from "../service/axiosFunc.js";
import {parseImageName} from "../store/Unit.js";


function ReqDetail({role}) {
    console.log(role);
    const {id} = useParams();
    const navigate = useNavigate();
    const goHistory = () => navigate('/history');

    const [editedRequest, setEditedRequest] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [purchaseDetail, setPurchaseDetail] = useState(null);

    const handleApprove = () => {
        approvePurchase(() => {
            alert('승인이 완료되었습니다.');
            goHistory();
        }, id, purchaseDetail.approvalComment);
    };

    const handleCancel = () => {
        rejectPurchase(() => {
            alert('반려가 완료되었습니다.');
            goHistory();
        }, id, purchaseDetail.approvalComment);
    };

    const handleDelete = () => {
        console.log(`delete purchaseId: ${id}`)
        if (window.confirm("정말로 삭제하시겠습니까?")) {
            deletePurchase(id, () => {
                goHistory();
            })
        }
    };

    const handleEmail = () => {
        const email = purchaseDetail?.item.company.email;
        const itemName = purchaseDetail?.item.name;
        const reqQuantity = purchaseDetail?.reqNum;

        navigate('/vendor/mail', {
            state: {
                email: email,
                item: itemName,
                requestQuantity: reqQuantity
            },
        });
    };

    const openModal = () => setShowModal(true);
    const closeModal = () => setShowModal(false);

    useEffect(() => {
        getPurchaseDetail((data) => {
            setPurchaseDetail(data);
            console.log(data);
            console.log(
                `purchaseId: ${purchaseDetail.id}, 
          itemId: ${purchaseDetail.item.id}`
            )
        }, id)
    }, [id]);

    return (
        <main>
            <h1 className={'nanum-gothic-extrabold'}>결재 내역 상세</h1>
            <div className={'d-flex m-5 justify-content-center align-content-center'}>
                <div className={'mt-1 p-3'}>
                    <div className={'mt-3 req-content mx-5 p-3 border border-1 rounded-1'}>
                        <div className={'d-flex p-3 justify-content-start'}>
                <span className={'nanum-gothic-bold text-start fs-5 cursor-pointer'}
                      style={{color: 'rgba(15, 76, 129, .9)', cursor: 'pointer'}}
                      onClick={goHistory}
                >
                  <i className={'bi bi-box-arrow-in-left me-1'}></i>
                  목록으로
                </span>
                        </div>
                        <div className={'row mt-1 p-3 justify-content-center align-items-center'}>
                            <div className={'col-sm-8'}>
                                <label className={'fs-5 nanum-gothic-bold float-start'}>요청서제목</label>
                                <input type="text" className={'form-control'} readOnly name={'title'}
                                       value={purchaseDetail?.title}/>
                            </div>
                        </div>
                        <section className={'row p-3 justify-content-center align-items-center'}>
                            <div className={'d-grid col-sm-8'}>
                                <h3 className={'fs-5 nanum-gothic-bold text-start'}>비품 정보</h3>
                                <div className={'col-sm border rounded-3'}
                                     style={{backgroundColor: 'rgba(224,232,243,0.48)'}}>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>비품명</label>
                                                <input
                                                    type="text"
                                                    className={'form-control'}
                                                    readOnly
                                                    name="item"
                                                    value={purchaseDetail?.item.name}
                                                />
                                            </div>
                                        </div>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>비품 이미지</label>
                                                <input type="text" className={'form-control'} readOnly
                                                       value={parseImageName(purchaseDetail?.item.thumbNail)}/>
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div
                                                className={'img-preview bg-white border border-1 rounded-3 d-flex justify-content-center align-items-center'}
                                                style={{
                                                    backgroundImage: purchaseDetail?.item.thumbNail ? 'url(' + purchaseDetail?.item.thumbNail + ')' : `url(${noImage})`,
                                                    backgroundSize: 'contain',
                                                    backgroundPosition: 'center'
                                                }}
                                            >
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>거래처명</label>
                                                <input type="text" className={'form-control'} readOnly name="supplier"
                                                       value={purchaseDetail?.item.company.name}/>
                                            </div>
                                        </div>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>창고명</label>
                                                <input type="text" className={'form-control'} readOnly name="warehouse"
                                                       value={purchaseDetail?.item.container.section}/>
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        {purchaseDetail?.newYn === 'N' && (
                                            <div className={'col-sm'}>
                                                <div className={'d-grid'}>
                                                    <label className={'nanum-gothic-bold text-start'}>재고수량</label>
                                                    <input type="number" className={'form-control'} readOnly
                                                           name="stock"
                                                           value={purchaseDetail?.item.totalNum}/>
                                                </div>
                                            </div>
                                        )}
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>구매 요청 수량</label>
                                                <input type="number" className={'form-control'} readOnly
                                                       name="requestQuantity"
                                                       value={purchaseDetail?.reqNum}/>
                                            </div>
                                        </div>
                                        {purchaseDetail?.newYn === 'Y' && (
                                            <div className={'col-sm'}>
                                                <div className={'d-grid'}>
                                                    <label className={'nanum-gothic-bold text-start'}>적정수량</label>
                                                    <input type="number" className={'form-control'} readOnly
                                                           value={purchaseDetail?.item.minNum}/>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>단가</label>
                                                <div className={'d-flex align-items-baseline'}>
                                                    <input type="number" className={'form-control me-2'}
                                                           readOnly
                                                           value={purchaseDetail?.item.price}
                                                    />
                                                    <label className={'nanum-gothic-bold'}>원</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                        <section className={'row p-3 justify-content-center align-items-center'}>
                            <div className={'d-grid col-sm-8'}>
                                <label className={'fs-5 nanum-gothic-bold text-start'}>요청 코멘트</label>
                                <textarea className={'form-control'}
                                          readOnly
                                          name="comment"
                                          value={purchaseDetail?.reqComment}
                                          style={{height: 'auto', overflowY: 'auto'}}
                                />
                                {purchaseDetail?.approvedStatus === 'WAIT' ? (
                                    <>
                                        {!role && (
                                            <>
                                                <label className={'mt-3 fs-5 nanum-gothic-bold text-start'}>결재
                                                    코멘트</label>
                                                <textarea
                                                    className={'form-control'}
                                                    name="approvalComment"
                                                    value={purchaseDetail?.approvalComment}
                                                    rows={5}
                                                    onChange={(e) => setPurchaseDetail(({
                                                        ...purchaseDetail,
                                                        approvalComment: e.target.value
                                                    }))}
                                                />
                                            </>
                                        )}
                                    </>
                                ) : (
                                    <>
                                        <label className={'mt-3 fs-5 nanum-gothic-bold text-start'}>결재 코멘트</label>
                                        <textarea
                                            className={'form-control'}
                                            readOnly
                                            name="approvalComment"
                                            value={purchaseDetail?.approvalComment}
                                            rows={5}
                                        />
                                    </>
                                )}
                            </div>
                        </section>
                        <div className={'mt-3'}>
                            {purchaseDetail?.approvedStatus === 'WAIT' && (
                                <>
                                    {role ? (
                                        <button type={"button"} className={'px-4 me-2 btn btn-danger nanum-gothic-bold'}
                                                onClick={handleDelete}>
                                            상신취소
                                        </button>
                                    ) : (
                                        <>
                                            <button type={'button'}
                                                    className={'px-4 me-2 btn btn-success nanum-gothic-bold'}
                                                    onClick={handleApprove}>승인
                                            </button>
                                            <button type={'button'}
                                                    className={'px-4 me-2 btn btn-danger nanum-gothic-bold'}
                                                    onClick={handleCancel}>반려
                                            </button>
                                        </>
                                    )}
                                </>
                            )}
                            {purchaseDetail?.approvedStatus === 'APPROVE' && (
                                <>
                                    {role ? (
                                        <>
                                            {purchaseDetail?.newYn === 'Y' && (
                                                <button type={'button'} className={'px-4 me-2 btn btn-blue'}
                                                        onClick={openModal}>QR 발행</button>
                                            )}
                                            <button type={'button'} className={'px-4 me-2 btn btn-success'}
                                                    onClick={handleEmail}>거래처 메일발송
                                            </button>
                                        </>
                                    ) : (
                                        <></>
                                    )}
                                </>
                            )}
                            {purchaseDetail?.newYn === 'CANCEL' && (
                                <>
                                </>
                            )}
                        </div>
                    </div>
                </div>
            </div>
            {showModal &&
                <QRCodeModal itemId={purchaseDetail.item.id} purchaseId={purchaseDetail.id} closeModal={closeModal}/>}
        </main>
    );
}

export default ReqDetail;