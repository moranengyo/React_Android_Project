import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {Editor} from "@tinymce/tinymce-react";
// npm install @tinymce/tinymce-react(텍스트편집기 API 사용, API 키 아래에 있음)

function SendVendorMail() {
  const location = useLocation();
  const navigate = useNavigate();
  const { email, item, requestQuantity } = location.state;
  const [message, setMessage] = useState("");
  const [subject, setSubject] = useState("");

  useEffect(() => {
    const initialMessage =  `
        <h3 className={'nanum-gothic-bold'}>구매 요청 내용</h3>
        <table className={'table table-bordered'}>
          <tbody>
            <tr>
              <th>비품명</th>
              <td width="50%">${item}</td>
            </tr>
            <tr>
              <th>요청수량</th>
              <td>${requestQuantity}</td>
            </tr>
          </tbody>
        </table>
    `;
    setMessage(initialMessage);
  }, [item, requestQuantity]);
  const handleSendMail = () => {
    alert('메일이 발송되었습니다.');
    navigate(-1);

  };

  const handleCancel = () => {
    alert('메일 발송이 취소되었습니다.');
    navigate(-1);
  };

  return (
      <main className={`sub-content-container`}>
        <div className={'row'}>
          <div className={'col-12'}>
            <h1 className={'nanum-gothic-extrabold float-start'}>구매 요청 메일 발송</h1>
          </div>
        </div>

        <div className={'mail-container row m-5 justify-content-center border border-1 rounded-3 p-3'}
             style={{backgroundColor: 'rgba(255,255,255,0.73)'}}
        >
          <div className={'input-group mt-3'}>
            <span className={'nanum-gothic-bold input-group-text'} style={{width: '100px'}}>거래처메일</span>
            <input
                type="text"
                className={'form-control'}
                readOnly={true}
                value={email}/>
          </div>

          <div className={'input-group mt-3'}>
            <span className={'nanum-gothic-bold input-group-text'} style={{width: '100px'}}>제목</span>
              <input
                  type="text"
                  className={'form-control'}
                  placeholder={'메일 제목을 입력하세요'}
                  value={subject}
                  onChange={(e) => setSubject(e.target.value)}
              />
          </div>

          <div className={'input-group mt-3'}>
            <Editor
              apiKey='nnktecto7maxpgp7z4jux7nnedzv3o8y2109opsnekfwcho8'
              value={message}
              init={{
                height: 300,
                width: '100%',
                menubar: false,
                plugins: [
                  'advlist autolink lists link image charmap print preview anchor',
                  'searchreplace visualblocks code fullscreen',
                  'insertdatetime media table paste code help wordcount'
                ],
                toolbar:
                  'undo redo | formatselect | bold italic backcolor | \
                  alignleft aligncenter alignright alignjustify | \
                  bullist numlist outdent indent | removeformat | help'
              }}
              onEditorChange={(newValue) => setMessage(newValue)}
            />
          </div>

          <div className={'mt-3 d-flex justify-content-end'}>
            <button className={'btn btn-primary me-2'} onClick={handleSendMail}>발송</button>
            <button className={'btn btn-danger'} onClick={handleCancel}>취소</button>
          </div>
        </div>
      </main>
  );
}

export default SendVendorMail;