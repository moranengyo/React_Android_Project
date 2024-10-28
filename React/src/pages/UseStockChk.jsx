import SearchTop from "../components/SearchTop.jsx";
import SetDateRange from "../components/SetDateRange.jsx";
import {useEffect, useState} from "react";
import {getUsageListByDateAndSearch} from "../service/axiosFunc.js";
import {useLocation} from "react-router-dom";
import PageNav from "../components/PageNav.jsx";
import {commaNumber} from "../service/util.js";
import {formatDateString} from "../store/Unit.js";

function UseStockChk() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const [searchDur, setSearchDur] = useState("D");
    const [searchVal, setSearchVal] = useState(queryParams.get('query') || "");
    const [useStock, setUseStock] = useState();
    const [totalPay, setTotalPay] =useState(0);
    const [totalUsage, setTotalUsage] = useState(0);
    const [totalUsageNum, setTotalUsageNum] = useState(0);

    const [currentPage, setCurrentPage] = useState(1)


    useEffect(() => {
        loadUseStock(0);
    }, []);

    useEffect(() => {
        setCurrentPage(1);
        loadUseStock(0);
    }, [searchDur, searchVal]);


    useEffect(() => {
        loadUseStock(currentPage - 1);
    }, [currentPage]);

    const loadUseStock = (pageNum) => {
        getUsageListByDateAndSearch((data) => {
            setUseStock(data.searchResult);
            setTotalPay(data.searchResultTotalPay);
            setTotalUsage(data.searchResultTotalUsage);
            setTotalUsageNum(data.searchResultTotalUsageNum);
        }, pageNum, searchDur, searchVal);
    }

    return (
        <main className={`sub-content-container`}>
            <h1>사용 현황 조회</h1>
            <div className={'col-sm-12'}>
                <SetDateRange setDate={setSearchDur}/>
                <form onSubmit={(e) => e.preventDefault()} className={'mx-5 d-flex justify-content-center'}>
                    <SearchTop
                        placeholder={'비품 / 직원명으로 검색'}
                        searchPath={'/useStockChk'}
                        setSearchVal={setSearchVal}
                    />
                </form>
            </div>
            <div className={'m-5 justify-content-center table-container'}>
                <h4 className={'my-3 nanum-gothic-bold float-start'}>총 <span
                    className={'nanum-gothic-extrabold'}>{commaNumber(totalUsage)}</span>건</h4>
                <table className={'table'}>
                    <thead>
                    <tr>
                        <th colSpan={3} className={'text-start bg-primary-subtle border border-primary'}>총 사용량 : <span
                            className={'nanum-gothic-extrabold'}>{commaNumber(totalUsageNum)}</span>개
                        </th>
                        <th colSpan={2} className={'text-start bg-primary-subtle border border-primary'}>총 사용금액 : <span
                            className={'nanum-gothic-extrabold'}>{commaNumber(totalPay)}</span>원
                        </th>
                    </tr>
                    <tr>
                        <th>직원명</th>
                        <th width={'30%'}>비품명</th>
                        <th>사용량</th>
                        <th>사용금액</th>
                        <th width={'30%'}>사용시간</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        (useStock != null && useStock.length > 0) ? (
                                useStock.map(req => (
                                        <tr key={req.id}>
                                            <td>{req.user.userName}</td>
                                            <td>{req.item.name}</td>
                                            <td>{req.usageNum}</td>
                                            <td>{commaNumber(req.usageNum * req.item.price)}</td>
                                            <td>{formatDateString(req.usageTime)}</td>
                                        </tr>
                                    )
                                ))
                            : (
                                <tr><td colSpan={5}>사용 기록이 없습니다</td></tr>
                            )
                    }
                    {searchVal && (
                        <tr>
                            <td colSpan={5}>{searchVal}에 대한 검색결과</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
            <div className={'pagination-container'}>
            <PageNav
                totalPageNum={Math.ceil(totalUsage / 10.0)}
                setCurrentPage={setCurrentPage}
                triggerVal={searchDur || searchVal}
            />
            </div>
        </main>
    );
}

export default UseStockChk;