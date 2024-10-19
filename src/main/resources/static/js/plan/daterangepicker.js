$(function () {
    $('#daterangepicker').daterangepicker({
        // "startDate": formattedDate,
        // "endDate": formattedDate,
        "locale": {
            "format": "YYYY년 MM월 DD일",
            "separator": "  ~  ",
            "applyLabel": "적용",
            "cancelLabel": "취소",
            "fromLabel": "From",
            "toLabel": "To",
            "customRangeLabel": "Custom",
            "weekLabel": "W",
            "daysOfWeek": [
                "일",
                "월",
                "화",
                "수",
                "목",
                "금",
                "토"
            ],
            "monthNames": [
                "1월",
                "2월",
                "3월",
                "4월",
                "5월",
                "6월",
                "7월",
                "8월",
                "9월",
                "10월",
                "11월",
                "12월"
            ],
            "firstDay": 1
        },
        "alwaysShowCalendars": true,
        "parentEl": ".box_date",
        "applyButtonClasses": "btn_apply_drp",
        "cancelClass": "btn_default_drp"
    }, function(start, end, label) {
        addDate(start.format('YYYY-MM-DD'), end.format('YYYY-MM-DD'));
      console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
        
       // 초기화
        itineraries.clear();
        itineraryKey = -1;
        costs.clear();
        costKey = -1;
    });
});