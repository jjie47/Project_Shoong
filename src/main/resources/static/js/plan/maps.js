// Maps Javascript API 
// Load API - Boot strap loader snippet
(g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=`https://maps.${c}apis.com/maps/api/js?`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
	key: "AIzaSyBlsHZu6Fki84Gnkb7zLfgX0XVIjrMnp0g",
	v: "weekly",
	language: "ko"
	// region: "KR"
	// Use the 'v' parameter to indicate the version to use (weekly, beta, alpha, etc.).
	// Add other bootstrap parameters as needed, using camel case.
});

// Initialize and add the map, marker
let map;
let markers = [];
let selectedMarkers = new Map();

async function initMap() {
  // The location of Incheon International Airport
  const init = { lat: 37.3601908, lng: 126.4406957 };
  const incheonAirport = { lat: 37.4601908, lng: 126.4406957 };
  // Request needed libraries.
  //@ts-ignore
  const { Map } = await google.maps.importLibrary("maps");
  // The map, centered at Uluru
	map = new Map(document.getElementById("map"), {
		zoom: 10,
		center: init,
		mapId: "DEMO_MAP_ID",
		disableDefaultUI: true // 기본 UI 설정
	});

	addMarker(markers, incheonAirport, "인천국제공항");
}

// Adds a marker to the map and push to the array.
async function addMarker(arr, position, title) {
	// Request needed libraries.
	//@ts-ignore
	const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

	// 추가한 경우, 파란 마커 추가
	if(arr == selectedMarkers) {
		const pinElement = new PinElement({
			background: "#2173ca",
			borderColor: "#0050a5",
			glyphColor: "#0050a5"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		arr.push(marker);
	}
	// 검색한 경우, 기본 마커 추가
	else{
		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title
		  });
		arr.push(marker);
	}
  }
  
  // Sets the map on all markers in the array.
  function setMapOnAll(arr, map) {
	for (let i = 0; i < markers.length; i++) {
		arr[i].setMap(map);
	}
  }
  
  // Removes the markers from the map, but keeps them in the array.
  function hideMarkers(arr) {
	setMapOnAll(arr, null);
  }
  
  // Shows any markers currently in the array.
  function showMarkers(arr) {
	setMapOnAll(arr, map);
  }
  
  // Deletes all markers in the array by removing references to them.
  function deleteMarkers(arr) {
	hideMarkers(arr);
	arr = [];
  }

// 선택한 장소 마커 메서드
// Adds a marker to the map and push to the array.
async function addSelectedMarker(id, position, title) {
	// Request needed libraries.
	//@ts-ignore
	const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

	const pinElement = new PinElement({
		background: "#2173ca",
		borderColor: "#0050a5",
		glyphColor: "#0050a5"
	})

	const marker = new AdvancedMarkerElement({
		map,
		position: position,
		title: title,
		content: pinElement.element
	});
	selectedMarkers.set(id, marker);
  }
  // Sets the map on all markers in the array.
  function setMapOnAllSelectedMarkers(map) {
	for (const marker of selectedMarkers.values()) {
		marker.setMap(map);
	}
  }
  // Removes the markers from the map, but keeps them in the array.
  function hideSelectedMarker(id) {
	selectedMarkers.get(id).setMap(null);
  }
  function hideSelectedMarkers() {
	setMapOnAllSelectedMarkers(null);
  }
  // Shows any markers currently in the array.
  function showSelectedMarkers() {
	setMapOnAllSelectedMarkers(map);
  }
  // Deletes all markers in the array by removing references to them.
  function deleteSelectedMarker(id) {
	hideSelectedMarker(id);
	selectedMarkers.delete(id);
  }
  function deleteSelectedMarkers(id) {
	hideSelectedMarkers(id);
	selectedMarkers.clear();
  }


// Places API
// Text Search (New)
async function findPlaces(query) {
	const { Place } = await google.maps.importLibrary("places");
	//@ts-ignore
	const request = {
	  textQuery: query,
	  fields: ["displayName", "location", "addressComponents", "primaryType",
		"photos", "rating", "userRatingCount"],
	  language: "ko"
	};
	//@ts-ignore
	const { places } = await Place.searchByText(request);
	console.log(places);

	clearSearchedPlaces();
	deleteMarkers(markers);
    
	if (places.length) {  
	  const { LatLngBounds } = await google.maps.importLibrary("core");
	  const bounds = new LatLngBounds();
		
	  // Loop through and get all the results.
	  places.forEach((place) => {
		addMarker(markers, place.location, place.displayName);

		bounds.extend(place.location);
		bindSearchPlaces(place);
	  });
	  map.setCenter(bounds.getCenter());
	  map.setZoom(10);
	} else {
	  console.log("No results");
	}
}
// Place Details
async function getPlaceDetails(id) {
	const { Place } = await google.maps.importLibrary("places");
	// Use place ID to create a new Place instance.
	const place = new Place({
	  id: id,
	  requestedLanguage: "ko", // optional
	});
  
	// Call fetchFields, passing the desired data fields.
	await place.fetchFields({
	  fields: ["displayName", "formattedAddress", "location", "addressComponents",
			"primaryType", "photos", "rating", "userRatingCount"],
	});
	// Log the result
	console.log(place);
	console.log(place.addressComponents);

	// Add an Advanced Marker
	addSelectedMarker(id, place.location, place.displayName);

	map.setCenter(place.location);
	map.setZoom(10);

	return place;
  }

// 검색 목록 바인딩
function bindSearchPlaces(place) {
	const addr = getAddrByPlace(place.addressComponents);
	const city = regexEng.test(addr[0])?cities.get(addr[0]):addr[0];
	const type = types.get(place.primaryType);

	let str="";
	if($('.place_choose').children('.place_list').children().length > 0) {
		str += '<span class="place_separator"></span>';
	}
	str += '<div class="place">';
	str += '<input type="hidden" value="'+place.id+'"></input>';
	str += '<div class="place_img">';
	str += '<img src="'+place.photos[0].getURI()+'" alt="">';
	str += '</div>';
	str += '<div class="place_info">';
	str += '<div class="place_name">';
	str += '<span>'+place.displayName+'</span>';
	str += `<div class="place_type" style="background-color:${getColorByPlaceType(type)}">`+type+'</div>';
	str += '</div>';
	str += '<div class="place_ratings">';
	str += '<div class="img_rating">';
	str += '<img src="/images/rating.png" alt="">';
	str += '</div>';
	str += '<span class="place_rating">'+place.rating+'</span>';
	str += '<span class="place_ratings_total">('+place.userRatingCount+')</span>';
	str += '</div>';
	str += '<span class="place_address">'+`${addr[1]} ${city}`+'</span>';
	str += '</div>';
	// selectedPlaces 존재시 버튼 이벤트 막기
	if(!selectedPlaces.get(place.id)) {
		str += '<div class="btn btn_add_place">';
	}
	else {
		str += '<div class="btn btn_add_place btn_clicked btn_no_hover">';
	}
	str += '<span>추&nbsp&nbsp가</span>';
	str += '</div>';
	str += '</div>';
	$('.place_choose').children('.place_list').append(str);
}
// 선택 목록에서 추가 
function bindSelectPlace(place) {
	let str = "";
	if($('.selectedPlaces').children().length > 0) {
		str += '<span class="place_separator"></span>';
	}
	str +=	`<div class="selectedPlace">
				<input type="hidden" value="${place.id}"></input>
				<div class="place_img">
					<img src="${place.photo}" alt="">
				</div>
				<div class="place_info">
					<div class="place_name">
						<span>${place.name}</span>
						<div style="background-color:${place.color}"><span>${place.type}</span></div>
					</div>
					<span>${place.country} ${place.city}</span>
				</div>
				<div class="place_remove">
					<img src="/images/remove.png" alt="">
				</div>
			</div>`;
	$('.selectedPlaces').append(str);
}
// clear list
function clearSearchedPlaces() {
	$('.place_choose').children('.place_list').empty();
}
// 선택 목록에서 제거
function removeSelectedPlace(id) {
	$('.selectedPlaces').find('input').each((index, element)=> {
        if(element.value===id) {
            const selectedPlace = $(element).parent();
            if(selectedPlace.next().length === 0) {
                selectedPlace.prev().remove();
            }
            else {
                selectedPlace.next().remove();
            }
            selectedPlace.remove();
        }
    });
}
// 선택 목록에서 제거 시 이벤트 복구
function deselectSearchedPlace(id) {
	$('.place_choose').children('.place_list').find('input').each((index, element)=> {
        if(element.value===id) {
			console.log(id);
			console.log(element.value);
            const searchedPlace = $(element).parent();
			searchedPlace.children().last().removeClass("btn_clicked btn_no_hover");
        }
    });
}

// API를 통해 받은 장소 데이터를 Place 객체로 변환
function parsePlace(place) {
    const addr = getAddrByPlace(place.addressComponents);
	let city = regexEng.test(addr[0])?cities.get(addr[0]):addr[0];
	if(city==="" || city==null || city==undefined) {
		city="";
	}
	const type = types.get(place.primaryType);

    const object = {
		// itineraryKey: -1,
        id: place.id,
        name: place.displayName,
        addr: place.formattedAddress,
        latitude: place.location.lat().toFixed(6),
        longitude: place.location.lng().toFixed(6),
        // startTime: "",
        // endTime: "",
        rating: place.rating,
        ratingCnt: place.userRatingCount,
        type: type,
        color: getColorByPlaceType(type),
        photo: place.photos[0].getURI(),
        city: city,
		countryCode: addr[2],
        country: addr[1],
        continent: continents.get(addr[2])
    };

    return object;
}
// get city and country
function getAddrByPlace(addrComponents) {
	let city;
	let country;
	let countryCode;
	addrComponents.forEach((component) => {
		switch(component.types[0]) {
			case "country":
				country = component.longText;
				countryCode = component.shortText;
				break;
			case "locality":
				if(!city) {
					city = component.longText;
					break;
				}
			case "administrative_area_level_1":
				if(!city) {
					city = component.longText;
					break;
				}
			case "administrative_area_level_2":
				if(!city) {
					city = component.longText;
					break;
				}
			case "administrative_area_level_3":
				if(!city) {
					city = component.longText;
					break;
				}
		}
	});
	return [city, country, countryCode];
}
// separate place by type
function getColorByPlaceType(type) {
	let color="";
	switch(type) {
		case "식당":
			color = RESTAURANT_TYPE;
			break;
		case "숙소":
			color = LODGING_TYPE;
			break;
		case "관광지":
			color = TOURIST_ATTRACTION_TYPE;
			break;
		case "공항":
			color = AIRPORT_TYPE;
			break;
		default:
			color = ETC_TYPE;
	}
	return color;
}


// Load libraries
initMap();




























































































// 현재 위치로 초기화

// Note: This example requires that you consent to location sharing when
// prompted by your browser. If you see the error "The Geolocation service
// failed.", it means you probably did not give permission for the browser to
// locate you.

// let map, infoWindow;

// async function initMap() {
// 	const { Map } = await google.maps.importLibrary("maps");

// 	// Try HTML5 geolocation.
// 	if (navigator.geolocation) {
// 		navigator.geolocation.getCurrentPosition(
// 		(position) => {
// 			const pos = {
// 				lat: position.coords.latitude,
// 				lng: position.coords.longitude,
// 			};

// 			map = new google.maps.Map(document.getElementById("map"), {
// 				center: pos,
// 				zoom: 15,
// 			});
// 			infoWindow = new google.maps.InfoWindow();

// 			infoWindow.setPosition(pos);
// 			infoWindow.setContent("현재 위치");
// 			infoWindow.open(map);
// 			// map.setCenter(pos);
// 		},
// 		() => {
// 			handleLocationError(true, infoWindow, map.getCenter());
// 			},
// 		);
// 	} else {
// 		// Browser doesn't support Geolocation
// 		handleLocationError(false, infoWindow, map.getCenter());
// 	}
// }

// function handleLocationError(browserHasGeolocation, infoWindow, pos) {
// 	infoWindow.setPosition(pos);
// 	infoWindow.setContent(
// 	browserHasGeolocation
// 		? "Error: The Geolocation service failed."
// 		: "Error: Your browser doesn't support geolocation.",
// 	);
// 	infoWindow.open(map);
// }

// initMap();


