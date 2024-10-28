import React, { useState } from "react";
import { useNavigate } from "react-router-dom";


function SearchTop({ placeholder, searchPath, setSearchVal, onSearch }) {
    const [searchItem, setSearchItem] = useState('');
    const navigate = useNavigate();

    const handleSearch = (e) => {
          e.preventDefault();
          if (onSearch) {
            onSearch(searchItem);
          } else {
            setSearchVal(searchItem);
            navigate(`${searchPath}?query=${encodeURIComponent(searchItem)}`);
          }
        };

    const handleKeyPress = (e) => {
      if (e.key === 'Enter') {
        handleSearch(e);
      }
    }
    return (
          <div className="position-relative search-container">
            <i className="bi bi-search search-top-icon"></i>
            <input
                type="text"
                className="form-control search-top-input nanum-gothic-regular"
                placeholder={placeholder || '비품을 검색하세요'}
                value={searchItem}
                onChange={(e) => setSearchItem(e.target.value)}
                onFocus={(e) => e.target.placeholder = ''}  // 포커스 시 placeholder 제거
                onBlur={(e) => e.target.placeholder = placeholder || `비품을 검색하세요` }  // 포커스 해제 시 placeholder 복원
                onKeyDown={handleKeyPress}
            />
          </div>
    );
}

export default SearchTop;
