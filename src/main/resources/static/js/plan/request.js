// 그룹 초대 - 요청
// 사용자 검색
function searchUser() {
    const keyword = $('#search_user').val();
    if (keyword.length > 0) {
        $.ajax({
            url: "/user/search",
            type: "GET",
            data: {"keyword": keyword},
            success: function (userList) {
                $('#userList').replaceWith(userList);
            }
        })
    }
    else {
        $('#userList').html('');
    }
}

// 그룹원 목록
window.onload = getGroupList();
// member_index.trigger("click");
function getGroupList() {
    // const planId = /*[[${planId}]]*/'';
    // $.ajax({
    //     url: "/group/list",
    //     type: "GET",
    //     data: {"planId": planId},
    //     success: function (groupMemberList) {
    //         $('#groupMemberList').replaceWith(groupMemberList);
    //     }
    // })
}



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

// 날짜 선택
// 날짜 추가
function addDate(start, end) {
    console.log("이게 불러진다ㅗㄱ??")
    selectedDates.set("startDate", start);
    selectedDates.set("endDate", end);
    
    dateArr = getDatesStartToLast(start, end);
}
// 날짜 초기화
addDate(timeEx, timeEx);

// dateMap 초기화
// function initDateMap() {
//     const map = new Map();

//     for(const element of dateArr) {
//         map.set(element, )
//     }
// }

// 날짜 불러오기


// 사이 모든 날짜 배열로 뽑기
function getDatesStartToLast(startDate, endDate) {
	var regex = RegExp(/^\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/);
	if(!(regex.test(startDate) && regex.test(endDate))) return "Not Date Format";
	var result = [];
	var curDate = new Date(startDate);
	while(curDate <= new Date(endDate)) {
        console.log(curDate)
        console.log(curDate.toISOString())
        console.log(curDate.toISOString().split("T")[0])
		result.push(curDate.toISOString().split("T")[0]);
		curDate.setDate(curDate.getDate() + 1);
	}
	return result;
}

// 장소 선택
// 장소 추가
function addPlace(place) {
    console.log(place.id);
    console.log(place);
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
    console.log(selectedDestinations);
}

// 장소 삭제
function removePlace(id) {
    selectedPlaces.delete(id);
    removeSelectedPlace(id);
    deselectSearchedPlace(id);
    deleteSelectedMarker(id);
}

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
    // itineraries.get(date).push(1);
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

    // itineraries.set(itineraryKey, new Map());
    itineraries.set(itineraryKey, {});

    const itinerary = itineraries.get(itineraryKey);
    console.log(itineraryKey);
    const now = new Date();
    const hour = String(now.getHours()).padStart(2, '0');
    const minute = String(now.getMinutes()).padStart(2, '0');
    itinerary.startTime = `${hour}:${minute}:00`;
    itinerary.endTime = `${hour}:${minute}:00`;
    // $('.time_choose').find(span).html()
    itinerary.lastKeyword = '';
    
    // // addCost
    // str = '';
    // str += `<div class="cost">
    //             <input type="hidden" value="${costKey}">
    //             <span class="cost_separator"></span>
    //             <div class="cost_name">
    //                 <span>항목명</span>
    //                     <input type="text">
    //                 </div>
    //                 <div class="cost_payer">
    //                     <span>( 이름 )</span>
    //                     <input type="text">
    //                 </div>
    //                 <div class="cost_amount">
    //                     <span>예상 금액</span>
    //                     <input type="text">
    //                 </div>
    //             <div class="cost_remove">
    //                 <img src="/images/remove.png" alt="">
    //             </div>
    //         </div>`;
    // $('.cost_list').append(str);
    // costs.get(Number(costKey)).set("itineraryId", itineraryKey);



    itineraryKey--;

}
// 일정 삭제
function removeItinerary(e) {
    const itineraryId = $(e.currentTarget).siblings('input').val();
    itineraries.delete(Number(itineraryId));

    $(e.currentTarget).parent().parent().remove();
}
// 일정 날짜 설정
function addItineraryDate(e) {
    const date = $(e.currentTarget).siblings('input').val();
    const itineraryId = $(e.currentTarget).prev().children().last().find('input').val();
    // itineraries.get(Number(itineraryId)).set("date", date);
    console.log(date)
    itineraries.get(Number(itineraryId)).date = date;
}
// 검색 장소 선택하여 ID 가져오기
function bindIdBySelectedPlace(placeId, lastItineraryEvent) {
    let str = "";   
    str += `<input type="hidden" value="${placeId}">`;
    $(lastItineraryEvent).prepend(str);
}
function addItineraryPlace(itinerary, placeId) {
    const itineraryId = $(itinerary).children('input').val();
    // itineraries.get(Number(itineraryId)).set("placeId", placeId)
    itineraries.get(Number(itineraryId)).placeId = placeId;
}
// 선택 장소 ID를 통해 일정 이름 주소 등 바인딩
function bindItineraryPlace(itinerary) {
    const $itinerary = $(itinerary);
    const itineraryId = $itinerary.children('input').val();
    // const id = itineraries.get(Number(itineraryId)).get("placeId");
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
        console.log("value : " + value);
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
    // "12:00:00"
    const timeStr = parseTime(time);

    const itineraryId = $(itinerary).children('input').val();
    // 일정맵에 넣기
    // itineraries.get(Number(itineraryId)).set(key, timeStr);
    if(key==="startTime") {
        itineraries.get(Number(itineraryId)).startTime = timeStr;
        
    } else {
        itineraries.get(Number(itineraryId)).endTime = timeStr;
    }
}
// 일정 시간 바인딩
function bindItineraryTime(itinerary, time, key) {
    const timeStr = parseTimeWithDayAndNight(time)

    if(key==="startTime") {
        $(itinerary).find('.itinerary_start_time').html(timeStr);
    } else {
        $(itinerary).find('.itinerary_end_time').html(timeStr);
    }
}
// 비용 추가
function addCost(e) {
    // itineraries.get(date).push(1);
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

    // costs.set(costKey, new Map());
    costs.set(costKey, {});

    const itineraryId = $(lastItineraryEvent).children('input').val();
    
    // costs.get(Number(costKey)).set("itineraryId", itineraryId);
    costs.get(Number(costKey)).itineraryId = itineraryId;
    costKey--;
    console.log(costs);
}
// 비용 삭제
function removeCost(e) {
    const costId = $(e.currentTarget).siblings('input').val();
    costs.delete(Number(costId));

    $(e.currentTarget).parent().remove();
}
function addCostContent(e) {
    const costId = $(e.currentTarget).siblings('input').val();
    const value = $(e.currentTarget).children('input').val();
    console.log(costs.get(Number(costId)));
    // costs.get(Number(costId)).set("content", value);
    costs.get(Number(costId)).content = value;
}
function addCostPayer(e) {
    const costId = $(e.currentTarget).siblings('input').val();
    const value = $(e.currentTarget).children('input').val();
    // costs.get(Number(costId)).set("payer", value);
    costs.get(Number(costId)).payer = value;
}
function addCostAmount(e) {
    const costId = $(e.currentTarget).siblings('input').val();
    const value = $(e.currentTarget).children('input').val();
    // costs.get(Number(costId)).set("amount", Number(value));
    costs.get(Number(costId)).amount = value;
}