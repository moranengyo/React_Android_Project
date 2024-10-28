import {Link, useLocation, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import logo from "../assets/logo.png";
import smlogo from "../assets/onlyLogo2.png"
import SidebarSearchTop from "./SidebarSearchTop.jsx";
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Sidebar.css'; // CSS 파일을 가져옴

function Sidebar({ role }) {
    const [isStockSubmenuOpen, setIsStockSubmenuOpen] = useState(false);
    const [isHistorySubmenuOpen, setIsHistorySubmenuOpen] = useState(false);
    const [isUserSubmenuOpen, setIsUserSubmenuOpen] = useState(false);
    const [isSmallScreen, setIsSmallScreen] = useState(window.innerWidth < 768);

    const location = useLocation();
    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("ACCESS_TOKEN");
        localStorage.removeItem("USER_ID");
    };

    const handleLogout = () => {
        if (window.confirm("로그아웃 하시겠습니까?")) {
            logout();
            navigate('/login');
        }
    };

    const toggleStockSubmenu = (event) => {
        event.preventDefault();
        setIsStockSubmenuOpen(!isStockSubmenuOpen);
    };

    const toggleHistorySubmenu = (event) => {
        event.preventDefault();
        setIsHistorySubmenuOpen(!isHistorySubmenuOpen);
    };

    const toggleUserSubmenu = (event) => {
        event.preventDefault();
        setIsUserSubmenuOpen(!isUserSubmenuOpen);
    };

    const getActiveClass = (path) => {
        return location.pathname === path ? 'active' : '';
    };


    const updateScreenSize = () => {
      setIsSmallScreen(window.innerWidth < 768);
    }

    useEffect(() => {
      window.addEventListener("resize", updateScreenSize);
      return () => window.removeEventListener("resize", updateScreenSize);
    }, []);


    return (
        <aside className={`sidebar ${isSmallScreen ? 'small' : ''}`}>
            <nav>
                <Link className="navbar-brand" to="/">
                    <img
                        style={{width: isSmallScreen ? "30px" : "200px"}}
                        src={isSmallScreen ? smlogo : logo}
                        alt="YES!M 로고"/>
                </Link>

                <ul className={`navbar-nav`}>
                    <SidebarSearchTop/>
                    <hr className="sidebar-divider"/>
                    <Link className={`nav-item ${getActiveClass('/')}`} to="/">
                        <i className="bi bi-columns me-3"></i>대시보드
                    </Link>
                    <Link className={`nav-item ${getActiveClass('/search')}`} to="/search">
                        <i className="bi bi-search me-3"></i>비품조회
                    </Link>
                    <div className="nav-item d-flex align-items-center justify-content-between"
                         onClick={toggleStockSubmenu} style={{cursor: 'pointer'}}>
                        <div className="d-flex align-items-center">
                            <i className="bi bi-box-seam me-3"></i>
                            <span>입출고현황</span>
                        </div>
                        <span>
                            <i className={`bi ${isStockSubmenuOpen ? 'bi-caret-down-fill' : 'bi-caret-right-fill'}`}></i>
                        </span>
                    </div>
                    <ul className={`navbar-nav submenu align-items-start mx-3 ${isStockSubmenuOpen ? 'open' : ''}`}>
                        <Link className={`nav-item ${getActiveClass('/instock')}`}
                              to="/instock">
                            입고 현황 조회
                        </Link>
                        <Link className={`nav-item ${getActiveClass('/usestockchk')}`}
                              to="/usestockchk">사용 현황 조회
                        </Link>
                    </ul>

                    {/* role 별 메뉴 항목 변경 */}
                    {role ? (   // 구매관리자(true)
                        <>
                            <Link className={`nav-item ${getActiveClass('/alarm')}`} to="/alarm">
                                <i className="bi bi-app-indicator me-3"></i>적정수량 미만 비품
                            </Link>
                            <Link className={`nav-item ${getActiveClass('/vendor')}`} to="/vendor">
                                <i className="bi bi-buildings me-3"></i>거래처 관리
                            </Link>
                            <Link className={`nav-item ${getActiveClass('/req/new')}`} to="/req/new">
                                <i className="bi bi-box2-heart me-3"></i>신규비품 등록 요청
                            </Link>
                        </>
                    ) : (   // 구매부장(false)
                        <>
                            <div className="nav-item d-flex align-items-center justify-content-between"
                                 onClick={toggleUserSubmenu} style={{cursor: 'pointer'}}>
                                <div className="d-flex align-items-center">
                                    <i className="bi bi-people me-3"></i>
                                    <span>직원 관리</span>
                                </div>
                                <span>
                            <i className={`bi ${isUserSubmenuOpen ? 'bi-caret-down-fill' : 'bi-caret-right-fill'}`}></i>
                        </span>
                            </div>
                            <ul className={`navbar-nav submenu align-items-start mx-3 ${isUserSubmenuOpen ? 'open' : ''}`}>
                                <Link className={`nav-item ${getActiveClass('/user/req')}`}
                                      to="/user/req">
                                    신규 직원 등록 요청
                                </Link>
                                <Link className={`nav-item ${getActiveClass('/user')}`}
                                      to="/user">전체 직원 조회
                                </Link>
                            </ul>
                        </>
                    )}

                    <div className="nav-item d-flex align-items-center justify-content-between"
                         onClick={toggleHistorySubmenu} style={{cursor: 'pointer'}}>
                        <div className="d-flex align-items-center">
                            <i className="bi bi-clock-history me-3"></i>
                            <span>결재 내역 목록</span>
                        </div>
                        <span>
                            <i className={`bi ${isHistorySubmenuOpen ? 'bi-caret-down-fill' : 'bi-caret-right-fill'}`}></i>
                        </span>
                    </div>
                    <ul className={`navbar-nav submenu align-items-start mx-3 ${isHistorySubmenuOpen ? 'open' : ''}`}>
                        <Link className={`nav-item ${getActiveClass('/history')}`} to="/history">
                            전체
                        </Link>
                        <Link className={`nav-item ${getActiveClass('/history/requested')}`} to="/history/requested">
                            요청
                        </Link>
                        <Link className={`nav-item ${getActiveClass('/history/approved')}`} to="/history/approved">
                            승인
                        </Link>
                        <Link className={`nav-item ${getActiveClass('/history/rejected')}`} to="/history/rejected">
                            반려
                        </Link>
                    </ul>
                </ul>
            </nav>
            <div style={{flexGrow: 1}}></div>
            <nav className={`navbar-nav mb-3`}>
            <Link className={`nav-item ${getActiveClass('/login')}`}
                  to="/login"
                  style={{margin: '0'}}
                  onClick={(e) => {
                      e.preventDefault();
                      handleLogout();
                  }}>
                <i className="bi bi-box-arrow-right me-3"></i>로그아웃
            </Link>
            </nav>
        </aside>
    )
        ;
}

export default Sidebar;