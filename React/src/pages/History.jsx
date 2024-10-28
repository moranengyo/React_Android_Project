import ReqList from "../components/ReqList.jsx";
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";

function History({ status }){
  const location = useLocation();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('전체');

  useEffect(() => {
    const path = location.pathname.split("/").pop();
    if (path === "requested") {
      setActiveTab('요청');
    } else if (path === "approved") {
      setActiveTab('승인');
    } else if (path === "rejected") {
      setActiveTab('반려');
    } else {
      setActiveTab('전체');
    }
  }, [location.pathname, status]);

  const handleTabClick = (status) => {
    if (activeTab !== status) {
      setActiveTab(status);
      if (status === "요청") {
        navigate("/history/requested");
      } else if (status === "승인") {
        navigate("/history/approved");
      } else if (status === "반려") {
        navigate("/history/rejected");
      } else {
        navigate("/history");
      }
    }
  };

    return (
        <main className={`sub-content-container`}>
          <div className={'row'}>
            <div className={'col-12'}>
              <h1>결재 내역 목록</h1>
            </div>
          </div>
          <div className={'row justify-content-center'}>
            <nav className={'mx-5 my-4'}>
              <div className={'nav nav-tabs justify-content-center'} role={"tablist"}>
                <button
                    className={`fs-5 nanum-gothic-extrabold nav-link ${activeTab === "전체" ? "active" : ""}`}
                    onClick={() => handleTabClick("전체")}>
                  전체
                </button>
                <button
                    className={`fs-5 nanum-gothic-extrabold nav-link ${activeTab === "요청" ? "active" : ""}`}
                    onClick={() => handleTabClick("요청")}>
                  요청
                </button>
                <button
                    className={`fs-5 nanum-gothic-extrabold nav-link ${activeTab === "승인" ? "active" : ""}`}
                    onClick={() => handleTabClick("승인")}>
                  승인
                </button>
                <button
                    className={`fs-5 nanum-gothic-extrabold nav-link ${activeTab === "반려" ? "active" : ""}`}
                    onClick={() => handleTabClick("반려")}>
                  반려
                </button>
              </div>
            </nav>
          </div>
          <div className={'d-flex mx-5 justify-content-center align-content-center'}>
            <ReqList status={status}/>
          </div>
        </main>
    );
}

export default History;