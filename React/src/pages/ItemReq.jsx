import { useLocation } from "react-router-dom";
import CommonReq from "../components/CommonReq.jsx";

function ItemReq() {

  const location = useLocation();
  const { itemData } = location.state || {};

  console.log(itemData)
  return (
    <CommonReq
      title={"비품 추가 구매 요청"}
      reqType={"추가구매"}
      itemData={itemData}
      isReadOnly={true}
    />
  );
}

export default ItemReq;