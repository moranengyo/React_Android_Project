import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getAlarmItemList} from "../service/axiosFunc.js";
import PageNav from "./PageNav.jsx";

function MinValItemsView({ onToTalCntChange }) {
  const navigate = useNavigate();

  const toAlarm = () => {
    navigate(`/alarm`);
  }

    const [list, setList] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalCnt, setTotalCnt] = useState(0);


    useEffect(() => {
        getAlarmItemList((data) =>{
            setList(data.itemList);
            setTotalCnt(data.allUnderMinCnt);
            onToTalCntChange(data.allUnderMinCnt);
            console.log(data);
        },0);
    }, []);

    useEffect(() =>{
        getAlarmItemList((data) =>{
            setList(data.itemList);
            setTotalCnt(data.allUnderMinCnt);
            console.log(data);
        },currentPage - 1);
    }, [currentPage])

  const handleNavigate = (data) => {
    // 데이터를 콘솔에 출력
    console.log("Navigating with data:", data);

    // navigate로 페이지 이동
    navigate('/req', { state: { itemData: data } });
  };

  return (
      <div className={'table-container'}>
          <table className={'mt-3 table table-striped'}>
              <thead>
              <tr className={'text-center'}>
                  <th width={'30%'}>비품명</th>
                  <th>창고위치</th>
                  <th>재고수량</th>
              </tr>
              </thead>
              <tbody>
              {
                  (list != null && list.length > 0) ? list.map(data => (
                          <tr key={data.id}>
                              <td>{data.name}</td>
                              <td>{data.container.section}</td>
                              <td>{data.totalNum}</td>
                              <td width='20%'>
                                  <button type={'button'} className={'btn btn-sm btn-primary'}
                                          onClick={() => handleNavigate(data)}>요청서 작성
                                  </button>
                              </td>
                          </tr>
                      )
                  ) : (
                      <tr>
                          <td colSpan={4}> -----</td>
                      </tr>
                  )
              }
              </tbody>
          </table>
          <div>
            <button className={'btn btn-sm btn-outline-primary mt-3 float-end'}
                    onClick={toAlarm}>Detail
            </button>
            <div className="pagination-container">
              <PageNav
                  totalPageNum={Math.ceil(totalCnt / 10.0)}
                  setCurrentPage={setCurrentPage}
              />
            </div>
          </div>
      </div>
  );
}

export default MinValItemsView;