// 선택한 목적지
let selectedDefaultDestinations = new Map();
let selectedDestinations = new Set();
let destinationKey = -1;
// 선택한 날짜 - key = "startDate", "endDate"
let selectedDates = new Map();
// 선택한 장소 - key: placeId
let selectedPlaces = new Map();
// 일정 - key: itineraryKey
let itineraries = new Map();
let itineraryKey = -1;
// 비용 - key: costKey
let costs = new Map();
let costKey = -1;

// 이전 일정 이벤트 객체
var lastItineraryEvent = null;

// 현재 시간과 포맷
const currentDate = new Date();
const formattedDate = `${currentDate.getFullYear()}년 ${currentDate.getMonth()+1}월 ${currentDate.getDate()}일`;
const timeEx = `${currentDate.getFullYear()}-${currentDate.getMonth()+1}-${currentDate.getDate()}`;
// 일별 배열
let dateArr = [];
// 요일 상수
const dayOfWeek = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];

