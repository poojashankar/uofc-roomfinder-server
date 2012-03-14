dojo.require("esri.map");
var myMap;

function init() {

	var lods = [ {
		"level" : 1,
		"resolution" : 6.61459656252646,
		"scale" : 25000
	}, {
		"level" : 2,
		"resolution" : 2.64583862501058,
		"scale" : 10000
	}, {
		"level" : 3,
		"resolution" : 1.32291931250529,
		"scale" : 5000
	}, {
		"level" : 4,
		"resolution" : 0.661459656252646,
		"scale" : 2500
	}, {
		"level" : 5,
		"resolution" : 0.264583862501058,
		"scale" : 1000
	}, {
		"level" : 6,
		"resolution" : 0.132291931250529,
		"scale" : 500
	}, {
		"level" : 7,
		"resolution" : 0.0661459656252646,
		"scale" : 250
	} ];

	//init map with extent
	myMap = new esri.Map("mapDiv", {
		logo : false,
		nav : false,
		extent : new esri.geometry.Extent({
			xmin : 700300,
			ymin : 5662000,
			xmax : 701300,
			ymax : 5662500,
			spatialReference : {
				wkid : 26911
			}
		}),
		lods : lods
	});

	//aerial picture
	//var aerialLayer = new esri.layers.ArcGISTiledMapServiceLayer("http://136.159.24.32/ArcGIS/rest/services/Imagery/TrueOrtho2011_cached/MapServer");
	//myMap.addLayer(aerialLayer);

	//floor plan
	var floorLayer = new esri.layers.ArcGISDynamicMapServiceLayer(
			"http://asebeast.cpsc.ucalgary.ca:1892/ArcGIS/rest/services/RoomFinder/MapServer");
	myMap.addLayer(floorLayer);

	//building layer, names and outline
	buildingLayer = new esri.layers.ArcGISDynamicMapServiceLayer(
			"http://136.159.24.32/ArcGIS/rest/services/Buildings/MapServer");
	buildingLayer.opacity = 0.4;
	myMap.addLayer(buildingLayer);

	dojo.connect(myMap, "onLoad", getLocation);
}

function displayLocation(point) {
	var pt = new esri.geometry.Point(point.x, point.y,
			new esri.SpatialReference({
				wkid : 26911
			}));
	var symbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 20,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							0, 0, 255 ]), 1), new dojo.Color(
					[ 0, 0, 255, 0.25 ]));
	var graphic = new esri.Graphic(pt, symbol);

	myMap.graphics.add(graphic);
}

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(zoomToLocation, locationError);
		//navigator.geolocation.watchPosition(showLocation, locationError);
	}

}

function locationError(error) {
	switch (error.code) {
	case error.PERMISSION_DENIED:
		alert("Location not provided");
		break;

	case error.POSITION_UNAVAILABLE:
		alert("Current location not available");
		break;

	case error.TIMEOUT:
		alert("Timeout");
		break;

	default:
		alert("unknown error");
		break;
	}

}

function zoomToLocation(location) {
	//var pt = esri.geometry.geographicToWebMercator(new esri.geometry.Point(location.coords.longitude, location.coords.latitude));

	//          var pt = new esri.geometry.Point(700973.4620294351, 5662694.382010558, new esri.SpatialReference({ wkid: 26911 }));

	Proj4js.defs["EPSG:2153"] = "+proj=utm +zone=11 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs";

	var source = new Proj4js.Proj('WGS84'); //source coordinates will be in Longitude/Latitude WGS84
	var dest = new Proj4js.Proj('EPSG:2153'); //destination coordinates UTMNAD83z11N in Calgary

	// transforming point coordinates
	var p = new Proj4js.Point(location.coords.longitude,
			location.coords.latitude); //any object will do as long as it has 'x' and 'y' properties
	Proj4js.transform(source, dest, p); //do the transformation.  x and y are modified in place

	var pt = new esri.geometry.Point(p.x, p.y, new esri.SpatialReference({
		wkid : 26911
	}));

	displayLocation(pt);
	myMap.centerAndZoom(pt, 5);
}

function orientationChanged() {
	console.log("Orientation changed: " + window.orientation);
	if (map) {
		map.reposition();
		map.resize();
	}
}

dojo.addOnLoad(init);