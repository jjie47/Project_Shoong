const sendit =document.getElementById("submit_button");
const review_form = document.getElementById("review_form");

sendit.addEventListener("click", function(e){	
	const title = document.getElementById("review_title");
	const review_write_box = document.getElementById("review_write_box");
	
	if(title.value.trim()===""){
		alert("제목을 입력하세요.")
		e.preventDefault();
		return;
	}
	if(review_write_box.value.trim()===""){
		alert("내용을 입력하세요.")
		e.preventDefault();
		return;
	}
	
});








