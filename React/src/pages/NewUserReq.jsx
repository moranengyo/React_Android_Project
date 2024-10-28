import {useEffect, useState} from "react";
import {unconfirmedUserList, userApproved, userRejected} from "../service/axiosFunc.js"

function NewUserReq() {

    const [newUsers, setNewUsers] = useState([]);
    const [pageNumber, setPageNumber] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    const handleApproval = (user) => {
        // approveUser({ ...user, role: 'USER'}); // role 기본 값 'USER'
        userApproved(() => {
            loadUnconfirmedUser();
        }, user.seq);
    };

    const handleRejection = (user) => {
        // rejectUser(userId);

        console.log("reject btn")
        userRejected(() => {
            loadUnconfirmedUser();
        }, user.seq);

    };

    const loadUnconfirmedUser = () => {
        unconfirmedUserList((data) => {
            setNewUsers(data.unconfirmedUserList)
            setTotalPages(data.totalPages);
        }, pageNumber - 1);
    }

    useEffect(() => {
        loadUnconfirmedUser();
    }, [pageNumber]);

    return (
        <main className={`sub-content-container`}>
            <h1>신규 직원 등록 요청</h1>
            <div className={'m-5 justify-content-center'}>
                <div>
                    <h4 className={'nanum-gothic-bold float-start'}>
                        총 <span className={'nanum-gothic-extrabold'}>{newUsers.length}</span>명
                    </h4>
                </div>
                <table className={'table'}>
                    <thead>
                    <tr>
                        <th>이름</th>
                        <th>아이디</th>
                        <th>이메일</th>
                        <th width={'25%'}></th>
                    </tr>
                    </thead>
                    <tbody>
                    {(newUsers != null && newUsers.length > 0) ? (
                        newUsers.map((user) => (
                            <tr key={user.seq}>
                                <td>{user.userName}</td>
                                <td>{user.userId}</td>
                                <td>{user.email}</td>
                                <td className={'d-flex justify-content-center'}>
                                    <button className={'btn btn-sm btn-primary nanum-gothic-bold me-2'}
                                            onClick={() => handleApproval(user)}>승인
                                    </button>
                                    <button className={'btn btn-sm btn-danger nanum-gothic-bold'}
                                            onClick={() => handleRejection(user)}>거절
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={4} className={'text-center'}>등록된 요청이 없습니다.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
                <div className={'d-flex justify-content-between'}>
                    <div>
                        {
                            pageNumber > 1 ? <button onClick={() => setPageNumber(pageNumber - 1)}>{'<'}</button> : ''
                        }
                    </div>
                    <div>
                        {
                            pageNumber < totalPages ? <button onClick={() => setPageNumber(pageNumber + 1)}>{'>'}</button> : ''
                        }
                    </div>
                </div>
            </div>
        </main>
    );
}

export default NewUserReq;