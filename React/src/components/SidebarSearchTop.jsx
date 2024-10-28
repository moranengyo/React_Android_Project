import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import './SidebarSearchTop.css';

const SidebarSearchTop = () => {
    const [searchVal, setSearchVal] = useState("");
    const navigate = useNavigate();

    const handleSearch = (e) => {
        e.preventDefault();
        if (searchVal) {
            navigate(`/search?query=${encodeURIComponent(searchVal)}`);
        } else {
            navigate('/search');
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSearch(e);
        }
    };

    return (
        <div className="position-relative search-container">
            <i className="bi bi-search search-icon"></i>
            <input
                type="text"
                className="form-control search-input"
                placeholder='비품 검색'
                value={searchVal}
                onChange={(e) => setSearchVal(e.target.value)}
                onFocus={(e) => e.target.placeholder = ''}  // 포커스 시 placeholder 제거
                onBlur={(e) => e.target.placeholder = '비품 검색'}  // 포커스 해제 시 placeholder 복원
                onKeyDown={handleKeyPress}
            />
        </div>
    );
}

export default SidebarSearchTop;
