import React, {useEffect, useState} from 'react';
import './Pages.css';
import InOutViewComponent from "../components/InOutViewComponent.jsx";
import MinValItemsView from "../components/MinValItemsView.jsx";
import ReqListView from "../components/ReqListView.jsx";
import {useNavigate} from "react-router-dom";
import newItem from "../assets/newStock.png";
import vendor from "../assets/Vendor.png";
import newUser from "../assets/newUser.png";
import allUser from "../assets/EntireUser.png";
import {checkAccess} from "../service/axiosFunc.js";


function Statistics({role}) {
    const navigate = useNavigate();
    const [totalMinItems, setTotalMinItems] = useState(0);

    const [checkAuth, setCheckAuth] = useState(false);

    const handleTotalCntChange = (cnt) => {
        setTotalMinItems(cnt);
    };

    const topBtnHandler = () => {
        navigate(role ? `/req/new` : '/user/req');
    }
    const bottomBtnHandler = () => {
        navigate(role ? `/vendor` : '/user');
    }
    const commonBoardStyle = 'h-100 rounded-3 bg-white pt-4 pb-3 px-2';

    useEffect(() => {
        checkAccess(() => setCheckAuth(true));
    }, []);


    return (
        <div>
            {
                checkAuth === true ? (<div className={`sub-content-container`}>
                        <h1>대시보드</h1>
                        <div className={'row g-3'}
                             style={{height: 400}}>
                            <div className={'col-4'}>
                                <div className={commonBoardStyle} style={{boxShadow: '5px 5px 10px -10px'}}>
                                    <InOutViewComponent/>
                                </div>
                            </div>
                            <div className={'col-4'}>
                                <div className={commonBoardStyle} style={{boxShadow: '5px 5px 10px -10px'}}>
                                    <ReqListView/>
                                </div>
                            </div>

                            <div className={'col-4'}>
                                <div className="d-flex flex-column h-100">
                                    <div className="flex-grow-1 d-flex">
                                        <div className={`${commonBoardStyle} box`}
                                             onClick={topBtnHandler}
                                             style={role ? {
                                                 width: '100%',
                                                 backgroundImage: `url(${newItem})`,
                                                 backgroundSize: 'cover',
                                                 backgroundPosition: 'bottom',
                                                 backgroundRepeat: 'no-repeat',
                                                 boxShadow: '5px 5px 10px -10px'
                                             } : {
                                                 width: '100%',
                                                 backgroundImage: `url(${newUser})`,
                                                 backgroundSize: 'cover',
                                                 backgroundPosition: 'bottom',
                                                 backgroundRepeat: 'no-repeat',
                                                 boxShadow: '5px 5px 10px -10px'
                                             }}>
                                            <h4 className={'nanum-gothic-extrabold text-start mb-3 text-white'}
                                                style={{whiteSpace: 'nowrap'}}
                                            >{role ? '신규 비품 등록' : '신규 직원 등록 요청'}</h4>
                                        </div>
                                    </div>

                                    <div className="flex-grow-1 d-flex mt-2">
                                        <div className={`${commonBoardStyle} box`}
                                             onClick={bottomBtnHandler}
                                             style={role ? {
                                                 width: '100%',
                                                 backgroundImage: `url(${vendor})`,
                                                 backgroundSize: 'cover',
                                                 backgroundPosition: 'bottom',
                                                 backgroundRepeat: 'no-repeat',
                                                 boxShadow: '5px 5px 10px -10px'
                                             } : {
                                                 width: '100%',
                                                 backgroundImage: `url(${allUser})`,
                                                 backgroundSize: 'cover',
                                                 backgroundPosition: 'bottom',
                                                 backgroundRepeat: 'no-repeat',
                                                 boxShadow: '5px 5px 10px -10px'
                                             }}>
                                            <h4 className={'nanum-gothic-extrabold text-start mb-3 text-white'}
                                                style={{whiteSpace: 'nowrap'}}
                                            >{role ? '거래처 관리' : '전체 직원 조회'}</h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        {role && (
                            <div className={'row my-5 flex-grow-1'}>
                                <div className={'col'}>
                                    <div className="status-header text-center">
                                        <h4 className={'nanum-gothic-bold text-start'}
                                            style={{whiteSpace: 'nowrap'}}
                                        >적정 수량 미만 비품 <span>{totalMinItems}</span>건</h4>
                                    </div>
                                    <div className={'rounded-3 p-3 h-100 bg-white'}
                                         style={{boxShadow: '5px -5px 10px -10px'}}>
                                        <MinValItemsView onToTalCntChange={handleTotalCntChange}/>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>)
                    : (<></>)
            }
        </div>
    );
}

export default Statistics;
