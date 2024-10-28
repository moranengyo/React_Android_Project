import './Modal.css';
import {useEffect, useState} from "react";
import SearchTop from "./SearchTop.jsx";
import {getSearchCompanyList} from "../service/axiosFunc.js";


function SelectVendorModal({ onClose, onSelect }) {
  const [vendors, setVendors] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    getSearchCompanyList((dataList) => {
      setVendors(dataList || []);
    },searchTerm);
  }, [searchTerm]);


  return (
    <div className={'modal-overlay'}>
      <div className={'vendor-modal'}>
        <nav className={'navbar rounded-top-3'} style={{backgroundColor: 'rgba(15,76,128,.9)'}}>
          <div className={'container-fluid d-flex justify-content-end'}>
            <button className={'btn btn-sm btn-danger px-2'} onClick={onClose}>X</button>
          </div>
        </nav>
        <div className={'p-2'}>
        <h2 className={'nanum-gothic-extrabold mt-5'}>거래처 선택</h2>
        <form action="" className={'d-flex justify-content-center'}>
          <SearchTop
              placeholder={'거래처명 검색'}
              searchPath={undefined}
              setSearchVal={setSearchTerm}
              onSearch={setSearchTerm}
          />
        </form>
        <div style={{maxHeight: '400px', overflowY: 'auto'}}>
          <table className={'my-5 table'}>
            <thead>
            <tr>
              <th>거래처명</th>
              <th>이메일</th>
              <th>선택</th>
            </tr>
            </thead>
            <tbody>
            {/*{console.log(vendors)}*/}
            {vendors != null && vendors.length > 0 ? (
                vendors.map((vendor) => (
                    <tr key={vendor.id}>
                      <td>{vendor.name}</td>
                      <td>{vendor.email}</td>
                      <td>
                        <button className={'btn btn-sm btn-primary'} onClick={() => onSelect(vendor)}>선택</button>
                      </td>
                    </tr>
                ))
            ) : (
                <tr>
                  <td colSpan={3}>해당하는 거래처가 없습니다.</td>
                </tr>
            )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
    </div>
  );
}

export default SelectVendorModal;