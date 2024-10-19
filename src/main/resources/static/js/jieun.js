//좋아요 버튼 ajax
function likedPlan(heartElement) {
	// planId 값 가져오기
	let planId = heartElement.nextElementSibling.value; 
	
	// planId 값이 제대로 설정됐는지 확인
	console.log("planId: " + planId);
	
	//좋아요 카운트 span.like_plan_num
	let likeCount = heartElement.nextElementSibling.nextElementSibling;
	
	//좋아요 하트 이미지
	let heartImg = heartElement.innerHTML.trim();
	let heartF = '<img src="../images/like.png" alt="">'; //빈이미지
	let heartT = '<img src="../images/like_on.png" alt="">'; //채워진
	
	//컨트롤러 전송용
	let likedCheck = heartImg === heartF ? 1 : 0;
	
	const xhr = new XMLHttpRequest();
	xhr.open("POST", "/plan/likedPlan", true);
	
	// POST 데이터 전송을 위해 헤더 설정
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 

	// 요청 상태가 변경될 때 호출되는 콜백 함수
    xhr.onreadystatechange = function () {		
        if (xhr.readyState === 4 && xhr.status === 200) {
			//빈 하트를 클릭했을 때 (좋아요 생성)
			if(heartImg == heartF) {
				//채워진 하트로 변경
				heartElement.innerHTML = heartT;
				// 좋아요 수 증가
				likeCount.innerText = parseInt(likeCount.innerText) + 1;
			} 
			//채워진 하트를 클릭했을 때 (좋아요 취소)
			else {
				//빈 하트로 변경
				heartElement.innerHTML = heartF;
				// 좋아요 수 감소
				likeCount.innerText = parseInt(likeCount.innerText) - 1;
            } 
		}
    }
	//전송
    xhr.send("planId=" + encodeURIComponent(planId) + "&likedCheck=" + encodeURIComponent(likedCheck));
}

//검색창에서 돋보기 클릭으로 검색
function searchBtnHandler(){
	const form = document.searchForm;
	//유효성 검사
	form.pageNum.value = 1;
	form.submit();
}



$(document).ready(function() {
	$('.pl_type').each(function() {
        let textContent = $(this).text().trim(); // 현재 요소의 텍스트를 가져와 공백 제거

        // 텍스트에 따라 클래스 추가
        if (textContent === '공항') {
            $(this).addClass('air');
        } else if (textContent === '숙소') {
            $(this).addClass('hotel');
        } else if (textContent === '식당') {
            $(this).addClass('eat');
        } else if (textContent === '관광지') {
            $(this).addClass('tour');
        } else if (textContent === '기타') {
            $(this).addClass('etc');
        }
    });
	
	//검색창에서 엔터로 검색
	$("#keyword").keypress(function(e) {
        if (e.which === 13) { // 눌린키가 엔터 키인지
			const form = document.searchForm;
			//유효성 검사
			form.pageNum.value = 1;
			form.submit();
        }
    });
	
	//카테고리 버튼
	$(".cate_btn").click(function(e) {
        //e(이벤트)의 기본 작동 막기 ex) a태그 페이지 이동 막기
        e.preventDefault();

		const form = document.categoryForm;
		//.cate_btn의 href값 가져오기
		form.category.value = e.target.getAttribute("href");
		form.pageNum.value = 1;
		form.submit();
    });
	
    //페이징 버튼
    $(".change_page").click(function(e) {
        //e(이벤트)의 기본 작동 막기 ex) a태그 페이지 이동 막기
        e.preventDefault();
		
		const form = document.pageForm;
		//.change_page의 href값 가져오기
		form.pageNum.value = e.target.getAttribute("href");
		form.submit();
    });
	
	//계획 보러가기 버튼 클릭시
    $(".a_plan_view").click(function(e) {
        //e(이벤트)의 기본 작동 막기 ex) a태그 페이지 이동 막기
        e.preventDefault();
		//.change_page의 href값 가져오기
        let planId = e.target.getAttribute("href");
		//새로운 요청 보내기
		location.replace("/plan/get?planId="+planId);
    });
	

});














