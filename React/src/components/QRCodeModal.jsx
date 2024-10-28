import { useRef, useState } from "react";
import { useReactToPrint } from "react-to-print";
import {makeQRCode} from "../service/axiosFunc.js";

function QRCodeModal({closeModal, itemId, purchaseId}) {
    const [qrCodeUrl, setQrCodeUrl] = useState(null);
    const contentRef = useRef(null);

    const makeQRCodeHandler = () => {
        makeQRCode(itemId, purchaseId, ( result ) => {
            setQrCodeUrl(result)
        })
    }

    const handlePrint = useReactToPrint({ contentRef });

    return (
        <div className={'modal-overlay'}>
            <div className={'vendor-modal'}>
                <nav className={'navbar rounded-top-3'} style={{backgroundColor: 'rgba(15,76,128,.9)'}}>
                    <div className={'container-fluid d-flex justify-content-end'}>
                        <button className={'btn btn-sm btn-danger px-2'} onClick={closeModal}>X</button>
                    </div>
                </nav>
                <div className={'p-2 justify-content-center align-items-center'}>
                    <h2 className={'nanum-gothic-extrabold mt-3'}>QR 코드 생성 및 프린트</h2>
                    <div className={'my-3 d-flex justify-content-center align-items-center'}>
                    <button className={'btn btn-blue me-2'} onClick={makeQRCodeHandler}>QR 코드 생성</button>
                    {qrCodeUrl && (
                        <button className={'btn btn-outline-dark'} onClick={handlePrint}>프린트 하기</button>
                        )}
                    </div>
                    <div className={'mb-5 d-flex justify-content-center align-items-center'}>
                        <div className={'qr-box border border-1 p-1'}
                             ref={contentRef}
                             style={{
                                 width: '300px',
                                 height: '300px',
                                 backgroundImage: 'url(' + qrCodeUrl + ')',
                                 backgroundPosition: 'center',
                                 backgroundSize: 'cover',
                                 backgroundRepeat: 'no-repeat'
                             }}
                        >
                            {!qrCodeUrl && (<p>QR 코드를 생성하세요</p>)}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );

};

export default QRCodeModal;
