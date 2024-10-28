import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

function PageNav({totalPageNum, setCurrentPage, triggerVal}) {
    const pageCount = 5;
    const [start, setStart] = useState(1);
    var [currentPage, setCurPage] = useState(1);
    const noPrev = start === 1;
    const noNext = start + pageCount -1 >= totalPageNum;


    useEffect(() => {
        console.log(1);
        setCurrentPage(currentPage);
    }, [currentPage]);


    useEffect(() => {
        setCurPage(1)
    }, [triggerVal]);


    useEffect(() => {
        if (currentPage === start + pageCount) {
            setStart(prev => prev + pageCount);
        }
        if (currentPage < start) {
            setStart(prev => prev - pageCount);
        }
    }, [currentPage, pageCount, start]);

    const handlePageClick = (page) => {
        setCurPage(page);
    }


    return (
        <div className={'pagination-wrapper fixed-pagination'}>
            <ul>
                <li className={`move ${noPrev ? 'invisible' : ''}`}>
                    <Link to='#' onClick={() => handlePageClick(start - 1)}>{`<`}</Link>
                </li>
                {[...Array(pageCount)].map((_, i) => (
                    start + i <= totalPageNum && (
                        <li key={i}>
                            <Link
                                className={`page ${currentPage === start + i ? 'active' : ''}`}
                                to='#'
                                onClick={() => handlePageClick(start + i)}
                            >
                                {start + i}
                            </Link>
                        </li>
                    )
                ))}
                <li className={`move ${noNext ? 'invisible' : ''}`}>
                    <Link to="#" onClick={() => handlePageClick(start + pageCount)}>{`>`}</Link>
                </li>
            </ul>
        </div>
    );
}

export default PageNav;