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
		zoom: 7,
		// center: init,
		mapId: "DEMO_MAP_ID",
		disableDefaultUI: true, // 기본 UI 설정,
		minZoom: 7,
  		maxZoom: 20
	});
}

// Adds a marker to the map and push to the array.
async function addMarker(type, position, title) {
	// Request needed libraries.
	//@ts-ignore
	const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

	// 장소 타입별 마커 색상 설정 및 지도 바인딩
	if(type == "관광지") {
		const pinElement = new PinElement({
			background: "#f55a91",
			borderColor: "#f55a91",
			glyphColor: "#f55a91"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		marker.setMap(map);
	}
	if(type == "식당") {
		const pinElement = new PinElement({
			background: "#f47700",
			borderColor: "#f47700",
			glyphColor: "#f47700"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		marker.setMap(map);
	}
	if(type == "기타") {
		const pinElement = new PinElement({
			background: "#f5d030",
			borderColor: "#f5d030",
			glyphColor: "#f5d030"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		marker.setMap(map);
	}
	if(type == "공항") {
		const pinElement = new PinElement({
			background: "#006BDF",
			borderColor: "#006BDF",
			glyphColor: "#006BDF"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		marker.setMap(map);
	}
	if(type == "숙소") {
		const pinElement = new PinElement({
			background: "#00ada4",
			borderColor: "#00ada4",
			glyphColor: "#00ada4"
		})

		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title,
			content: pinElement.element
		});
		marker.setMap(map);
	}
	// 검색한 경우, 기본 마커 추가
	else{
		const marker = new AdvancedMarkerElement({
			map,
			position: position,
			title: title
		  });
		  marker.setMap(map);
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


// 각 장소별 마커 추가
async function findPlaces() {
	const { AdvancedMarkerElement, PinElement } = await google.maps.importLibrary("marker");

	const planDataList = planList.planDataList;
	if (planDataList.length) {  
	  const { LatLngBounds } = await google.maps.importLibrary("core");
	  const bounds = new LatLngBounds();
		
	  // Loop through and get all the results.
	  for(const planData of planDataList) {
        const placeList = planData[0].placeList;
        for(const places of placeList) {
			const place = places.place;
            const latitude = place.latitude;
            const longitude = place.longitude;
			const type = place.type;
			const position = { lat: latitude, lng: longitude };
			
			const pinElement = new PinElement({
				background: colorMap.get(type),
				borderColor: colorborderMap.get(type),
				glyphColor: colorborderMap.get(type)
			})
		
			const marker = new AdvancedMarkerElement({
				map: map,
				position: position,
				title: name,
				content: pinElement.element,
			});

            bounds.extend(position);
        }
    }
	  map.setCenter(bounds.getCenter());
	  map.setZoom(7);
	} else {
	  console.log("No results");
	}
}

// Load libraries
initMap();
findPlaces();