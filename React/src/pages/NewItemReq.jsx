import CommonReq from "../components/CommonReq.jsx";
import {useLocation} from "react-router-dom";

function NewItemReq() {

  const location = useLocation();
  const { itemData } = location.state || {};
  return (
    <CommonReq
      title={"신규 비품 등록 요청"}
      reqType={"신규등록"}
      itemData={itemData}
      isReadOnly={false}
    />
  );
}

export default NewItemReq;