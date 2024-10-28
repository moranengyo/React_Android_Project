import React from 'react';
import './Footer.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const Footer = () => {
    return (
        <div className="footer">
            <div className="footer_section more_site">
                <div className="address">
                    오시는 길: [부산IT교육센터] 부산광역시 부산진구 중앙대로 708 부산파이낸스센터 4F, 5F 부산IT교육센터
                </div>
                <div className="phone">
                    고객지원 문의전화: 1234-8080 (평일 오전 9시 30분~오후 5시 30분)
                    <br />
                </div>
            </div>
            <div className="container-fluid">
                <div className="footer_bottom">
                    <div className="except">
                        ※ 토/일/공휴일/회사 정기휴일 및 특별휴일 제외
                    </div>
                    <div className="year mx-3">
                        ⓒ 2024 YesIam of Korea Co., Ltd. All Rights Reserved.
                    </div>
                    <div className="name">
                        상호명 : 예스아이엠주식회사&nbsp;&nbsp;&nbsp; 단체명 : 파이널 팀프로젝트 1조
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Footer;