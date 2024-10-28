import axios from "axios";

// const baseUrl = 'http://localhost:8080'

const getAccessToken = () => {
    return localStorage.getItem("ACCESS_TOKEN");
}

const getRefreshToken = () => {
    return sessionStorage.getItem("REFRESH_TOKEN");
}

const getNewAccessToken = (success) => {
    console.log("getNewAccessToken");
    axios.post(`/api/auth/refresh`, null,
        {
            headers: {
                RefreshToken: getRefreshToken()
            }
        })
        .then(res => {
            localStorage.setItem("ACCESS_TOKEN", `Bearer ${res.data}`);
            success();
        })
        .catch(err => {
            alert(`다시 로그인 해주세요`);
            location.href = '/login';
        });
}


const login = (success, userId, userPw) => {
    axios.get(`/api/auth/login`, {
        params: {
            userId: userId,
            userPw: userPw,
        }

    })
        .then(res => {
            success(res.data)

            // location.href = '/';
        })
        .catch(err => {

            alert(`로그인 실패 ${err}`);
        });
}

const unconfirmedUserList = (success, pageNum) => {
    axios.get(`/api/s-manager/unconfirmed-user/${pageNum}`,
        {
            headers: {Authorization: getAccessToken()}
        })
        .then(res => {
            console.log(res.data);
            success(res.data);
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => {
                    unconfirmedUserList(success, pageNum)
                },);
            }
        });
}

const userApproved = (success, id) => {
    axios.put(`/api/s-manager/user/approved/${id}`, null,
        {headers: {Authorization: getAccessToken()}})
        .then(res => {
            success();
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => userApproved(success, id));
            }
        });
}

const userRejected = (success, id) => {
    axios.delete(`/api/s-manager/user/reject/${id}`,
        {headers: {Authorization: getAccessToken()}})
        .then(res => {
            success();
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => userRejected(success, id));
            }
        });
}

const deleteUser = (success, id) => {
    console.log(id)
    axios.delete(`/api/s-manager/user/delete/${id}`,
        {
            headers: {
                Authorization: getAccessToken()
            }
        })
        .then(res => {
            success();
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => userRejected(success, id));
            }
        });
}

const getApprovedUsers = (success, pageNumber, searchVal) => {
    console.log(pageNumber);
    axios.get(`/api/s-manager/approved-user/${pageNumber}`,
        {
            params: {userName: searchVal},
            headers: {Authorization: getAccessToken()}
        })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => getApprovedUsers(success, pageNumber, searchVal));
            }
        })
}

const changeUserRole = (success, id, role) => {
    axios.put(`/api/s-manager/user/changeRole/${id}`, null, {
        params: {
            role: role
        },
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => changeUserRole(success, id, role));
            }
        });
}

const getUserTotalReq = (success, id) => {
    axios.get(`/api/manager/purchase/req/${id}`,
        {headers: {Authorization: getAccessToken()}})
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            if (err.status === 403) {
                getNewAccessToken(() => getUserTotalReq(success, id));
            }
        });
}

const getPurchaseListByDateAndSearch = (success, pageNum, searchDur, searchVal) => {
    axios.get(`/api/manager/purchase/in-store/search/${pageNum}`, {
        params: {
            searchDur: searchDur,
            searchVal: searchVal
        },
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => getPurchaseListByDateAndSearch(success, pageNum, searchDur, searchVal));
            }
        });
}

const getPurchaseListByStatus = (success, pageNum, purchaseStatus = null) => {
    let url = ""
    let config = {params: {}, header: {}}

    switch (purchaseStatus) {
        case null:
            url = `/api/manager/purchase/all/${pageNum}`
            config.params = {
                pageNum: pageNum
            }

            config.header = {Authorization: getAccessToken()}

            break;

        default:

            break;
    }

    axios.get(url, ...config)
        .then(res => {
            success(res.data);
        })
        .catch(err => {

            console.log(err);
        });
}

const getUsageListByDateAndSearch = (success, pageNum, searchDur, searchVal) => {
    console.log(`${pageNum}  ${searchDur}  ${searchVal}`)

    axios.get(`/api/manager/usage/search/${pageNum}`, {
        params: {
            searchDur: searchDur,
            searchVal: searchVal
        },
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => getUsageListByDateAndSearch(success, pageNum, searchDur, searchVal));
            }
        });

}

const getItemListSearch = (success, pageNum, searchVal) => {
    axios.get(`api/user/item/search/p/${pageNum}`, {
        params: {
            searchVal: searchVal
        },
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err)
            if (err.status === 403) {
                getNewAccessToken(() => getItemListSearch(success, pageNum, searchVal));
            }
        })
}

const getCompanyList = (success, pageNum) => {
    axios.get(`api/manager/company/all/${pageNum}`, {headers: {Authorization: getAccessToken()}})
        .then(res => {
            success(res.data);
            console.log(res.data);
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => getCompanyList(success, pageNum));
            }
        });
}

const getSearchCompanyList = (success, searchVal) => {
    axios.get(`/api/manager/company/search`, {
        params: {
            searchVal: searchVal
        },
        headers: {
            Authorization: getAccessToken()
        }
    })
        .then(res => {
            console.log(res);
            success(res.data);
        })
        .catch(err => {
            console.log(err);
        });
}


const addNewCompany = (success, company) => {
    axios.post(`/api/manager/company/new`, {
            id: company.id ? company.id : 0,
            name: company.name,
            email: company.email,
            code: company.code

        }, {headers: {Authorization: getAccessToken()}}
    )
        .then((res) => {
            success();
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => addNewCompany(success, company));
            }
        });
}

const updateCompany = (success, companyId, company) => {
    axios.put(`/api/manager/company/update/${companyId}`, {
        id: companyId,
        name: company.name,
        email: company.email,
        code: company.code
    }, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => updateCompany(success, companyId, company));
            }
        })
}

const deleteCompany = (success, companyId) => {
    axios.delete(`api/manager/company/delete/${companyId}`, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => deleteCompany(success, companyId));
            }
        })
}

const getAlarmItemList = (success, pageNum) => {
    axios.get(`/api/manager/item/under-min/${pageNum}`, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => getAlarmItemList(success, pageNum));
            }
        });
}

const getPurchaseDetail = (success, purchaseId) => {
    axios.get(`/api/manager/purchase/detail/${purchaseId}`, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err)
            if (err.status === 403) {
                getNewAccessToken(() => getPurchaseDetail(success, purchaseId));
            }
        })
}


const getPurchaseList = (success, pageNum, status = "") => {
    if (status === null || status === "") {
        axios.get(`/api/manager/purchase/all/${pageNum}`,
            {
                headers: {Authorization: getAccessToken()}
            })
            .then(res => {
                success(res.data);
            })
            .catch(err => {
                console.log(err);
                if (err.status === 403) {
                    getNewAccessToken(() => getPurchaseList(success, pageNum, status));
                }
            });
    } else {
        axios.get(`/api/manager/purchase/status/${pageNum}`, {
            params: {
                status: status
            },
            headers: {Authorization: getAccessToken()}
        })
            .then(res => {
                success(res.data);
            })
            .catch(err => {
                if (err.status === 403) {
                    getNewAccessToken(() => getPurchaseList(success, pageNum, status));
                }
                console.log(err);

            });
    }

}

const getDashBoardInoutCnt = (success, date) => {
    axios.get(`/api/manager/dashboard`, {
        params: {
            date: date
        },
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err);
        });
}


const getDashBoardPurchaseCnt = (success) => {
    var token = getAccessToken();
    axios.get(`/api/manager/dashboard/purchaseCnt`, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success(res.data);
        })
        .catch(err => {
            console.log(err);
        });
}

const uploadImageFile = (file, result) => {

    const formData = new FormData();
    formData.append('file', file);

    axios.post(`/api/manager/upload`, formData, {
        headers: {
            Authorization: getAccessToken(),
            'Content-Type': 'multipart/form-data'
        }
    })
        .then(res => {
            result(res.data);
        })
        .catch(err => {
            console.log(err);
        });
}

const newPurchaseRequest = (purchaseDTO, result) => {
    console.log(purchaseDTO)
    axios.post(`/api/manager/purchase/req`, purchaseDTO, {
        headers: {
            Authorization: getAccessToken(),
            'Content-Type': 'application/json',
        }
    })
        .then(res => {
            result(res.data);
        })
        .catch(err => {
            console.log(err);
        })
}

const getContainerList = (result) => {
    axios.get(`/api/manager/container/all`, {
        headers: {
            Authorization: getAccessToken(),
        }
    })
        .then(res => {
            result(res.data);
        })
        .catch(err => {
            console.log(err);
        })
}

const checkAccess = (success) => {
    console.log("check")
    axios.get(`/api/auth/check`, {
        headers: {
            Authorization: getAccessToken()
        }
    })
        .then(res => {
            console.log("success");
            success();
        })
        .catch(err => {
            if (err.status === 403) {
                console.log("check fail")
                getNewAccessToken(success);
            }
        })
}


const rejectPurchase = (success, purchaseId, approvedComment) =>{
    axios.post(`/api/s-manager/purchase/reject/${purchaseId}`, {approvedComment}, {
        params : {approvedComment: approvedComment},
        headers : {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            if(err.status === 403){
                getNewAccessToken(() => rejectPurchase(success, purchaseId, approvedComment));
            }
        })
}

const approvePurchase = (success, purchaseId, approvedComment) =>{
    axios.post(`/api/s-manager/purchase/approve/${purchaseId}`, {approvedComment}, {
        params : {approvedComment: approvedComment},
        headers : {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            if(err.status === 403){
                getNewAccessToken(() => approvePurchase(success, purchaseId, approvedComment));
            }
        });
}

const makeQRCode = (itemId, purchaseId, success) => {

    axios.post(`/api/manager/qrcode?itemId=${itemId}&purchaseId=${purchaseId}`, {}, {
        headers: { Authorization: getAccessToken() },
        responseType: 'arraybuffer'
    })
        .then(res => {
            const arrayBuffer = res.data;
            const base64Image = btoa(
                new Uint8Array(arrayBuffer).reduce(
                    (data, byte) => data + String.fromCharCode(byte),
                    ''
                )
            );

            // 성공 시 콜백 함수 실행
            success(`data:image/png;base64,${base64Image}`);
        })
        .catch(err => {
            console.error(err);
        });
};

const deletePurchase = (purchaseId, success) => {
    console.log(`axios purchase: ${purchaseId}`)
    axios.delete(`/api/manager/purchase/delete/${purchaseId}`, {
        headers: {Authorization: getAccessToken()}
    })
        .then(res => {
            success();
        })
        .catch(err => {
            console.log(err);
            if (err.status === 403) {
                getNewAccessToken(() => deletePurchase(success, purchaseId));
            }
        })
}

export {
    login,
    checkAccess,
    getDashBoardInoutCnt, getDashBoardPurchaseCnt,
    unconfirmedUserList,
    userRejected, deleteUser, userApproved, getApprovedUsers, changeUserRole, getUserTotalReq,
    getPurchaseListByDateAndSearch, getPurchaseListByStatus, getPurchaseList, getPurchaseDetail, rejectPurchase, approvePurchase,
    getUsageListByDateAndSearch,
    getItemListSearch, getAlarmItemList,
    uploadImageFile,
    getCompanyList, addNewCompany, updateCompany, deleteCompany, getSearchCompanyList,
    newPurchaseRequest, getContainerList, makeQRCode, deletePurchase,
}