import React, { useState } from "react";
import { format, startOfYear } from 'date-fns';
import './SetDateRange.css'

function SetDateRange({ setDate }) {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [viewMode, setViewMode] = useState('day'); // day, month, year

  const handleRightClick = () => {
    switch (viewMode) {
      case 'day':
        setViewMode('month');
        setDate("M");
        break;
      case 'month':
        setViewMode('year');
        setDate("Y");
        break;
      case 'year':
        setViewMode('day');
        setDate("D");
        break;
      default:
        break;
    }
  };

  const handleLeftClick = () => {
    switch (viewMode) {
      case 'day':
        setViewMode('year');
        setDate("Y");
        break;
      case 'month':
        setViewMode('day');
        setDate("D");
        break;
      case 'year':
        setViewMode('month');
        setDate("M");
        break;
      default:
        break;
    }
  };

  const getDisplayDate = () => {
    switch (viewMode) {
      case 'day':
        return `오늘`; // 일 기준
      case 'month':
        return `이번달`; // 월 기준
      case 'year':
        return `이번해`; // 연 기준
      default:
        return '';
    }
  };

  const getDateRange = () => {
    switch (viewMode) {
      case 'day':
        return `${format(currentDate, 'yyyy-MM-dd')}`;
      case 'month':
        return `${format(currentDate, 'yyyy-MM')}`;
      case 'year':
        return `${format(startOfYear(currentDate), 'yyyy-MM')} ~ ${format(currentDate, 'yyyy-MM')}`; // 연 범위
      default:
        return '';
    }
  };

  return (
      <div className="date-selector-container">
        <div className="date-navigation">
          <button className="arrow-btn" onClick={handleLeftClick}>
            &#9664;
          </button>
          <div className="date-display">
            <p className="nanum-gothic-extrabold mb-0">{getDisplayDate()}</p>
          </div>
          <button className="arrow-btn" onClick={handleRightClick}>
            &#9654;
          </button>
        </div>
            <p className="nanum-gothic-bold date-range mb-0">{getDateRange()}</p>
      </div>
  );
}

export default SetDateRange;
