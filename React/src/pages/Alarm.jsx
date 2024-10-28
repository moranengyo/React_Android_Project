import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {getAlarmItemList} from "../service/axiosFunc.js";
import PageNav from "../components/PageNav.jsx";

function Alarm(){

  const [list, setList] = useState();
  const [currentPage, setCurrentPage] = useState(1);
  const [totalCnt, setTotalCnt] = useState(0);


  useEffect(() => {
    getAlarmItemList((data) =>{
      setList(data.itemList);
      setTotalCnt(data.allUnderMinCnt);
      // console.log(data);
    },0);
  }, []);

  useEffect(() =>{
    getAlarmItemList((data) =>{
      setList(data.itemList);
      setTotalCnt(data.allUnderMinCnt);
      // console.log(data);
    },currentPage - 1);
  }, [currentPage])

  const handleNavigate = (data) => {

    // navigate로 페이지 이동
    navigate('/req', { state: { itemData: data } });
  };

  const navigate = useNavigate();
    return (
        <main className={`sub-content-container`}>
          <h1>적정수량 미만 비품</h1>
          <div className={'m-5 justify-content-center table-container'}>
            <table className={'table'}>
              <thead>
              <tr>
                <th width={'35%'}>비품명</th>
                <th>창고명</th>
                <th>재고수량</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              {
                (list != null && list.length > 0) ? list.map( data => (
                    <tr key={data.id}>
                      <td>{data.name}</td>
                      <td>{data.container.section}</td>
                      <td>{data.totalNum}</td>
                      <td width='20%'>
                        <button type={'button'} className={'btn btn-sm btn-primary'}
                        onClick={() => handleNavigate(data)}>요청서 작성</button>
                      </td>
                    </tr>
                    )
                ) :(
                    <tr><td colSpan={4}> ----- </td></tr>
                )
              }

              </tbody>
            </table>
          </div>
          <PageNav
            totalPageNum={Math.ceil(totalCnt / 10.0)}
            setCurrentPage={setCurrentPage}
            />
        </main>
    );
}

export default Alarm;