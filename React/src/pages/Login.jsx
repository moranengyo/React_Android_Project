import logo from "/src/assets/logo.png";
import {login} from "../service/axiosFunc.js"
import {useNavigate} from "react-router-dom";


function Login({ setRole }) {
// 접근 권한 임시 테스트 (true - 관리자 / false - 부장)
  const navigate = useNavigate();

  // 관리자 접속
  const loginHandler = (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const userId = formData.get('userId');
    const userPw = formData.get('userPw');

    login((data) => {
      console.log(data)
          var user = data.userDto
          if (user.role === "ROLE_SENIOR_MANAGER" || user.role === "ROLE_MANAGER") {
            localStorage.setItem("ACCESS_TOKEN", `Bearer ${data.jwtDto.accessToken}`);
            localStorage.setItem("USER", JSON.stringify(data.userDto));
            localStorage.setItem("ROLE", user.role);

            sessionStorage.setItem("REFRESH_TOKEN", data.jwtDto.refreshToken);
            setRole(user.role === "ROLE_MANAGER");
            navigate("/");
          }
          else{
            alert(`권한이 없습니다.`)
          }
        },
        userId
        , userPw);
  };

  // 부장 접속
  const cancelHandler = () => {
    setRole(false);
    navigate("/");
  };

  return (
      <main className={'login-container login-background no-scroll'}>
        <div className={'login-content'}>
          <div>
            <img className={'logo'} src={logo} style={{width:'80%', height:'80%', padding:'0'}} />
            <div className={'login-box col-sm bg-white border border-2 rounded-3 p-3'}>
              <h1 className={'text-start nanum-gothic-extrabold'}>Login</h1>
              <hr/>
              <div className={'col-sm mx-auto'}>
                <div className={'border rounded-3 p-5'}>
                  <form onSubmit={loginHandler}>
                    <div className={'mt-3'}>
                      <div className={'form-floating'}>
                        <input type="text" className={'form-control'} id={''} name={'userId'} placeholder={'LoginID'}/>
                        <label htmlFor="" className={'form-label nanum-gothic-regular'}>ID</label>
                      </div>
                    </div>
                    <div className={'mt-3'}>
                      <div className={'form-floating'}>
                        <input type="password" className={'form-control'} id={''} name={'userPw'} placeholder={'LoginPW'}/>
                        <label htmlFor="" className={'form-label nanum-gothic-regular'}>PW</label>
                      </div>
                    </div>
                    <div className={'mt-3 d-grid gap-2'}>
                      <button type={"submit"} className={'btn btn-blue nanum-gothic-bold'}>로그인</button>
                      <button type={"button"} className={'btn btn-secondary nanum-gothic-bold'} onClick={cancelHandler}>취소</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
  );
}

export default Login;