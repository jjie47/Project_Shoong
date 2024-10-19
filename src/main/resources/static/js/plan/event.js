// tab
// contents의 각 tab 변수
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
// 일정 버튼
const btn_schedule = $('.btn_schedule');
const btn_save = $('.btn_save');

// 지도 커버
const cover = $('.cover');

// 도시 및 나라 선택
// 대륙 카테고리 버튼
const continentCategories = $('.continents').children('button');
const world = $('.continents').children('button:eq(0)');
const southeast_asia = $('.continents').children('button:eq(1)');
const asia = $('.continents').children('button:eq(2)');
const oceania = $('.continents').children('button:eq(3)');
const europe = $('.continents').children('button:eq(4)');
const america = $('.continents').children('button:eq(5)');
let continent = '전체';

// index - tab 전환시 커서 변경 이벤트
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
    // header - hover, tab 이동
    $('header').find('a').hover(function() {
        $('#contents').children('.holder').css('left','-247px');
    }, function() {
        $('#contents').children('.holder').css('left','-361px');
    })

    // tab
    // index - hover, 밑줄
    plan_indexes.hover(function() {
        $(this).children().css('text-decoration-line', 'underline')
    }, function() {
        $(this).children().css('text-decoration-line', 'none')
    })

    // index - click, tab 전환
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
    })
    btn_schedule.click(function() {
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
        bindDates();
    })

    // 그룹 초대
    // 초대 버튼 - click
    $(document).on("click", ".btn_invite", function(e) {
        $(e.currentTarget).addClass("btn_clicked");
		console.log("click");
        $(this).off("click");
    })

    // 도시 및 나라 선택
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


	// 장소 선택
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
    // let lastItineraryEvent = null;       =>      timepicker 쪽으로 이동
    $(document).on("click", ".itinerary", function(e){
        let iId;
        if(lastItineraryEvent!=null || lastItineraryEvent!=undefined) {
            iId = $(lastItineraryEvent).children('input').val();
            let value = $('#schedule_search_place').val();
            itineraries.get(Number(iId)).lastKeyword = (value==null || value==undefined) ? '' : value;

            const startTime = parseTimeValueToTime(itineraries.get(Number(iId)).startTime);
            const endTime = parseTimeValueToTime(itineraries.get(Number(iId)).endTime);
            $('.time_choose').find('input:eq(0)').val(startTime);
            $('.time_choose').find('input:eq(1)').val(endTime);
        }

        $('.schedule_manage').css('visibility', 'visible');
        // e를 .schedule_manage에 전달
        lastItineraryEvent = e.currentTarget;
        iId = $(lastItineraryEvent).children('input').val();
        const keyword = itineraries.get(Number(iId)).lastKeyword;
        $('#schedule_search_place').val(keyword);
        searchSelectedPlaces(keyword);
    })
    // $(document).click(function(e) {
    //     if(!$(e.target).closest('section:first-of-type, .itinerary, .schedule_manage, .ui-timepicker-container').length) {
    //         $('.schedule_manage').css('visibility', 'hidden');
    //     }
    // })
    // $('#map').mousedown(function() {
    //     $('.schedule_manage').css('visibility', 'hidden');
    // })
    // 일정 추가 - click
    $(document).on("click", ".itinerary_add", function(e) {
        addItinerary(e);
        addItineraryDate(e);
        searchSelectedPlaces('');
        
        
        $('#schedule_search_place').val('');
        $('.schedule_place').children('.place_list').html('');
        
        // 현재 관리 일정
        $(e.currentTarget).siblings('.itineraries').children().last().children('.itinerary').get(0).click();
        $('.time_choose').find('input').val(parseTimeWithDayAndNight(new Date()));
    })
    // 일정 삭제 - click
    $(document).on("click", ".itinerary_remove", function(e) {
        removeItinerary(e);
    })

    // 일정 관리
    // 장소 설정
    // 장소 검색
    $('#schedule_search_place').keyup(function (e){
        console.log("장소 검색!!");
        console.log(selectedPlaces);
        let keyword = $(this).val();
        keyword = keyword.length < 1 ? '': keyword;
        console.log("장소 검색 : " + keyword);
        searchSelectedPlaces(keyword);
    })
    // 장소 선택
    $(document).on("click", ".btn_add_place_schedule", function(e) {
        const placeId = $(e.currentTarget).siblings('input').val();
        // bindIdBySelectedPlace(placeId, lastItineraryEvent);
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
    $(document).on("click", ".cost_name, .cost_payer, .cost_amount", function(e) {
        $(e.currentTarget).children('input').css('visibility', 'visible');
        $(e.currentTarget).children('input').focus();
        $(e.currentTarget).children('span').css('visibility', 'hidden');
    })
    $(document).on("blur", ".cost_name", function(e) {
        $(e.currentTarget).children('input').css('visibility', 'hidden');
        $(e.currentTarget).children('span').css('visibility', 'visible');
        const value = $(e.currentTarget).children('input').val();
        $(e.currentTarget).children('span').html(value);
        addCostContent(e);
    })
    $(document).on("keyup", ".cost_name", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).children('input').css('visibility', 'hidden');
            $(e.currentTarget).children('span').css('visibility', 'visible');
            const value = $(e.currentTarget).children('input').val();
            $(e.currentTarget).children('span').html(value);
            addCostContent(e);
        }
    })
    $(document).on("blur", ".cost_payer", function(e) {
        $(e.currentTarget).children('input').css('visibility', 'hidden');
        $(e.currentTarget).children('span').css('visibility', 'visible');
        const value = $(e.currentTarget).children('input').val();
        if(value.length > 0) {
            $(e.currentTarget).children('span').css('color', '#828282');
            $(e.currentTarget).children('span').html(value);
        }
        else {
            $(e.currentTarget).children('span').css('color', '#E1E1E1');
            $(e.currentTarget).children('span').html("( 이름 )");
        }
        addCostPayer(e);
    })
    $(document).on("keyup", ".cost_payer", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).children('input').css('visibility', 'hidden');
            $(e.currentTarget).children('span').css('visibility', 'visible');
            const value = $(e.currentTarget).children('input').val();
            if(value.length > 0) {
                $(e.currentTarget).children('span').css('color', '#828282');
                $(e.currentTarget).children('span').html(value);
            }
            else {
                $(e.currentTarget).children('span').css('color', '#E1E1E1');
                $(e.currentTarget).children('span').html("( 이름 )");
            }
            addCostPayer(e);
        }   
    })
    $(document).on("blur", ".cost_amount", function(e) {
        $(e.currentTarget).children('input').css('visibility', 'hidden');
        $(e.currentTarget).children('span').css('visibility', 'visible');
        const value = $(e.currentTarget).children('input').val();
        if(value.length > 0) {
            $(e.currentTarget).children('span').css('color', '#828282');
            $(e.currentTarget).children('span').html(value+"원");
        }
        else {
            $(e.currentTarget).children('span').css('color', '#828282');
            $(e.currentTarget).children('span').html("예상 금액");
        }
        addCostAmount(e);
    })
    $(document).on("keyup", ".cost_amount", function(e) {
        if(e.keyCode===13) {
            $(e.currentTarget).children('input').css('visibility', 'hidden');
            $(e.currentTarget).children('span').css('visibility', 'visible');
            const value = $(e.currentTarget).children('input').val();
            if(value.length > 0) {
                $(e.currentTarget).children('span').css('color', '#828282');
                $(e.currentTarget).children('span').html(value+"원");
            }
            else {
                $(e.currentTarget).children('span').css('color', '#828282');
                $(e.currentTarget).children('span').html("예상 금액");
            }
            addCostAmount(e);
        }   
    })


    $('.btn_save').click(function() {
        console.log(selectedDefaultDestinations);
        console.log(selectedDestinations);
        console.log(selectedDates);
        console.log(selectedPlaces);
        console.log(itineraries);
        console.log(itineraryKey);
        console.log(costs);
        console.log(costKey);
    })
    $('.btn_save').click(function() {
        const selectedDefaultDestinationsObj = Object.fromEntries(selectedDefaultDestinations);
        const selectedDatesObj = Object.fromEntries(selectedDates);
        const selectedPlacesObj = Object.fromEntries(selectedPlaces);
        const itinerariesObj = Object.fromEntries(itineraries);
        const costsObj = Object.fromEntries(costs);
        console.log(JSON.stringify({"selectedDefaultDestinations": selectedDefaultDestinationsObj})); 
        console.log(JSON.stringify({"selectedDestinations":  [...selectedDestinations]})); 
        console.log(JSON.stringify({"selectedDates": selectedDatesObj})); 
        console.log(JSON.stringify({"selectedPlaces": selectedPlacesObj})); 
        console.log(JSON.stringify({"itineraries": itinerariesObj})); 
        console.log(JSON.stringify({"costs": costsObj})); 
        $.ajax({
				url : "/plan/write",
				type : "POST",
				contentType : "application/json",
				data : JSON.stringify({
                    "selectedDefaultDestinations": selectedDefaultDestinationsObj, 
                    "selectedDestinations": [...selectedDestinations], 
                    "selectedDates": selectedDatesObj, 
                    "selectedPlaces": selectedPlacesObj, 
                    "itineraries": itinerariesObj, 
                    "costs": costsObj}),
				success : function(data) {
					window.location.href = "/user/myPlan"
				}
        })
    })
})



