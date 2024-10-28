import SearchTop from "../components/SearchTop.jsx";
import SetDateRange from "../components/SetDateRange.jsx";
import {useEffect, useState} from "react";

import {getPurchaseListByDateAndSearch} from "../service/axiosFunc.js"
import {useLocation} from "react-router-dom";
import {commaNumber} from "../service/util.js";
import PageNav from "../components/PageNav.jsx";
import {formatDateString} from "../store/Unit.js";

function InStock() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const searchItem = queryParams.get("query");

    const [currentPage, setCurrentPage] = useState(1);
    const [searchDur, setSearchDur] = useState("");
    const [searchVal, setSearchVal] = useState("");
    const [inStockList, setInStockList] = useState();
    const [inStockCount, setInStockCount] = useState(0);

    useEffect(() => {
        loadInStockList(0);
    }, []);

    useEffect(() =>{
        setCurrentPage(1);
        loadInStockList(0);
    }, [searchDur, searchVal]);

    useEffect(() => {
        loadInStockList(currentPage - 1);
    }, [currentPage]);

    const loadInStockList = (pageNum) => {
        getPurchaseListByDateAndSearch((data) => {
            setInStockList(data.searchResult);
            setInStockCount(data.searchResultCount);
        }, pageNum, searchDur, searchVal);
    }


    return (
        <main className={`sub-content-container`}>
            <h1>입고현황</h1>
            <div className={'col-sm-12 align-content-center'}>
                <SetDateRange setDate={setSearchDur}/>
                <form onSubmit={(e) => e.preventDefault()} className={'mx-5 d-flex justify-content-center'}>
                    <SearchTop
                        setSearchVal={setSearchVal}
                        placeholder={'입고 비품 검색'}
                        searchPath='/instock'
                    />
                </form>
            </div>
            <div className={'m-5 justify-content-center table-container'}>
                <h4 className={'my-3 nanum-gothic-bold float-start'}>총 <span
                    className={'nanum-gothic-extrabold'}>{commaNumber(inStockCount)}</span>개</h4>
                <table className={'table'}>
                    <thead>
                    <tr>
                        <th>No.</th>
                        <th width={'35%'}>비품명</th>
                        <th>창고명</th>
                        <th>거래처명</th>
                        <th>입고수량</th>
                        <th>입고시간</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        (inStockList != null && inStockList.length > 0) ? (
                            inStockList.map(req => (
                                <tr key={req.id}>
                                    <th>{req.id}</th>
                                    <td>{req.item.name}</td>
                                    <td>{req.item.container.section}</td>
                                    <td>{req.item.company.name}</td>
                                    <td>{req.reqNum}</td>
                                    <td>{formatDateString(req.reqTime)}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={6} className={'text-center'}>입고된 비품이 없습니다</td>
                            </tr>
                        )
                    }
                    {searchItem && (
                        <tr>
                            <td colSpan={6}>({searchItem}) 에 대한 검색 결과</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            <PageNav
                totalPageNum={Math.ceil(inStockCount / 10.0)}
                setCurrentPage={setCurrentPage}
                triggerVal={searchDur || searchVal}
                />
        </main>
    );
}
export default InStock;