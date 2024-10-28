import SearchTop from "../components/SearchTop.jsx";
import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import {getItemListSearch} from "../service/axiosFunc.js";
import PageNav from "../components/PageNav.jsx";
import {commaNumber} from "../service/util.js";

function Search(){
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const [searchVal, setSearchVal] = useState(queryParams.get('query') || '');

  const [itemList, setItemList] = useState();
  const [currentPage, setCurrentPage] = useState(1);
  const [totalCnt, setTotalCnt] = useState(0);

  useEffect(() => {
    setSearchVal(queryParams.get('query') || '');

    getItemListSearch((data) => {
      setItemList(data.searchResult);
      setTotalCnt(data.itemTotalCnt);
      console.log(data);
    }, currentPage - 1, queryParams.get('query') || '');
  } ,[location.search, currentPage]);


    return (
        <main className={`sub-content-container`}>
          <h1>비품조회</h1>
          <div className={'col-sm-12 align-content-center'}>
            <form onSubmit={(e) => e.preventDefault()} className={'m-5 d-flex justify-content-center'}>
              <SearchTop
                  searchPath='/search'
                  setSearchVal={setSearchVal}
              />
            </form>
          </div>
          <div className={'mt-5 mx-5 justify-content-center table-container'}>
            <table className={'table'}>
              <colgroup>
                <col style={{width: `35%`}}/>
                <col style={{width: `10%`}}/>
                <col style={{width: `20%`}}/>
                <col style={{width: `20%`}}/>
                <col style={{width: `15%`}}/>
              </colgroup>
              <thead>
              <tr>
              <th>비품명</th>
                <th>창고명</th>
                <th>거래처명</th>
                <th>비품단가</th>
                <th>재고수량</th>
              </tr>
              </thead>
              <tbody>
              {
                (itemList != null && itemList.length > 0)
                    ?(
                        itemList.map( data =>(
                          <tr key={data.id}>
                            <td>{data.name}</td>
                            <td>{data.container.section}</td>
                            <td>{data.company.name}</td>
                            <td>{commaNumber(data.price)}</td>
                            <td>{commaNumber(data.totalNum)}</td>
                          </tr>
                        ))
                    )
                    : (
                        <tr>
                          <td colSpan={5}>{searchVal}에 대한 검색 결과</td>
                        </tr>
                    )
              }
              </tbody>
            </table>
          </div>
          <PageNav
            totalPageNum={Math.ceil(totalCnt / 10.0)}
            setCurrentPage={setCurrentPage}
            triggerVal={searchVal}
            />
        </main>
    );
}

export default Search;