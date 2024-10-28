import {useNavigate, useRouteLoaderData} from "react-router-dom";
import {useEffect, useState} from "react";
import {getPurchaseList, getUserTotalReq} from "../service/axiosFunc.js";
import PageNav from "./PageNav.jsx";
import {formatDateString} from "../store/Unit.js";

function ReqList ({status}) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  const [totalCnt, setTotalCount] = useState(0);
  const [purchaseList, setPurchaseList] = useState([]);

  const handleClick = (req) => {
    navigate(`/history/detail/${req.id}`);
  };

  const getStatusEnum = (status) => {
    switch (status) {
      case '승인':
        return 'APPROVE';
      case '반려':
        return 'CANCEL';
      case '요청':
        return 'WAIT';
      case '입고완료':
        return 'IN_STOCK';
      default:
        return '';
    }
  }



  const loadPurchaseData = (pageNum) => {
    getPurchaseList((data) => {
      console.log(data.purchaseList);
      setPurchaseList(data.purchaseList);
      setTotalCount(data.totalPurchaseCnt);
    }, pageNum, getStatusEnum(status));
  };

  useEffect(() =>{
    console.log(status);
    loadPurchaseData(0);
  }, [status]);

  useEffect(() => {
    loadPurchaseData(currentPage - 1);
  }, [currentPage]);

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVE':
        return 'bg-success';
      case 'CANCEL':
        return 'bg-danger';
      case 'WAIT':
        return 'bg-white border border-primary text-dark'
      case 'IN_STOCK':
        return 'bg-info text-dark'
      default:
        return 'bg-secondary';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'APPROVE':
        return '승인';
      case 'CANCEL':
        return '반려';
      case 'WAIT':
        return '요청';
      case 'IN_STOCK':
        return '입고완료';
      default:
        return '';
    }
  };

  const getTypeColor = (type) => {
    switch (type) {
      case 'Y':
        return 'bg-primary';
      case 'N' :
        return 'bg-warning text-dark';
      default:
        return '';
    }
  }

  return (
      <div className={'ms-5'}>
        <div style={{height:'700px'}}>
        {(purchaseList != null && purchaseList.length > 0) ? (
            purchaseList.map((req) => (
                <div key={req.id} className={'m-3 border border-1 rounded-3 p-3 req-card'}
                     style={{height: '150px', width: '700px'}}
                     onClick={() => handleClick(req)}>
                  <div className={'d-flex border-bottom align-items-center'}>
                    <div className={'d-grid mb-1 me-2'}>
                      <span className={`badge rounded-fill mb-2 nanum-gothic-extrabold ${getStatusColor(req.approvedStatus)}`}>{getStatusText(req.approvedStatus)}</span>
                      <span className={`badge rounded-fill nanum-gothic-extrabold ${getTypeColor(req.newYn)}`}>{req.newYn === 'Y' ? '신규등록' : '추가구매'}</span>
                    </div>
                    <h3 className={'nanum-gothic-bold'}>{req.title}</h3>
                  </div>
                  <div className={'ms-3'}>
                    <span className={'d-block text-start fs-5 nanum-gothic-bold my-2'} style={{color: 'rgba(15,76,128, .8)'}}>{req.item.name}</span>
                    <span className={'d-block text-start fs-6 nanum-gothic-regular'}>요청시간 : {formatDateString(req.reqTime)}</span>
                  </div>
                </div>
            )

            )
        ) : (
            <div className={'text-center nanum-gothic-extrabold'}>등록된 요청이 없습니다.</div>
        )}
        </div>
        <PageNav
          totalPageNum={Math.ceil(totalCnt / 4.0)}
          setCurrentPage={setCurrentPage}
          triggerVal={status}
          />
      </div>
  );
}

export default ReqList;