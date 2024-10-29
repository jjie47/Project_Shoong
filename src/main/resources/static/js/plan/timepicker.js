$(function () {
    $('#startTime').timepicker({
        timeFormat: 'HH:mm p',
        interval: 30,
        minTime: '0',
        maxTime: '23:50 pm',
        defaultTime: 'now',
        dynamic: false,
        dropdown: true,
        // startTime: new Date(0,0,0,10,0,0),
        scrollbar: false,
        change: function(time) {
            addItineraryTime(lastItineraryEvent, time, "startTime");
            bindItineraryTime(lastItineraryEvent, time, "startTime");

            const startTime = $('#startTime').val();
            const endTime = $('#endTime').val();

            // 시작 시간이 더 빠른 경우
            if(lastItineraryEvent!==null) {
                const iId = $(lastItineraryEvent).children('input').val();
                if(compareTime(startTime, endTime)==1) {
                    $('#endTime').parent().css({
                        "border-color":"#f00",
                        "transition-duration":"1s"
                    });
                    $('#endTime').css({
                        "color":"#f00",
                        "transition-duration":"1s"
                    });
                    setTimeout(function() {
                        $('#endTime').parent().css("border-color","#E1E1E1");
                        $('#endTime').css("color","#161616");
                        $('#endTime').val(startTime);
                        $(lastItineraryEvent).find('.itinerary_end_time').html(parseTimeToText(startTime));
                        // const iId = $(lastItineraryEvent).children('input').val();
                        console.log("확인"+iId)
                        itineraries.get(Number(iId)).endTime = parseTimeToColonTime(startTime);
                    },500)
                }
            }
        }
    });
    $('#endTime').timepicker({
        timeFormat: 'HH:mm p',
        interval: 30,
        minTime: '0',
        maxTime: '23:50 pm',
        defaultTime: 'now',
        dynamic: false,
        dropdown: true,
        // startTime: new Date(0,0,0,14,0,0),
        scrollbar: false,
        change: function(time) {
            addItineraryTime(lastItineraryEvent, time, "endTime");
            bindItineraryTime(lastItineraryEvent, time, "endTime");

            const startTime = $('#startTime').val();
            const endTime = $('#endTime').val();

            // 종료 시간이 더 늦는 경우
            if(lastItineraryEvent!==null) {
                if(compareTime(startTime, endTime)==1) {
                    $('#endTime').parent().css({
                        "border-color":"#f00",
                        "transition-duration":"1s"
                    });
                    $('#endTime').css({
                        "color":"#f00",
                        "transition-duration":"1s"
                    });
                    setTimeout(function() {
                        $('#endTime').parent().css("border-color","#E1E1E1");
                        $('#endTime').css("color","#161616");
                        $('#endTime').val(startTime);
                        $(lastItineraryEvent).find('.itinerary_end_time').html(parseTimeToText(startTime));
                        const iId = $(lastItineraryEvent).children('input').val();
                        itineraries.get(Number(iId)).endTime = parseTimeToColonTime(startTime);
                    },500)
                }
            }
        }
    });
});
function startTimeNumber() {
    const value = $('#startTime').val();
    const after = value.substring(0,2) + value.substring(3,5);
    console.log(Number(after))
    return Number(after);
}
function endTimeNumber() {
    const value = $('#endTime').val();
    const after = value.substring(0,2) + value.substring(3,5);
    console.log(Number(after))
    return Number(after);
}
function parseTime(time) {
    return time.toString().split(" ")[4];
}

function parseTimeWithDayAndNight(time) {
    const timeStr = parseTime(time).split(":");
    const hour = timeStr[0];
    const minute = timeStr[1];
    // let dayOrNight;
    // if(hour>12) {
    //     dayOrNight = "오후"
    // }
    // else {
    //     dayOrNight = "오전"
    // }
    return `${hour}시 ${minute}분`;
}
function parseTimeToNum(time) {
    const hour = String(time).slice(0, 2);
    const minute = String(time).slice(3, 5);
    return Number(hour+minute);
}
function parseTimeToText(time) {
    const hour = time.slice(0, 2);
    const minute = time.slice(3, 5);
    return `${hour}시 ${minute}분`;
}
function parseNumToTime(num) {
    let c;
    if(num>1130) c = 'PM';
    else c = 'AM';

    console.log(num);
    let str = String(num);
    console.log(str);
    if(str.length==1) str = '000'+str;
    if(str.length==2) str = '00'+str;
    if(str.length==3) str = '0'+str;
    return `${str.slice(0, 2)}:${str.slice(2)} ${c}`;
}
function parseTimeValueToTime(value) {
    const str = value.split(":");
    const num = Number(str[0] + str[1]);
    return parseNumToTime(num);
}
function parseTimeToColonTime(value) {
    const hour = value.slice(0, 2);
    const minute = value.slice(3, 5);
    return `${hour}:${minute}:00`;
}
function compareTime(a, b) {
    const c = parseTimeToNum(a);
    const d = parseTimeToNum(b);
    if(c>d) return 1;
    if(c<d) return -1;
    return 0;
}