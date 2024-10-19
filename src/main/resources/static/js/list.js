// 글쓰기 버튼 클릭 이벤트 추가
$('.write_button').click(function(){
	window.location.href = '/review/write'; // 페이지 이동
});

let currentPage = 1;
const reviews = $("#reviews");
let isLoading = false;

window.addEventListener("scroll",(e)=>{
	// 전체 높이
    const fullHeight = document.body.offsetHeight;
    // 현재 높이
    const currentHeight = window.innerHeight;
    // 현재 스크롤 된 값
    const scrollValue = window.scrollY;
	// 마지막인지
	const isScrollEnded = currentHeight + scrollValue >= fullHeight;
	
	if(isScrollEnded && !isLoading){
		currentPage++;
		isLoading = true;
		$.getJSON(
			`/review/loadMore/${currentPage}`,
			function(data){ //  [{},{},...]
				for (const review of data) { 
					const reviewId = review.reviewId; 
					const commentCount = review.commentCount || 0; 
				reviews.append(`<div>
									 <div class="white_box">
					        			<div class="review_box" onclick="readReview(${review.reviewId})">

											<a href="/review/read?reviewId=${review.reviewId}">
												<div class="review_top">
													<span class="review_date">${review.createdDate}</span>
													<button class="review_like">
					                                	<img src="/images/like.png" alt="">
					                            	</button>
												</div>
												<h2>${review.title}</h2>
												<p>댓글 (${commentCount})</p>
												
											</a>

									 	</div> <!-- reviewbox -->
							        </div> <!-- white_wbox -->
							    </div> <!-- th:each 끝 -->`)
				}
				isLoading = false;
			}
		)
	}
})