// 전역 변수 ===============================================================
// 전체 레이아웃
// contents의 각 tab
const members_tab = $('.plan_holder').children('section:eq(0)');
const destinations_tab = $('.plan_holder').children('section:eq(1)');
const date_tab = $('.plan_holder').children('section:eq(2)');
const places_tab = $('.plan_holder').children('section:eq(3)');
const index_tab = $('.plan_holder').children('section:eq(4)');
const schedules_tab = $('.plan').children('section');
// tab별 추가 화면
const member_invite = $('.detail').children('div:eq(0)');
const destination_choose = $('.detail').children('div:eq(1)');
const date_choose = $('.detail').children('div:eq(2)');
const place_choose = $('.detail').children('div:eq(3)');
const schedule_manage = $('.detail').children('div:eq(4)');
// tab별 index
const plan_indexes = $('.plan_holder').find('.index');
const member_index = members_tab.find('.index');
const destinations_index = destinations_tab.find('.index');
const date_index = date_tab.find('.index');
const places_index = places_tab.find('.index');
const index_index = index_tab.find('.index');
// 일정 짜기 버튼, 저장 버튼
const btn_schedule = $('.btn_schedule');
const btn_save = $('.btn_save');
// 지도 커버
const cover = $('.cover');

// 2. 도시 및 나라 선택
// 대륙 카테고리 버튼
const continentCategories = $('.continents').children('button');
const world = $('.continents').children('button:eq(0)');
const southeast_asia = $('.continents').children('button:eq(1)');
const asia = $('.continents').children('button:eq(2)');
const oceania = $('.continents').children('button:eq(3)');
const europe = $('.continents').children('button:eq(4)');
const america = $('.continents').children('button:eq(5)');
let continent = '전체';



// 전체 레이아웃 ===============================================================
// event
// tab 전환시 index의 커서 상태 변경
function changeCursorStyle(e) {
    plan_indexes.css('cursor', 'pointer');
    if(e.currentTarget!=index_tab.get(0)) {
        $(e.target).css('cursor','default');
    } else {
        member_index.css('cursor','default');
    }
}
$(function() {
    // header
    // header - hover시 tab 이동
    $('header').find('a').hover(function() {
        $('#contents').children('.holder').css('left','-247px');
    }, function() {
        $('#contents').children('.holder').css('left','-361px');
    })
    // tab
    // index - hover시 밑줄
    plan_indexes.hover(function() {
        $(this).children().css('text-decoration-line', 'underline')
    }, function() {
        $(this).children().css('text-decoration-line', 'none')
    })
    // index
    // index - click시 tab 전환
    member_index.click(function(e) {
        members_tab.css('top','0px');
        destinations_tab.css('top','calc(100vh - 295px)');
        date_tab.css('top','calc(100vh - 224px)');
        places_tab.css('top','calc(100vh - 153px)');

        member_invite.css('visibility', 'visible');
        destination_choose.css('visibility', 'hidden');
        date_choose.css('visibility', 'hidden');
        place_choose.css('visibility', 'hidden');
        cover.css('visibility', 'visible');

        changeCursorStyle(e);

        getInviteSocketConnection();
    })      
    destinations_index.click(function(e) {
        members_tab.css('top','0px');
        destinations_tab.css('top','71px');
        date_tab.css('top','calc(100vh - 224px)');
        places_tab.css('top','calc(100vh - 153px)');

        member_invite.css('visibility', 'hidden');
        destination_choose.css('visibility', 'visible');
        date_choose.css('visibility', 'hidden');
        place_choose.css('visibility', 'hidden');
        cover.css('visibility', 'visible');

        changeCursorStyle(e);

        closeInviteSocketConnection();
    })      
    date_index.click(function(e) {
        members_tab.css('top','0px');
        destinations_tab.css('top','71px');
        date_tab.css('top','142px');
        places_tab.css('top','calc(100vh - 153px)');

        member_invite.css('visibility', 'hidden');
        destination_choose.css('visibility', 'hidden');
        date_choose.css('visibility', 'visible');
        place_choose.css('visibility', 'hidden');
        cover.css('visibility', 'visible');

        changeCursorStyle(e);

        closeInviteSocketConnection();
    })      
    places_index.click(function(e) {
        members_tab.css('top','0px');
        destinations_tab.css('top','71px');
        date_tab.css('top','142px');
        places_tab.css('top','213px');

        member_invite.css('visibility', 'hidden');
        destination_choose.css('visibility', 'hidden');
        date_choose.css('visibility', 'hidden');
        place_choose.css('visibility', 'visible');
        cover.css('visibility', 'hidden');
        
        changeCursorStyle(e);

        closeInviteSocketConnection();
    })
    index_tab.click(function(e) {
        index_tab.css('visibility', 'hidden');
        members_tab.css('visibility','visible');
        destinations_tab.css('visibility','visible');
        date_tab.css('visibility','visible');
        places_tab.css('visibility','visible');

        members_tab.css('top','0px');
        destinations_tab.css('top','calc(100vh - 295px)');
        date_tab.css('top','calc(100vh - 224px)');
        places_tab.css('top','calc(100vh - 153px)');

        member_invite.css('visibility', 'visible');
        destination_choose.css('visibility', 'hidden');
        date_choose.css('visibility', 'hidden');
        place_choose.css('visibility', 'hidden');
        schedule_manage.css('visibility', 'hidden');
        cover.css('visibility', 'visible');


        btn_schedule.css('visibility', 'visible');
        btn_save.css('visibility', 'hidden');

        changeCursorStyle(e);

        closeInviteSocketConnection();
    })
    btn_schedule.click(function() {
        if(selectedPlaces.size == 0) {
            // console.log("추가된 장소가 없어요.")
            return;
        }
        index_tab.css('visibility', 'visible');
        members_tab.css('top','calc(100vh - 153px)');
        destinations_tab.css('top','calc(100vh - 153px)');
        date_tab.css('top','calc(100vh - 153px)');
        places_tab.css('top','calc(100vh - 153px)');

        setTimeout(function() {
            members_tab.css('visibility','hidden');
            destinations_tab.css('visibility','hidden');
            date_tab.css('visibility','hidden');
            places_tab.css('visibility','hidden');
        }, 400)
        
        member_invite.css('visibility', 'hidden');
        destination_choose.css('visibility', 'hidden');
        date_choose.css('visibility', 'hidden');
        place_choose.css('visibility', 'hidden');
        schedule_manage.css('visibility', 'hidden');
        cover.css('visibility', 'hidden');

        $(this).css('visibility', 'hidden');
        btn_save.css('visibility', 'visible');

        // 기간 불러오기
        bindPeriod();

        closeInviteSocketConnection();
    })
})

// 1. 그룹 초대 ===============================================================
// 그룹 초대 - button

getInviteSocketConnection();
function getInviteSocketConnection() {
    if(socket_invite==null) {
        socket_invite = new WebSocket(`ws://localhost:8080/invite/${userId}`);

        socket_invite.onerror = function(e) {
            // console.log(e);
        }
        socket_invite.onopen = function(e) {
            // console.log(e);
        }
        socket_invite.onmessage = function(e) {
            // console.log(e.data);
            let sender = e.data.split(":")[0];
            let responsedPlanId = e.data.split(":")[1];
            if(responsedPlanId == String(planId)) {
                if(sender=='accept' || sender=='refuse') {
                    getGroupList();
                }
            }
        }
        socket_invite.onclose = function(e) {
            // console.log(e);
        }
    }
}
function closeInviteSocketConnection() {
    if(socket_invite!=null) {
        socket_invite.close();
        socket_invite = null;
    }
}


$(function() {
    // 초대 버튼 - click시 이벤트 제거
    $(document).on("click", ".btn_invite", function(e) {
        // 이벤트 제거
        $(e.currentTarget).addClass("btn_clicked btn_no_hover");
        
        const inviteUserId = $(e.currentTarget).siblings('input').val();

        // 초대 요청
        $.ajax({
            url: "/group/invite",
            type: "POST",
            data: JSON.stringify({"planId": planId,
                    "userId": inviteUserId}),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                // 성공
                if(result === "invite success") {
                    // socket 전송
                    socket_invite.send(`invite:${planId}:${inviteUserId}`);
                    getGroupList();
                }

                // 실패
                else if(result === "invite failed") {
                    alert("초대 오류 발생!!")
                    setTimeout(()=>{
                        $(e.currentTarget).removeClass("btn_clicked btn_no_hover");
                    }, 3000)
                }
            }
        })
    })
})
// 그룹 초대 - 목록
// 사용자 검색
function searchUser() {
    const keyword = $('#search_user').val();
    if (keyword.length > 0) {
        $.ajax({
            url: "/user/search",
            type: "GET",
            data: {"keyword": keyword,
                "planId": planId
            },
            success: function (userList) {
                let str = "";
                for(const user of userList) {
                    str += `<div class="user">
                                        <input type="hidden" value="${user.userId}"/>
                                        <div class="user_img">
                                            <img src="" alt="">
                                        </div>
                                        <div class="user_info">
                                            <span>${user.nickname}</span>
                                            <span>${user.userId}</span>
                                        </div>
                                        <div class="btn btn_invite">
                                            <span>초&nbsp&nbsp대</span>
                                        </div>
                                    </div>`;
                }
                $('#userList').html(str);
            }
        })
    }
    else {
        $('#userList').html('');
    }
}
// 그룹원 목록 불러오기
window.onload = getGroupList();
function getGroupList() {
    $.ajax({
        url: "/group/list",
        type: "GET",
        data: {"planId": planId},
        success: function (groupMemberList) {
            $('#groupMemberList').replaceWith(groupMemberList);
        }
    })
}

// 2. 도시 및 나라 선택 ===============================================================
// 도시 및 나라 선택 - event
$(function() {
    // 대륙 카테고리 - click
    $('.btn_continent').click(function() {
        continentCategories.addClass('btn_unclicked');
        $(this).removeClass('btn_unclicked');
        
        continent = $(this).html(); // == '전체' ? '' : $(this).html();
        // ajax 호출 - 목적지 검색(카테고리명 전달)
        searchDefaultDestination();
    });
    // 기본목적지 추가 - click
    $(document).on("click", ".destination", function(e) {
        const destinationId = $(e.currentTarget).children('input').val();
        addDefaultDestination(destinationId);
    })
    // 선택된 기본목적지 삭제 - click
    $(document).on("click", ".selected_destination", function(e) {
        const destinationId = $(e.currentTarget).children('input').val();
        removeDefaultDestination(destinationId);
    })
})

// 도시 및 나라 선택
// 목적지 검색
window.onload = searchDefaultDestination();
function searchDefaultDestination() {
    const keyword = $('#search_destination').val();
    $.ajax({
        url: "/defaultdestination/list",
        type: "GET",
        data: {"keyword": keyword, "continent": continent},
        success: function (defaultDestinationList) {
            $('#defaultDestinationList').replaceWith(defaultDestinationList)
        }
    })
}

// 선택 기본목적지 전체 조회
function getSelectedDefaultDestinations() {
    $('.selected_destination_list').html("");
    for(let value of selectedDefaultDestinations.values()) {
        let str = "";
        str += `<div class="selected_destination">
                    <input type="hidden" value="${value.destinationId}">
                    <div class="selected_destination_img">
                        <img src="/images/default_destination.jpg" alt="" style="background-color: #000">
                    </div>
                    <div class="selected_destination_name">
                        <span>${value.cityKor}</span>
                    </div>
                </div>`;
        $('.selected_destination_list').append(str);
    }
}

// 기본목적지 추가
function addDefaultDestination(destinationId) {
    $.ajax({
        url: "/defaultdestination/get",
        type: "GET",
        data: {"destinationId": destinationId},
        success: function (data) {
            if(!selectedDefaultDestinations.get(Number(data.destinationId))) {
                selectedDefaultDestinations.set(data.destinationId, data); 
               
            
                let str = "";
                str += `<div class="selected_destination">
                            <input type="hidden" value="${data.destinationId}">
                            <div class="selected_destination_img">
                                <img src="/images/default_destination.jpg" alt="" style="background-color: #000">
                            </div>
                            <div class="selected_destination_name">
                                <span>${data.cityKor}</span>
                            </div>
                        </div>`;
                $('.selected_destination_list').append(str);
            }
        }
    })
}

// 기본목적지 삭제 후 조회
function removeDefaultDestination(destinationId) {
    selectedDefaultDestinations.delete(Number(destinationId));
    getSelectedDefaultDestinations();
}


// 3. 날짜 선택 ===============================================================
// 날짜 추가
function addDate(start, end) {
    selectedDates.set("startDate", start);
    selectedDates.set("endDate", end);
    
    dateArr = getDatesStartToLast(start, end);

    bindDates();
}
// 날짜 초기화
addDate(timeEx, timeEx);

// 사이 모든 날짜 배열로 뽑기
function getDatesStartToLast(startDate, endDate) {
	var regex = RegExp(/^\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/);
	if(!(regex.test(startDate) && regex.test(endDate))) return "Not Date Format";
	var result = [];
	var curDate = new Date(startDate);
	while(curDate <= new Date(endDate)) {
		result.push(curDate.toISOString().split("T")[0]);
		curDate.setDate(curDate.getDate() + 1);
	}
	return result;
}


// 4. 장소 선택 ===============================================================
$(function() {
    // 장소 검색 - keyup
    $('#search_place').keydown(function (e){
        if(e.keyCode===13) {
            const query = $(this).val();
            findPlaces(query);
        }
    })
    // 장소 추가 - click
    $(document).on("click", ".btn_add_place", function(e) {
        // place_id 값 참조
        const id = $(e.currentTarget).siblings('input').val();
    
        // button 색 변경 및 hover 제거
        $(e.currentTarget).addClass("btn_clicked btn_no_hover");
    
        if(!selectedPlaces.get(id)) {
        // Places API - Details
            getPlaceDetails(id).then((response) => {
                const place = parsePlace(response);
    
                bindSelectPlace(place);
                addPlace(place);
            });
        }
    })
    // 장소 삭제 - hover
    $(document).on("mouseenter", ".selectedPlace", function(e) {
        $(e.currentTarget).children('.place_remove').css('visibility', 'visible');
    })
    $(document).on("mouseleave", ".selectedPlace", function(e) {
        $(e.currentTarget).children('.place_remove').css('visibility', 'hidden');
    })
    // 장소 삭제 - click
    $(document).on("click", ".place_remove", function(e) {
        const id = $(e.currentTarget).siblings('input').val();
        removePlace(id);
    })
})

// 장소 선택
// 장소 추가
function addPlace(place) {
    selectedPlaces.set(place.id, place);
    // 선택 목적지 목록 추가
    const destination = {
        // destinationId: destinationKey,
        cityKor: place.city,
        countryKor: place.country,
        countryCode: place.countryCode,
        continent: place.continent
    }
    destinationKey--;
    selectedDestinations.add(JSON.stringify(destination));
}

// 장소 삭제
function removePlace(id) {
    selectedPlaces.delete(id);
    removeSelectedPlace(id);
    deselectSearchedPlace(id);
    deleteSelectedMarker(id);
}


// 5. 일정 짜기 ===============================================================
$(function() {
    // 일정 짜기
    // 일별 조회 - click
    $(document).on("click", ".schedule_info", function(e) {
        const itinerarieList = $(e.currentTarget).parent().siblings();
        itinerarieList.toggle();
    })
    // 일정 - hover
    $(document).on("mouseenter", ".itinerary", function(e) {
        $(e.currentTarget).children('.itinerary_remove').css('visibility', 'visible');
    })
    $(document).on("mouseleave", ".itinerary", function(e) {
        $(e.currentTarget).children('.itinerary_remove').css('visibility', 'hidden');
    })
    // 일정 - click
    $(document).on("click", ".itinerary", function(e){
        // console.log("현재 이벤트 : " + $(lastItineraryEvent));

        // 일정 관리 화면 띄우기
        $('.schedule_manage').css('visibility', 'visible');
        // 이전 선택 일정 테두리색 변화
        $(lastItineraryEvent).parent().css("border", "none");
		
        // 클릭시 재정렬
        // 이전 일정의 id 저장
        let iId = $(e.currentTarget).children('input').val();
        // 시간 정렬
        const date = $(lastItineraryEvent).closest('.schedule').children('input').val();
        $(lastItineraryEvent).closest('.itineraries').html(bindItinerariesByDate(date));
        // 클릭한 id에 해당하는 event 객체 복구
        const clickedItinerary = $('.schedules').find(`input[value=${iId}]`).parent();

        // 일정의 event 객체 저장
        lastItineraryEvent = clickedItinerary;
        // 현재 선택 일정 테두리색 변화
        $(lastItineraryEvent).parent().css({"border-top":"2px solid #006BDF",
            "border-bottom":"2px solid #006BDF"});
        // 새로운 일정 Key 불러오기
        iId = $(lastItineraryEvent).children('input').val();
        // 일정 검색어 목록 불러오기
        const keyword1 = itineraries.get(Number(iId)).lastKeyword;
        $('#schedule_search_place').val(keyword1);
        searchSelectedPlaces(keyword1);
        // 일정 시간 불러오기
        const startTime = parseTimeValueToTime(itineraries.get(Number(iId)).startTime);
        const endTime = parseTimeValueToTime(itineraries.get(Number(iId)).endTime);
        $('.time_choose').find('input:eq(0)').val(startTime);
        $('.time_choose').find('input:eq(1)').val(endTime);
        // 일정 비용 불러오기
        let str = '';
        $('.cost_list').html('');
        for(const [key, value] of costs) {
            const content = (value.content === "")? "항목명" : value.content;
            const payer = (value.payer === "")? "( 이름 )" : value.payer;
            const amount = (value.amount === 0)? "예상 금액" : value.amount.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")+"원";
            const color = (value.payer!=="")? "style='color:#828282'":"style='color:#E1E1E1'";
            if(value.itineraryId === iId) {
                str += `<div class="cost">
                            <input type="hidden" value="${key}">
                            <span class="cost_separator"></span>
                            <div class="cost_name">
                                <span>${content}</span>
                                <input type="text">
                            </div>
                            <div class="cost_payer" ${color}>
                                <span>${payer}</span>
                                <input type="text">
                            </div>
                            <div class="cost_amount">
                                <span>${amount}</span>
                                <input type="text">
                            </div>
                            <div class="cost_remove">
                                <img src="/images/remove.png" alt="">
                            </div>
                        </div>`;
            }
        }
        $('.cost_list').append(str);
    })
	
    // 일정 추가 - click
    $(document).on("click", ".itinerary_add", function(e) {
        if(lastItineraryEvent!==null) {
            // 이전 선택 일정 테두리색 변화
            // $(lastItineraryEvent).parent().css("border", "none");
            // 클릭시 재정렬
            // 시간 정렬
            const date = $(lastItineraryEvent).closest('.schedule').children('input').val();
            $(lastItineraryEvent).closest('.itineraries').html(bindItinerariesByDate(date));
        }
        // 새로운 일정 화면 바인딩 및 객체 추가
        addItinerary(e);
        addItineraryDate(e);
        
        // 일정의 event 객체 저장
        lastItineraryEvent = $(e.currentTarget).siblings('.itineraries').children().last().children('.itinerary').get(0);
        const iId = $(lastItineraryEvent).children('input').val();
        
        // 일정관리 탭 띄우기
        schedule_manage.css('visibility', 'visible');
        // 장소 검색 초기화
        searchSelectedPlaces('');
        $('#schedule_search_place').val('');
        // 일정관리 시간 초기화 - 현재시간
        const hour = String(new Date().getHours()).padStart(2, '0');
        const minute = String(new Date().getMinutes()).padStart(2, '0');
        const time = `${hour}:${minute}:00`;
        $('.time_choose').find('input').val(parseTimeValueToTime(time));
        // 비용 목록 초기화
        $('.cost_list').html('');
        // 현재 선택 일정 테두리 색 변화
        $(lastItineraryEvent).parent().css({"border-top":"2px solid #006BDF",
            "border-bottom":"2px solid #006BDF"});
    })
    // 일정 삭제 - click
    $(document).on("click", ".itinerary_remove", function(e) {
        e.stopPropagation();
        removeItinerary(e);
    })
    
    // 일정 관리
    // 장소 설정
    // 장소 검색
    $('#schedule_search_place').keyup(function (e){
        // 검색 키워드 불러오기
        let keyword = $(this).val();
        keyword = keyword.length < 1 ? '': keyword;
        // 검색 키워드를 일정 객체에 입력
        const iId = $(lastItineraryEvent).children('input').val();
        itineraries.get(Number(iId)).lastKeyword = keyword;
        // 검색
        searchSelectedPlaces(keyword);
    })
    // 장소 선택
    $(document).on("click", ".btn_add_place_schedule", function(e) {
        const placeId = $(e.currentTarget).siblings('input').val();
        addItineraryPlace(lastItineraryEvent, placeId)
        bindItineraryPlace(lastItineraryEvent);
    })
    
    // 비용 관리
    // 비용 - hover
    $(document).on("mouseenter", ".cost", function(e) {
        $(e.currentTarget).children('.cost_remove').css('visibility', 'visible');
    })
    $(document).on("mouseleave", ".cost", function(e) {
        $(e.currentTarget).children('.cost_remove').css('visibility', 'hidden');
    })
    // 비용 추가 - click
    $(document).on("click", ".cost_add", function(e) {
        addCost(e);
    })
    // 비용 삭제 - click
    $(document).on("click", ".cost_remove", function(e) {
        removeCost(e);
    })
    // 항목명 변경
    $(document).on("click", ".cost_name span, .cost_payer span, .cost_amount span", function(e) {
        $(e.currentTarget).siblings('input').css('visibility', 'visible');
        $(e.currentTarget).siblings('input').focus();
        $(e.currentTarget).css('visibility', 'hidden');
    })
    $(document).on("blur", ".cost_name input", function(e) {
        $(e.currentTarget).css('visibility', 'hidden');
        $(e.currentTarget).siblings('span').css('visibility', 'visible');
        let value = $(e.currentTarget).val();
        if(value!=="") {
            $(e.currentTarget).siblings('span').html(value);
        }
        else {
            $(e.currentTarget).siblings('span').html("항목명");
            value = ""
        }

        const costId = $(e.currentTarget).parent().siblings('input').val();
        costs.get(Number(costId)).content = value;
        console.log(JSON.stringify(costs.get(Number(costId))));
    })
    $(document).on("keyup", ".cost_name input", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).css('visibility', 'hidden');
            $(e.currentTarget).siblings('span').css('visibility', 'visible');
            let value = $(e.currentTarget).val();
            if(value!=="") {
                $(e.currentTarget).siblings('span').html(value);
            }
            else {
                $(e.currentTarget).siblings('span').html("항목명");
                value = ""
            }

            const costId = $(e.currentTarget).parent().siblings('input').val();
            costs.get(Number(costId)).content = value;
        }
    })
    $(document).on("blur", ".cost_payer input", function(e) {
        $(e.currentTarget).css('visibility', 'hidden');
        $(e.currentTarget).siblings('span').css('visibility', 'visible');
        let value = $(e.currentTarget).val();
        if(value!=="") {
            $(e.currentTarget).siblings('span').css('color', '#828282');
            $(e.currentTarget).siblings('span').html(value);
        }
        else {
            $(e.currentTarget).siblings('span').css('color', '#E1E1E1');
            $(e.currentTarget).siblings('span').html("( 이름 )");
            value = ""
        }

        const costId = $(e.currentTarget).parent().siblings('input').val();
        costs.get(Number(costId)).payer = value;
    })
    $(document).on("keyup", ".cost_payer input", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).css('visibility', 'hidden');
            $(e.currentTarget).siblings('span').css('visibility', 'visible');
            let value = $(e.currentTarget).val();
            if(value!=="") {
                $(e.currentTarget).siblings('span').css('color', '#828282');
                $(e.currentTarget).siblings('span').html(value);
            }
            else {
                $(e.currentTarget).siblings('span').css('color', '#E1E1E1');
                $(e.currentTarget).siblings('span').html("( 이름 )");
                value = ""
            }

            const costId = $(e.currentTarget).parent().siblings('input').val();
            costs.get(Number(costId)).payer = value;
        }   
    })
    $(document).on("blur", ".cost_amount input", function(e) {
        $(e.currentTarget).css('visibility', 'hidden');
        $(e.currentTarget).siblings('span').css('visibility', 'visible');
        let value = $(e.currentTarget).val();
        var regex = /^[0-9]*$/
        if(value!=="" && regex.test(value)) {
            $(e.currentTarget).siblings('span').css('color', '#828282');
            $(e.currentTarget).siblings('span').html(value.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")+"원");
        }
        else {
            $(e.currentTarget).siblings('span').css('color', '#828282');
            $(e.currentTarget).siblings('span').html("예상 금액");
            $(e.currentTarget).val('');
            value = 0;
        }
        const costId = $(e.currentTarget).parent().siblings('input').val();
        costs.get(Number(costId)).amount = value;
    })
    $(document).on("keyup", ".cost_amount input", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).css('visibility', 'hidden');
            $(e.currentTarget).siblings('span').css('visibility', 'visible');
            let value = $(e.currentTarget).val();
            var regex = /^[0-9]*$/
            if(value!=="" && regex.test(value)) {
                $(e.currentTarget).siblings('span').css('color', '#828282');
                $(e.currentTarget).siblings('span').html(value.replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")+"원");
            }
            else {
                $(e.currentTarget).siblings('span').css('color', '#828282');
                $(e.currentTarget).siblings('span').html("예상 금액");
                $(e.currentTarget).val('');
                value = 0;
            }
            const costId = $(e.currentTarget).parent().siblings('input').val();
            costs.get(Number(costId)).amount = value;
        }   
    })
})

// 일정 짜기
// 기간 불러오기
function bindPeriod() {
    const dates = $('#daterangepicker').val();
    $('.period').html(dates);
}
// 일별 토글 불러오기
function bindDates() {
    $('.schedules').html('');
    for(const element of dateArr) {
        const dayNum = new Date(element).getDay();
        const day = dayOfWeek[dayNum];
        const date = element.split('-')[1]+'.'+element.split('-')[2];

        let str = "";
        str += `<div class="schedule">
                    <input type="hidden" value="${element}">
                    <div>
                        <div class="schedule_info">
                            <div class="schedule_day">
                                <span>${day}</span>
                            </div>
                            <div class="schedule_date">
                                <span>${date}</span>
                            </div>
                            <div class="schedule_cost">
                                <span></span>
                            </div>
                            <div class="schedule_toggle">
                                <img src="/images/toggle_up.png" alt="">
                            </div>
                        </div>
                        <span class="schedule_separator"></span>
                    </div>
                    <div class="itineraries">
                        ${bindItinerariesByDate(element)}
                    </div>
                    <div class="itinerary_add">
                        <div>
                            <img src="/images/add.png" alt="">
                        </div>
                        <span>일정 추가</span>
                    </div>
                </div>`
        $('.schedules').append(str);
    }
}
// 날짜에 따라 일정 바인딩
function bindItinerariesByDate(date) {
    let str = "";

    const arrItineraries = [...itineraries.entries()];
    arrItineraries.sort((a, b) => {
        if (a[1].date < b[1].date) return -1;
        if (a[1].date > b[1].date) return 1;
        if (a[1].startTime < b[1].startTime) return -1;
        if (a[1].startTime > b[1].startTime) return 1;
        if (a[1].endTime < b[1].endTime) return -1;
        if (a[1].endTime > b[1].endTime) return 1;
        return 0;
    });
    itineraries = new Map(arrItineraries);
    for(const [itinerary_key, itinerary_value] of itineraries) {
        if(date === itinerary_value.date) {
            const pId = itinerary_value.placeId;
            const pName = !pId ? "장소명" : selectedPlaces.get(pId).name;
            const pCity = !pId ? "도시명" : selectedPlaces.get(pId).city;
            const color = !pId ? "#00A538" : selectedPlaces.get(pId).color;
            const startTime = parseTimeKor(itinerary_value.startTime);
            const endTime = parseTimeKor(itinerary_value.endTime);

            str += `<div>
                        <div class="itinerary">
                            <input type="hidden" value="${itinerary_key}">
                            <span class="itinerary_type" style="background-color:${color}"></span>
                            <div class="itinerary_info">
                                <span class="itinerary_place">${pName}</span>
                                <span class="itinerary_destination">${pCity}</span>
                            </div>
                            <div class="itinerary_times">
                                <span class="itinerary_start_time">${startTime}</span>
                                <span>~</span>
                                <span class="itinerary_end_time">${endTime}</span>
                            </div>
                            <div class="itinerary_remove">
                                <img src="/images/remove.png" alt="">
                            </div>
                        </div>
                    </div>`;
        }
    }
    return str;
}
// 날짜 파싱
function parseTimeKor(time) {
    const arr = time.split(":");
    const hour = arr[0];
    const minute = arr[1];

    return `${hour}시 ${minute}분`
}

// 일정 추가
function addItinerary(e) {
    // 일정 화면에 추가
    let str = '';
    str += `<div>
                <div class="itinerary">
                    <input type="hidden" value="${itineraryKey}">
                    <span class="itinerary_type"></span>
                    <div class="itinerary_info">
                        <span class="itinerary_place">장소명</span>
                        <span class="itinerary_destination">도시명</span>
                    </div>
                    <div class="itinerary_times">
                        <span class="itinerary_start_time">${parseTimeWithDayAndNight(new Date())}</span>
                        <span>~</span>
                        <span class="itinerary_end_time">${parseTimeWithDayAndNight(new Date())}</span>
                    </div>
                    <div class="itinerary_remove">
                        <img src="/images/remove.png" alt="">
                    </div>
                </div>
            </div>`;
    $(e.currentTarget).prev().append(str);

    // 일정 map에 일정 객체 추가
    itineraries.set(itineraryKey, {});

    const itinerary = itineraries.get(itineraryKey);
    itinerary.itineraryKey = itineraryKey;
    const now = new Date();
    const hour = String(now.getHours()).padStart(2, '0');
    const minute = String(now.getMinutes()).padStart(2, '0');
    itinerary.startTime = `${hour}:${minute}:00`;
    itinerary.endTime = `${hour}:${minute}:00`;
    itinerary.lastKeyword = '';
    itineraryKey--;
}
// 일정 삭제
function removeItinerary(e) {
    const itineraryId = $(e.currentTarget).siblings('input').val();
    itineraries.delete(Number(itineraryId));
    // 현재 일정객체라면 일정관리탭 종료
    if(lastItineraryEvent!==null) {
        const eId = $(lastItineraryEvent).children('input').val();
        if(itineraryId===eId) {
            schedule_manage.css('visibility', 'hidden');
        }
    }

    $(e.currentTarget).parent().parent().remove();
}
// 일정 날짜 설정
function addItineraryDate(e) {
    const date = $(e.currentTarget).siblings('input').val();
    const itineraryId = $(e.currentTarget).prev().children().last().find('input').val();
    itineraries.get(Number(itineraryId)).date = date;
}
// 검색 장소 선택하여 ID 가져오기
function bindIdBySelectedPlace(placeId, lastItineraryEvent) {
    let str = "";   
    str += `<input type="hidden" value="${placeId}">`;
    $(lastItineraryEvent).prepend(str);
}
// 검색 장소 선택시 객체에 추가
function addItineraryPlace(itinerary, placeId) {
    const itineraryId = $(itinerary).children('input').val();
    itineraries.get(Number(itineraryId)).placeId = placeId;
}
// 검색 장소 선택시 장소 event 객체를 통해 일정 이름 주소 등 바인딩
function bindItineraryPlace(itinerary) {
    const $itinerary = $(itinerary);
    const itineraryId = $itinerary.children('input').val();
    const id = itineraries.get(Number(itineraryId)).placeId;
    const type = getColorByPlaceType(selectedPlaces.get(id).type);
    const name = selectedPlaces.get(id).name;
    const address = `${selectedPlaces.get(id).country} ${selectedPlaces.get(id).city}`;

    $itinerary.children('.itinerary_type').css("background-color", type);
    $itinerary.find('.itinerary_place').html(name);
    $itinerary.find('.itinerary_destination').html(address);
}
// 검색 목록 바인딩
function searchSelectedPlaces(keyword) {
    $('.schedule_place').children('.place_list').html('');
    for(const value of selectedPlaces.values()) {
       if(value.name.indexOf(keyword)!=-1 ||
        value.city.indexOf(keyword)!=-1 ||
        value.country.indexOf(keyword)!=-1 ||
        value.continent.indexOf(keyword)!=-1 ||
        value.type.indexOf(keyword)!=-1) {
            let str = "";
            if($('.schedule_manage').children('.place_list').children().length > 0) {
                str += '<span class="place_separator"></span>';
            }
            str +=	`<div class="place">
                        <input type="hidden" value="${value.id}"></input>
                        <div class="place_img">
                            <img src="${value.photo}" alt="">
                        </div>
                        <div class="place_info">
                            <div class="place_name">
                                <span>${value.name}</span>
                                <div class="place_type" style="background-color:${getColorByPlaceType(value.type)}"><span>${value.type}</span></div>
                            </div>
                            <div class="place_ratings">
                                <div class="img_rating">
                                    <img src="/images/rating.png" alt="">
                                </div>
                                <span class="place_rating">${value.rating}</span>
                                <span class="place_ratings_total">(${value.ratingCnt})</span>
                            </div>
                            <span class="place_address">${value.country} ${value.city}</span>
                        </div>
                        <div class="btn btn_add_place_schedule">
                            <span>선&nbsp&nbsp택</span>
                        </div>
                    </div>`;
            $('.schedule_place').children('.place_list').append(str);
        }
    }
}
// 일정시간 추가
function addItineraryTime(itinerary, time, key) {
    if(itinerary !== null) {
        // "12:00:00"
        const timeStr = parseTime(time);
    
        const itineraryId = $(itinerary).children('input').val();
        // 일정맵에 넣기
        if(key==="startTime") {
            itineraries.get(Number(itineraryId)).startTime = timeStr;
        } else {
            itineraries.get(Number(itineraryId)).endTime = timeStr;
        }
    }
}
// 일정 시간 바인딩
function bindItineraryTime(itinerary, time, key) {
    if(itinerary !== null) {
        const timeStr = parseTimeWithDayAndNight(time)
    
        if(key==="startTime") {
            $(itinerary).find('.itinerary_start_time').html(timeStr);
        } else {
            $(itinerary).find('.itinerary_end_time').html(timeStr);
        }
    }
}
// 비용 추가
function addCost(e) {
    let str = '';
    str += `<div class="cost">
                <input type="hidden" value="${costKey}">
                <span class="cost_separator"></span>
                <div class="cost_name">
                    <span>항목명</span>
                    <input type="text">
                </div>
                <div class="cost_payer">
                    <span>( 이름 )</span>
                    <input type="text">
                </div>
                <div class="cost_amount">
                    <span>예상 금액</span>
                    <input type="text">
                </div>
                <div class="cost_remove">
                    <img src="/images/remove.png" alt="">
                </div>
            </div>`;
    $(e.currentTarget).prev().append(str);

    // 비용 객체 초기화
    costs.set(costKey, {});
    const cost = costs.get(Number(costKey));
    const itineraryId = $(lastItineraryEvent).children('input').val();
    cost.itineraryId = itineraryId;
    cost.costId = costKey;
    cost.content = "";
    cost.payer = "";
    cost.amount = 0;
    
    costKey--;
    console.log("추가 : " + JSON.stringify(cost));
}
// 비용 삭제
function removeCost(e) {
    const costId = $(e.currentTarget).siblings('input').val();
    costs.delete(Number(costId));
    $(e.currentTarget).parent().remove();
}

// 저장하기 ================================================================
$(function() {
    $('.btn_save').click(function() {
        const selectedDefaultDestinationsObj = Object.fromEntries(selectedDefaultDestinations);
        const selectedDatesObj = Object.fromEntries(selectedDates);
        const selectedPlacesObj = Object.fromEntries(selectedPlaces);
        const itinerariesObj = Object.fromEntries(itineraries);
        const costsObj = Object.fromEntries(costs);

        closeInviteSocketConnection();

        $.ajax({
                url : "/plan/write",
                type : "POST",
                contentType : "application/json",
                data : JSON.stringify({
					"planId": planId, 
                    "selectedDefaultDestinations": selectedDefaultDestinationsObj, 
                    "selectedDestinations": [...selectedDestinations], 
                    "selectedDates": selectedDatesObj, 
                    "selectedPlaces": selectedPlacesObj, 
                    "itineraries": itinerariesObj, 
                    "costs": costsObj}),
                success : function(data) {
                    window.location.replace("/user/myPlan");
                }
        })
    })
})