import React, {useEffect, useState} from "react";
import SetDateRange from "./SetDateRange.jsx";
import {useNavigate} from "react-router-dom";
import './InOutViewComponent.css';
import {Card, Image} from "react-bootstrap";
import {colors} from "../store/Unit.js";
import bg4 from "../assets/bg4.png";
import bg5 from "../assets/bg5.png";
import {getDashBoardInoutCnt, getDashBoardPurchaseCnt} from "../service/axiosFunc.js";
import {commaNumber} from "../service/util.js";

function InOutViewComponent() {
    const navigate = useNavigate();
    const goInStock = () => {
        navigate("/instock");
    };
    const goUseStock = () => {
        navigate("/usestockchk");
    };

    const cardStyle = 'h-100 text-center box border-0'


    const [date, setDate] = useState("D");
    const[inoutCnt, setInoutCnt] = useState({inStoreCnt : 0, usageCnt : 0});


    useEffect(() => {
        getDashBoardInoutCnt((data) =>{
            setInoutCnt({ ... data});
        }, date);
    }, [date]);




    return (
        <div className="d-flex flex-column h-100">
            <div>
                <div className="status-header text-center">
                    <h4 className={'nanum-gothic-bold text-start mb-3'}
                        style={{whiteSpace: 'nowrap'}}
                    >입출고현황 조회</h4>
                    <SetDateRange setDate={setDate}/>
                </div>
                <hr className="w-100"/>
            </div>
            <div className="d-flex flex-grow-1 w-100">
                <div className="row w-100 mx-auto mb-3 px-3">
                    <div className="col-12 col-md-6 mb-2 mb-md-0">
                        <Card className={cardStyle}
                              style={{
                                  backgroundImage: `url(${bg4})`,
                                  backgroundSize: 'cover',
                                  backgroundRepeat: 'no-repeat',
                                  backgroundPosition: 'bottom',
                                  boxShadow: '5px 5px 10px -10px'
                              }}
                              onClick={goInStock}>
                            <i className="bi bi-download"
                               style={{fontSize: "2rem", color: colors.secondary}}></i>
                            <Card.Body style={{color: colors.secondary, whiteSpace: "nowrap"}}>
                                <Card.Title className="nanum-gothic-extrabold">입고</Card.Title>
                                <Card.Text className="nanum-gothic-extrabold">{commaNumber(inoutCnt.inStoreCnt)}건</Card.Text>
                            </Card.Body>
                        </Card>
                    </div>

                    <div className="col-12 col-md-6">
                        <Card className={cardStyle} style={{
                            backgroundImage: `url(${bg5})`,
                            backgroundSize: 'cover',
                            backgroundRepeat: 'no-repeat',
                            backgroundPosition: 'bottom',
                            boxShadow: '5px 5px 10px -10px'
                        }}
                              onClick={goUseStock}>
                            <i className="bi bi-upload"
                               style={{fontSize: "2rem", color: colors.red}}></i>
                            <Card.Body style={{color: colors.red, whiteSpace: "nowrap"}}>
                                <Card.Title className="nanum-gothic-extrabold">
                                    출고
                                </Card.Title>
                                <Card.Text className="nanum-gothic-extrabold">{commaNumber(inoutCnt.usageCnt)}건</Card.Text>
                            </Card.Body>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default InOutViewComponent;
