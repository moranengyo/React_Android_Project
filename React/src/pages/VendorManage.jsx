import React, {useEffect, useState} from "react";
import {addNewCompany, deleteCompany, getCompanyList, updateCompany} from "../service/axiosFunc.js";
import PageNav from "../components/PageNav.jsx";

function VendorManage() {
  const [isAdding, setIsAdding] = useState(false);
  const [newVendor, setNewVendor] = useState({ code: "", name: "", email: ""});
  const [editingIndex, setEditingIndex] = useState(null);
  const [editedVendor, setEditedVendor] = useState({code: "", name:"", email:""});


  const [totalCnt, setTotalCnt] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);

  const [companyList, setCompanyList] = useState();

  const handleAddVendor = () => {
    if (newVendor.name && newVendor.email) {
      addNewCompany(() => {
        loadCompanyList(currentPage - 1);
        setIsAdding(false);
      },{... newVendor});
    }
  };

  const handleSaveEdit = (id, company) => {
    updateCompany(() =>{
      loadCompanyList(currentPage - 1);
      setEditingIndex(null)
    }, id, {... company})
  };

  const handleDeleteVendor = (id) =>{
    deleteCompany(() => {
      loadCompanyList(currentPage - 1);
    }, id);
  }


  const loadCompanyList = pageNum =>{
    getCompanyList((data) =>{
      setCompanyList(data.companyList);
      setTotalCnt(data.totalCompanyCount);
    }, pageNum);
  }

  useEffect(() => {
    loadCompanyList(0);
  }, []);

  useEffect(() =>{
    loadCompanyList(currentPage - 1);
  }, [currentPage]);


  return (
    <main className={`sub-content-container`}>
      <h1>거래처 관리</h1>
      <div className={'ms-5'} style={{height:'650px'}}>
        <button
            className={ `my-3 btn btn-blue float-end nanum-gothic-bold ${
                        isAdding ? "btn-secondary" : "btn-blue"}
                        ` }
            onClick={() => setIsAdding(!isAdding)}>
          {isAdding ? "취소" : "거래처 추가"}
        </button>

        <table className={'table'} style={{ maxHeight: '400px', overflowY: 'hidden' }}>
          <thead>
            <tr>
              <th>거래처코드</th>
              <th>거래처명</th>
              <th>이메일</th>
              <th width={'30%'}></th>
            </tr>
          </thead>
          <tbody>
            {isAdding && (
                <tr>
                  <td style={{textAlign: "center", verticalAlign: "middle"}}>
                    <input
                        type="text"
                        className={'form-control'}
                        placeholder="거래처코드"
                        style={{width: '60%', margin: '0 auto'}}
                        value={newVendor.code}
                        onChange={(e) =>
                            setNewVendor({...newVendor, code: e.target.value})
                        }
                    />
                  </td>
                  <td style={{textAlign: "center", verticalAlign: "middle"}}>
                    <input
                        type="text"
                        className={'form-control'}
                        placeholder="거래처명"
                        style={{width: '60%', margin: '0 auto'}}
                        value={newVendor.name}
                        onChange={(e) =>
                            setNewVendor({...newVendor, name: e.target.value})
                        }
                    />
                  </td>
                  <td style={{textAlign: "center", verticalAlign: "middle"}}>
                    <input
                        type="email"
                        className={'form-control'}
                        placeholder="이메일"
                        value={newVendor.email}
                        style={{width: '60%', margin: '0 auto'}}
                        onChange={(e) =>
                            setNewVendor({...newVendor, email: e.target.value})
                        }
                    />
                  </td>
                  <td>
                    <button
                        className={'btn btn-sm btn-success me-2 nanum-gothic-regular'}
                        onClick={handleAddVendor}
                    >
                      저장
                    </button>
                  </td>
                </tr>
              )}

            {(companyList != null && companyList.length > 0) ? (
                companyList.map(data => (
                    <tr key={data.id}>
                      {editingIndex === data.id ? (
                          <>
                            <td>
                              <input
                                  type="text"
                                  className={'form-control'}
                                  value={editedVendor.code}
                                  onChange={(e) =>
                                      setEditedVendor({ ...editedVendor, code: e.target.value })
                                  }
                                  />
                            </td>
                            <td>
                              <input
                                  type="text"
                                  className={'form-control'}
                                  value={editedVendor.name}
                                  onChange={(e) =>
                                      setEditedVendor({ ...editedVendor, name: e.target.value })
                      }
                    />
                  </td>
                  <td>
                    <input
                        type="email"
                        className={'form-control'}
                        value={editedVendor.email}
                        onChange={(e) =>
                        setEditedVendor({ ...editedVendor, email: e.target.value })
                      }
                    />
                  </td>
                  <td width='30%'>
                    <button
                        className={'btn btn-sm btn-success me-2 nanum-gothic-regular'}
                        onClick={() => handleSaveEdit(data.id, {... editedVendor})}
                    >
                      저장
                    </button>
                    <button
                        className={'btn btn-sm btn-secondary nanum-gothic-regular'}
                        onClick={() => setEditingIndex(null)}
                    >
                      취소
                    </button>
                  </td>
                </>
              ) : (
              <>
                <td>{data.code}</td>
                <td>{data.name}</td>
                <td>{data.email}</td>
                <td width='30%'>
                  <button
                      className={'btn btn-sm btn-primary me-2 nanum-gothic-regular'}
                      onClick={() => {
                        setEditingIndex(data.id);
                        setEditedVendor({code: data.code, name: data.name, email : data.email});
                      }}
                  >
                    수정
                  </button>
                <button
                    className={'btn btn-sm btn-danger nanum-gothic-regular'}
                    onClick={() => handleDeleteVendor(data.id)}
                >
                  삭제
                </button>
              </td>
            </>
          )}
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan={3}>등록된 거래처가 없습니다.</td>
          </tr>
        )}
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

export default VendorManage;