import React from 'react';
import {Navigate, Route} from 'react-router-dom';
import Login from "../pages/Login.jsx";

const ProtectedRoute = ({ element }) => {
    const token = localStorage.getItem('ACCESS_TOKEN');

    if (!token) {
        // alert("로그인 필요")
        return <Navigate to="/login" />;
    }

        // alert("로그인 됨")
    return element;
};

export default ProtectedRoute;
