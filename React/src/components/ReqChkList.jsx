import React from "react";
import {useNavigate} from "react-router-dom";

function ReqChkList() {
  const navigate = useNavigate();
  const handleClick = (path) => {
    navigate(path);
  };

  return (
      <>
        <div className={'d-flex justify-content-between'}>
          <h3 className={'nanum-gothic-bold'}>결재 내역 조회</h3>
        </div>
        <div className={'row mx-3'}>
          <div className={'box-primary col-sm mx-1 border border-2 rounded-3 p-3 text-center'}
                onClick={() => handleClick('/history/requested')}>
            <h5 className={'nanum-gothic-extrabold'}>요청</h5>
            <h5 className={'mt-5 nanum-gothic-extrabold'}>n건</h5>
          </div>
          <div className={'box col-sm mx-1 border border-2 rounded-3 p-3 text-center'}
                onClick={() => handleClick('/history/approved')}>
            <h5 className={'nanum-gothic-extrabold'}>승인</h5>
            <h5 className={'mt-5 nanum-gothic-extrabold'}>n건</h5>
          </div>
          <div className={'box-primary col-sm mx-1 border border-2 rounded-3 p-3 text-center'}
                onClick={() => handleClick('/history/rejected')}>
            <h5 className={'nanum-gothic-extrabold'}>반려</h5>
            <h5 className={'mt-5 nanum-gothic-extrabold'}>n건</h5>
          </div>
        </div>
        <button type={"button"} className={'btn btn-sm btn-outline-primary mt-3 float-end'}
                onClick={() => handleClick('/history')}>Detail</button>
      </>
  );
}

export default ReqChkList;