import React, {useEffect, useState} from "react";
import { useNavigate } from 'react-router-dom';
import bg1 from "../assets/bg1.png";
import bg2 from "../assets/bg2.png";
import bg3 from "../assets/bg3.png";
import {getDashBoardInoutCnt, getDashBoardPurchaseCnt} from "../service/axiosFunc.js";
import {commaNumber} from "../service/util.js";

function ReqListView() {
    const navigate = useNavigate();
    const handleClick = (path) => {
        navigate(path);
    };

    const colStyle = 'col-12 col-md-4 mb-3';
    const boxStyle = 'box-primary rounded-3 p-2 text-center d-flex flex-column justify-content-center';


    const[purchaseCnt, setPurchaseCnt] = useState({requestCnt : 0, approvedCnt : 0, canceledCnt : 0});


    useEffect(() =>{
        getDashBoardPurchaseCnt(data =>{
            setPurchaseCnt({... data});
        });
    }, []);

    return (
        <div className="d-flex flex-column h-100">
            <div>
                <h4 className={'nanum-gothic-bold text-start mb-3'}
                    style={{whiteSpace: 'nowrap'}}
                >결재 내역 조회</h4>
            </div>

            <div className="row flex-grow-1 d-flex g-1 p-1">
                <div className={colStyle}>
                    <div className={boxStyle}
                         style={{
                             backgroundImage: `url(${bg1})`,
                             height: '100%',
                             backgroundSize: 'cover',
                             backgroundRepeat: 'no-repeat',
                             backgroundPosition: 'bottom',
                         }}
                         onClick={() => handleClick('/history/requested')}>
                        <i className="bi bi-clipboard my-3" style={{ fontSize: '3rem', opacity: '0.7'}}></i>
                        <h4 className={'nanum-gothic-extrabold'}>요청</h4>
                        <h5 className={'nanum-gothic-extrabold'}>{commaNumber(purchaseCnt.requestCnt)}건</h5>
                    </div>
                </div>

                <div className={colStyle}>
                    <div className={boxStyle}
                         style={{
                           backgroundImage: `url(${bg2})`,
                           height: '100%',
                           backgroundSize: 'cover',
                           backgroundRepeat: 'no-repeat',
                           backgroundPosition: 'bottom',
                         }}
                         onClick={() => handleClick('/history/approved')}>
                        <i className="bi bi-clipboard-check my-3" style={{fontSize: '3rem', opacity: '0.7'}}></i>
                        <h4 className={'nanum-gothic-extrabold'}>승인</h4>
                        <h5 className={'nanum-gothic-extrabold'}>{commaNumber(purchaseCnt.approvedCnt)}건</h5>
                    </div>
                </div>

                <div className={colStyle}>
                    <div className={boxStyle}
                         style={{
                           backgroundImage: `url(${bg3})`,
                           height: '100%',
                           backgroundSize: 'cover',
                           backgroundRepeat: 'no-repeat',
                           backgroundPosition: 'bottom',
                         }}
                         onClick={() => handleClick('/history/rejected')}>
                        <i className="bi bi-clipboard-x my-3" style={{fontSize: '3rem', opacity: '0.7'}}></i>
                        <h4 className={'nanum-gothic-extrabold'}>반려</h4>
                        <h5 className={'nanum-gothic-extrabold'}>{commaNumber(purchaseCnt.canceledCnt)}건</h5>
                    </div>
                </div>
            </div>

            <div className="mt-auto mb-2">
                <button className={'btn btn-outline-primary w-100 mt-3 py-2 px-3 rounded-2 nanum-gothic-extrabold'}
                        onClick={() => handleClick('/history')}>
                    전체목록
                </button>
            </div>
        </div>
    );
}

export default ReqListView;
