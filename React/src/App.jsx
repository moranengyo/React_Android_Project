import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import ScrollToTop from "./components/ScrollToTop.jsx";
import Statistics from "./pages/Statistics.jsx";
import Search from "./pages/Search.jsx";
import Alarm from "./pages/Alarm.jsx";
import History from "./pages/History.jsx";
import Footer from "./components/Footer.jsx";
import './App.css';
import React, {useEffect, useState} from "react";
import Sidebar from "./components/Sidebar.jsx";
import Login from "./pages/Login.jsx";
import InStock from "./pages/InStock.jsx";
import UseStockChk from "./pages/UseStockChk.jsx";
import ItemReq from "./pages/ItemReq.jsx";
import NewItemReq from "./pages/NewItemReq.jsx";
import VendorManage from "./pages/VendorManage.jsx";
import ReqDetail from "./pages/ReqDetail.jsx";
import EntireUser from "./pages/EntireUser.jsx";
import NewUserReq from "./pages/NewUserReq.jsx";
import SendVendorMail from "./pages/SendVendorMail.jsx";
import ProtectedRoute from "./service/ProtectedRoute.jsx";

const PublicLayout = ({children}) => {
    return (
        <div className="public-layout">
            {children}
        </div>
    );
};

function App() {
    const [role, setRole] = useState(null);

    useEffect(() => {
        const storedRole = localStorage.getItem("ROLE");
        if (storedRole) {
            setRole(storedRole === "ROLE_MANAGER");
        }
    }, []);

    const PrivateLayout = ({children}) => {
        return (
            <div className="app-container">
                <Sidebar role={role}/>
                <div className="content-wrapper">
                    <div className="content-container">
                        {children}
                        <Footer/>
                    </div>
                </div>
            </div>
        );
    };

    return (
        // 거래처 관리 임시 더미데이터 호출
            <Router>
                <ScrollToTop/>
                <Routes>
                    <Route path="/login" element={
                        <PublicLayout> <Login setRole={setRole}/> </PublicLayout>
                    }
                    />
                    {/* props로 role 전달받아 role별 ui 렌더링 */}
                    <Route path="/" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><Statistics role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/search" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><Search role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/alarm" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><Alarm role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/history" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><History status={'전체'}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/history/requested" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><History status={'요청'}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/history/approved" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><History  status={'승인'}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/history/rejected" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><History  status={'반려'}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/history/detail/:id" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><ReqDetail role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/instock" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><InStock role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/usestockchk" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><UseStockChk role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route
                        path="/req" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><ItemReq role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/req/new" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><NewItemReq role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/vendor" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><VendorManage role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/user" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><EntireUser role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/user/req" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><NewUserReq role={role}/></PrivateLayout>}/>
                    }
                    />
                    <Route path="/vendor/mail" element={
                        <ProtectedRoute element={<PrivateLayout role={role}><SendVendorMail role={role}/></PrivateLayout>}/>
                    }
                    />
                </Routes>
            </Router>
    );
}

export default App;