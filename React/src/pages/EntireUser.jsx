import SearchTop from "../components/SearchTop.jsx";
import {useEffect, useState} from "react";
import {getApprovedUsers, changeUserRole, deleteUser} from "../service/axiosFunc.js";
import PageNav from "../components/PageNav.jsx";

function EntireUser() {
  const [users, setUsers] = useState([]);
  const [totalCnt, setTotalCnt] = useState(0);
  const queryParams = new URLSearchParams(location.search);
  const [searchVal, setSearchVal] = useState(queryParams.get('query') || "");
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    loadApprovedUsers(0);
  }, []);

  useEffect(() => {
    setCurrentPage(1);
    loadApprovedUsers(0);
  }, [searchVal]);

  useEffect(() => {
    loadApprovedUsers(currentPage - 1);
  }, [currentPage]);

  const loadApprovedUsers = (pageNum) =>{
    getApprovedUsers((data) => {
      setUsers(data.approvedUserList);
      setTotalCnt(data.totalApprovedUserCnt);
    }, pageNum, searchVal)
  }

  const handleRoleChange = (id, newRole) => {
    changeUserRole(() => {
      setUsers((prevUsers) =>
          prevUsers.map(user => user.seq === id ? { ...user, role: newRole } : user
          )
      );
      loadApprovedUsers()
    }, id, newRole);
  };

  const handleDelete = (id) => {
    deleteUser(() => {
      loadApprovedUsers(0);
    }, id);
  };




  return (
      <main className={`sub-content-container`}>
        <h1>전체 직원 조회</h1>
        <div className={'col-sm-12'}>
          <form onSubmit={(e) => e.preventDefault()} className={'m-5 d-flex justify-content-center'}>
            <SearchTop
                placeholder={'직원명 검색'}
                searchPath={'/user'}
                setSearchVal={setSearchVal}
            />
          </form>
        </div>
        <div className={'m-5 justify-content-center'}>
          <div className={'align-items-baseline justify-content-between'}>
            <h4 className={'nanum-gothic-bold float-start'}>
              총 <span className={'nanum-gothic-extrabold'}>{totalCnt}</span>명
            </h4>
          </div>
          <table className={'table'}>
            <thead>
            <tr>
              <th>이름</th>
              <th>아이디</th>
              <th>이메일</th>
              <th>권한</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            {users.length > 0 ? (
                users.map((user) => (
                    <tr key={user.seq}>
                      <td>{user.userName}</td>
                      <td>{user.userId}</td>
                      <td>{user.email}</td>
                      <td>
                        <select
                            value={user.role}
                            onChange={(e) => handleRoleChange(user.seq, e.target.value)}
                        >
                          <option value="ROLE_USER">직원</option>
                          <option value="ROLE_MANAGER">관리자</option>
                          <option value="ROLE_SENIOR_MANAGER">부장</option>
                        </select>
                      </td>
                      <td width='15%'>
                        <button className={'btn btn-sm btn-danger nanum-gothic-bold'} onClick={() => handleDelete(user.userId)}>삭제</button>
                      </td>
                    </tr>
                ))
            ) : (
                <tr>
                  <td colSpan={5} className={'text-center'}>등록된 직원이 없습니다.</td>
                </tr>
            )}
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

export default EntireUser;