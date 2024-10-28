// 신규비품등록, 비품추가구매 요청서 컴포넌트 통일

import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import SelectVendorModal from "./SelectVendorModal.jsx";
import noImage from "../assets/no-image-svgrepo-com.svg";
import "./CommonReq.css";
import {getContainerList, newPurchaseRequest, uploadImageFile} from "../service/axiosFunc.js";
import {parseImageName} from "../store/Unit.js";

function CommonReq({title, reqType, itemData, isReadOnly}) {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [selectedImg, setSelectedImg] = useState(itemData?.thumbNail || null);
    const [selectedItem, setSelectedItem] = useState(itemData?.name || "");
    const [warehouseList, setWarehouseList] = useState([]);
    const [warehouse, setWarehouse] = useState(itemData?.container || null);
    const [selectedVendor, setSelectedVendor] = useState(itemData?.company || null);
    const [selectedQuantity] = useState(itemData?.totalNum || 0);
    const [selectedPrice, setSelectedPrice] = useState(itemData?.price || 0);
    const navigate = useNavigate();

    const [reqTitle, setReqTitle] = useState("");
    const [minCount, setMinCount] = useState("");
    const [reqComment, setReqComment] = useState("");
    const [purchaseQuantity, setPurchaseQuantity] = useState("");
    const [saveImgName, setSaveImgName] = useState("");

    const handleSubmit = async () => {
      if (window.confirm("작성하신 내용으로 상신하시겠습니까?")) {
        const user = JSON.parse(localStorage.getItem("USER"));

        const purchaseData = {
          id: 0,
          title: reqTitle || "",
          reqNum: purchaseQuantity || 0,
          reqTime: "",
          approvedTime: "",
          approvedStatus: "WAIT",
          reqComment: reqComment || "",
          approvalComment: "",
          newYn: reqType === "추가구매" ? "N" : "Y",
          item: {
            id: itemData?.id || -1,
            totalCode: itemData?.totalCode || "",
            name: selectedItem || "",
            minNum: minCount || 0,
            totalNum: selectedQuantity || 0,
            price: selectedPrice || 0,
            thumbNail: saveImgName || "",
            container: warehouse,
            company: selectedVendor || "",
          },
          user: user,
        }

        try {
          await newPurchaseRequest(purchaseData);
          navigate('/history/requested');
        } catch (error) {
          console.error(error);
        }
      }
    };

    const handleDropdownToggle = () => {
        setDropdownOpen(!dropdownOpen);
    };

    const handleSelect = (wareHouse) => {
        setWarehouse(wareHouse);
        setDropdownOpen(false);
    };

    const handleVendorSelect = (vendor) => {

        setSelectedVendor(vendor);
        setShowModal(false);
    };

    const handleImgUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            uploadImageFile(file, (result) => {
                var imgUrl = URL.createObjectURL(file)
                // setSelectedImg(result.thumbnail)
                setSelectedImg(imgUrl)
                setSaveImgName(result.thumbnail)
            })

        }
    };

    useEffect(() => {
        getContainerList((result) => {
            setWarehouseList(result);
            console.log(result)
        })
    }, [])

    return (
        <main>
            <h1>{title}</h1>
            <div className={'d-flex m-5 justify-content-center align-content-center'}>
                <div className={'mt-3 p-3'}>
                    <div className={'req-content mx-5 p-3 border border-1 rounded-1'}>
                        <div className={'row mt-3 p-3 justify-content-center align-items-center'}>
                            <div className={'col-sm-8'}>
                                <label className={'fs-5 nanum-gothic-bold float-start'}>요청서제목</label>
                                <input type="text" className={'form-control'} value={reqTitle}
                                       onChange={(e) => setReqTitle(e.target.value)}/>
                            </div>
                        </div>
                        <section className={'row p-3 justify-content-center align-items-center'}>
                            <div className={'d-grid col-sm-8'}>
                                <h3 className={'fs-5 nanum-gothic-bold text-start'}>비품 정보</h3>
                                <div className={'col-sm border rounded-3'}
                                     style={{backgroundColor: 'rgba(224,232,243,0.48)'}}>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>비품명</label>
                                                <input
                                                    type="text"
                                                    className={'form-control'}
                                                    readOnly={reqType !== "신규등록" || isReadOnly}
                                                    value={selectedItem}
                                                    onChange={(e) => reqType === "신규등록" && setSelectedItem(e.target.value)}
                                                />
                                            </div>
                                        </div>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>비품 이미지</label>
                                                {isReadOnly ? (
                                                    <input type="text" className={'form-control'} readOnly={isReadOnly}
                                                           value={parseImageName(selectedImg)}/>
                                                ) : (
                                                    <input type="file" className={'form-control'} accept="image/*"
                                                           onChange={handleImgUpload}/>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div
                                                className={'img-preview bg-white border border-1 rounded-3 d-flex justify-content-center align-items-center'}
                                                style={{
                                                    backgroundImage: selectedImg  ? `url(${selectedImg})` : `url(${noImage})`,
                                                    backgroundSize: 'contain',
                                                    backgroundPosition: 'center'
                                                }}
                                            >
                                                {!isReadOnly && !selectedImg && (
                                                    <div className={'text-center'}>
                                                        <span className={'nanum-gothic-regular'}></span>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>거래처명</label>
                                                <input
                                                    type="text"
                                                    className={'form-control'}
                                                    readOnly={true}
                                                    value={selectedVendor ? selectedVendor.name : ""}
                                                    onClick={() => !isReadOnly && setShowModal(true)}/>
                                            </div>
                                        </div>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>창고명</label>
                                                <input
                                                    type="text"
                                                    className={'form-control'}
                                                    readOnly={true}
                                                    value={(warehouse == null) ? "" : warehouse.section + " 창고"}
                                                    onClick={handleDropdownToggle}
                                                />
                                                {dropdownOpen && !isReadOnly && (
                                                    <div className={'dropdown'}>
                                                        <div className={'dropdown-menu show'}>
                                                            {warehouseList.map((warehouse) => (
                                                                <button
                                                                    key={warehouse.id}
                                                                    className={'dropdown-item'}
                                                                    onClick={() => handleSelect(warehouse)}>
                                                                    {warehouse.section}
                                                                </button>
                                                            ))}
                                                        </div>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                    <div className={'row m-3'}>
                                        {reqType === '추가구매' && (
                                            <div className={'col-sm'}>
                                                <div className={'d-grid'}>
                                                    <label className={'nanum-gothic-bold text-start'}>재고수량</label>
                                                    <input type="number" className={'form-control'} readOnly={true}
                                                           value={selectedQuantity !== null && selectedQuantity !== undefined ? selectedQuantity : 0}/>
                                                </div>
                                            </div>
                                        )}
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>구매 요청 수량</label>
                                                <input type="number" className={'form-control'} value={purchaseQuantity}
                                                       onChange={(e) => setPurchaseQuantity(e.target.value)}/>
                                            </div>
                                        </div>
                                        {reqType === '신규등록' && (
                                            <div className={'col-sm'}>
                                                <div className={'d-grid'}>
                                                    <label className={'nanum-gothic-bold text-start'}>적정수량</label>
                                                    <input type="number" className={'form-control'} value={minCount}
                                                           onChange={(e) => setMinCount(e.target.value)}/>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                    <div className={'row m-3'}>
                                        <div className={'col-sm'}>
                                            <div className={'d-grid'}>
                                                <label className={'nanum-gothic-bold text-start'}>단가</label>
                                                <div className={'d-flex align-items-baseline'}>
                                                    <input type="number" className={'form-control me-2'}
                                                           readOnly={isReadOnly}
                                                           value={selectedPrice}
                                                           onChange={(e) => setSelectedPrice(e.target.value)}/>
                                                    <label className={'nanum-gothic-bold'}>원</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                        <section className={'row p-3 justify-content-center align-items-center'}>
                            <div className={'d-grid col-sm-8'}>
                                <label className={'fs-5 nanum-gothic-bold text-start'}>요청 코멘트</label>
                                <textarea className={'form-control'} rows={10}
                                          value={reqComment} onChange={(e) => setReqComment(e.target.value)}></textarea>
                            </div>
                        </section>
                        <button type={'button'} className={'mt-3 px-4 me-2 btn btn-primary'}
                                onClick={handleSubmit}>상신
                        </button>
                        <button type={'reset'} className={'mt-3 px-4 me-2 btn btn-secondary'}
                                onClick={() => navigate('/')}>취소
                        </button>
                    </div>
                </div>
            </div>
            {showModal && !isReadOnly && (
                <SelectVendorModal onClose={() => setShowModal(false)} onSelect={handleVendorSelect}/>
            )}
        </main>
    );
}

export default CommonReq;