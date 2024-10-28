import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function ScrollToTop() {
    const { pathname } = useLocation();

    // App 내 contentwrapper의 overflow:hidden 요소 때문에 스크롤 명시 필요
    useEffect(() => {
        const contentContainer = document.querySelector('.content-container');
        if (contentContainer) {
            contentContainer.scrollTo(0, 0);
        } else {
            window.scrollTo(0, 0);
        }
    }, [pathname]); // 경로가 변경될 때마다 실행

    return null;
}

export default ScrollToTop;
