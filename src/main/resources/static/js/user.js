//회원가입
let isIdChecked = false;
let isPhoneVerified = false;

function checkId(){
    const userId = document.getElementById("userId");
    const idMessage = document.getElementById("idMessage");

    if(userId.value == ""){
        alert("아이디를 입력하세요!");
        userId.focus();
        return;
    }
	
	if(!/^[a-z0-9]{4,12}$/.test(userId.value)){
		idMessage.textContent = "영문 소문자와 숫자만 사용하여, 4~12자의 아이디를 입력해주세요.";
		idMessage.style.color = "red";
		return;
	}

	// 서버에 아이디 중복 체크 요청
	const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4 && xhr.status == 200){
            let txt = xhr.responseText.trim();
            
			if(txt == "O"){
				isIdChecked = true;
                idMessage.innerHTML = "사용할 수 있는 아이디입니다!";
                idMessage.style.color = "blue";
                document.joinform.nickname.focus();
            }
            else{
                idMessage.innerHTML = "중복된 아이디가 있습니다!";
                idMessage.style.color = "red";
                userId.value = "";
                userId.focus();
            }
        }
    }

    xhr.open("GET", "/user/checkId?userId="+userId.value);
    xhr.send();
}

function checkPw(){
    const password = document.joinform.password.value;
    const pwMessage = document.getElementById("pwMessage");

    if(password == ""){
        alert("비밀번호를 입력하세요!");
        password.focus();
        return;
    }

    // 비밀번호 유효성 검사 조건
    const regex = /^(?=.*[A-Za-z])(?=.*\d|.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,20}$/;
   
    if(!regex.test(password)){
        pwMessage.textContent = "영문 대문자와 소문자, 숫자, 특수문자 중 2가지 이상을 조합하여 6~20자로 입력해주세요.";
        pwMessage.style.color = "red";
        return;
    }
    else{
        pwMessage.textContent = "";
    }
}

function matchPw(){
    const password = document.joinform.password.value;
    const password_re = document.joinform.password_re.value;
    const pwMessage = document.getElementById("pwMessage");

    // 비밀번호 확인
    if(password != password_re){
        pwMessage.innerHTML = "비밀번호가 일치하지 않습니다.";
        pwMessage.style.color = "red";
    }
    else{
        pwMessage.innerHTML = "비밀번호가 일치합니다.";
        pwMessage.style.color = "blue";
		document.joinform.phoneNumber.focus();
    }
}

let countdown;
function sendPhoneNum(){
	let phoneNumber = document.getElementById("phoneNumber").value;
	
	if(!phoneNumber){
		alert("휴대전화 번호를 입력해주세요.");
		phoneNumber.focus();
		return;
	}
	
	$.ajax({
		type:"POST",
		url:"/sms/send",
		data:JSON.stringify({phoneNumber: phoneNumber}),
		contentType:"application/json;charset=utf-8",
		success:function(response){
			if(response.status == "success"){
				//alert("인증번호가 발송되었습니다. 확인해주세요.");
				document.getElementById("phoneAuth").style.display = "block";
				document.getElementById("email_box").style.display = "none";
				
				startCountdown(240);
				document.joinform.phone_number_auth.focus();
			}
			else{
				alert("인증번호가 발송에 실패했습니다." + response.message);
			}
		},
		error:function(xhr, status, error){
			alert("인증번호 발송 중 오류가 발생했습니다." + xhr.responseText);
		}
	});
}

function startCountdown(seconds){
	let remainingTime = seconds;
	const countdownDisplay = document.getElementById("countdown");
	
	countdown = setInterval(() => {
		const minutes = Math.floor(remainingTime / 60);
		const secs = remainingTime % 60;
		countdownDisplay.innerText = `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
		
		if(remainingTime <= 0){
			clearInterval(countdown);
			countdownDisplay.innerHTML = "시간 초과! 다시 시도해주세요.";
		}
		
		remainingTime--;
	}, 1000);
}

function checkNum(){
	let authCode = document.getElementById("phone_number_auth").value;
	
	if(!authCode){
		alert("인증번호를 입력해주세요.");
		authCode.focus();
		return;
	}
	
	$.ajax({
		type:"POST",
		url:"/sms/verify",
		data:JSON.stringify({authCode: authCode}),
		contentType:"application/json;charset=utf-8",
		success:function(response){
			if(response.status == "success"){
				isPhoneVerified = true;
				alert("인증번호가 확인되었습니다.");
				document.getElementById("phoneAuth").style.display = "none";
				document.getElementById("email_box").style.display = "block";
				document.joinform.email.focus();
			}
			else{
				alert("인증번호가 일치하지 않습니다.");
			}
		},
		error:function(xhr, status, error){
			alert("인증번호 확인 중 오류가 발생했습니다." + xhr.responseText);
		}
	});
}

function sendForm(){
	const joinForm = document.joinform;
	
	if(!isIdChecked){
		alert("아이디 중복확인 버튼을 눌러주세요.");
		return;
	}
	if(!isPhoneVerified){
		alert("인증번호 버튼을 눌러주세요.");
	}
	if(!joinForm.userId.value || !joinForm.nickname.value || !joinForm.password.value || !joinForm.phoneNumber.value || !joinform.email.value){
		alert("필수 항목을 모두 입력해주세요.");
		return;
	}
	
	const formData = new FormData(joinForm);
	
	$.ajax({
		type: "POST",
		url: "/user/join",
		data: formData,
		contentType: false,
		processData: false,
		success: function(response){
			if(response.status == "success"){
				alert("회원가입이 완료되었습니다.");
				
				//뒤로 가기 방지 로직
				//window.history.forward();
				window.history.pushState(null, null, window.location.href);
				window.onpopstate = function(event) {
					window.history.go(1);  
				};
				
				window.location.href= "/user/login";

			} else{
				alert("회원가입에 실패했습니다. 다시 시도해주세요.");
			}
		},
		error: function(xhr, status, error){
			alert("회원가입 중 오류가 발생했습니다.");
		}
	});
}

//로그인
function loginEnter(event){
	if(event.key == 'Enter'){
		event.preventDefault();
		sendLoginForm();
	}
}

function sendLoginForm(){
	const loginform = document.loginform;
	
	const userId = loginform.userId.value.trim();
	const password = loginform.password.value.trim();
	
	if(!userId){
		alert("아이디를 입력해주세요.");
		loginform.userId.focus();
		return;
	}
	if(!password){
		alert("비밀번호를 입력해주세요.");
		loginform.password.focus();
		return;
	}
	
	const data = {
		userId: userId,
		password: password
	};
	
	$.ajax({
		type: "POST",
		url: "/user/login",
		contentType: "application/json",
		data: JSON.stringify(data),
		success: function(response){
			if(response.status == "success"){
				alert("로그인이 완료되었습니다.");
				
				//뒤로 가기 방지 로직 추가하기
				
				window.location.href= "/";
			}
			else{
				alert("로그인에 실패했습니다. 아이디나 비밀번호를 확인해주세요.");
			}
		},
		error: function(xhr, status, error){
			alert("로그인 중 오류가 발행했습니다.");
		}
	});
}