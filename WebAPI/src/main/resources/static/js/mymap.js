const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2dTEiLCJpYXQiOjE2MjcxNDY3MjYsImV4cCI6MTYyNzIzMzEyNn0.ciSF-RITnssDvdwlZs6Dd7AJyqpqy_aX9WKLnWTNGUc'

let userLocation = null;
let truckLocation = [106.805, 10.88];
let warehouseLocation = [106.81, 10.88];
let lastResult = null;
let lastQueryTime = 0;
let lastAtRestaurant = 0;
let currentSchedule = [];
let currentRoute = null;
let pause = true;
let speedFactor = 50;
let profile = "driving-traffic";

const jwt_token = 'jwt_token'
const getCookieValueOf = function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}


// Add your access token
mapboxgl.accessToken =
    "pk.eyJ1IjoidHVkYW9ia3UiLCJhIjoiY2tvZmQ2dnNtMDZlNjJwcG53bmw2YXRhYSJ9.quj8ggmNSoE2iKuzCOtU-g";
// Track user location every 2 seconds
setInterval(() => {
    navigator.geolocation.getCurrentPosition(
        (pos) => {
            let coords = pos.coords;
            userLocation = [coords.longitude, coords.latitude];
            console.log(userLocation);
        },
        (e) => {
            alert("Location tracking are disabled!");
            userLocation = null;
        }
    );
}, 2000);
let map = new mapboxgl.Map({
    container: "map", // container id
    style: "mapbox://styles/mapbox/streets-v11",
    center: truckLocation, // starting position
    doubleClickZoom: false, // Disable double click zoom event
    zoom: 14, // starting zoom
});

// Add geolocate control to the map.
let userControl = new mapboxgl.GeolocateControl({
    positionOptions: {
        enableHighAccuracy: true,
    },
    trackUserLocation: true,
});
map.addControl(userControl);
map.addControl(new mapboxgl.NavigationControl());

// Create a GeoJSON feature collection for the warehouse
let warehouse = turf.featureCollection([turf.point(warehouseLocation)]);

// Create an empty GeoJSON feature collection for drop-off locations
let dropoffs = turf.featureCollection([]);

// Create an empty GeoJSON feature collection, which will be used as the data source for the route before users add any new data
let nothing = turf.featureCollection([]);

map.on("load", function() {
    userControl.trigger();

    // create the popup
    var popup = new mapboxgl.Popup({
        offset: 25,
    }).setHTML(
        '<button class="btn btn-danger mt-3" onclick="removeMarker()" data-id="user-marker">Remove</button>'
    );

    map.addSource("route", {
        type: "geojson",
        data: nothing,
    });

    map.addLayer({
            id: "routeline-active",
            type: "line",
            source: "route",
            layout: {
                "line-join": "round",
                "line-cap": "round",
            },
            paint: {
                "line-color": "#3887be",
                "line-width": ["interpolate", ["linear"],
                    ["zoom"], 12, 3, 22, 12
                ],
            },
        },
        "waterway-label"
    );

    map.addLayer({
            id: "routearrows",
            type: "symbol",
            source: "route",
            layout: {
                "symbol-placement": "line",
                "text-field": "▶",
                "text-size": ["interpolate", ["linear"],
                    ["zoom"], 12, 24, 22, 60
                ],
                "symbol-spacing": [
                    "interpolate", ["linear"],
                    ["zoom"],
                    12,
                    30,
                    22,
                    160,
                ],
                "text-keep-upright": false,
            },
            paint: {
                "text-color": "#3887be",
                "text-halo-color": "hsl(55, 11%, 96%)",
                "text-halo-width": 3,
            },
        },
        "waterway-label"
    );

    var geocoder = new MapboxGeocoder({
        accessToken: mapboxgl.accessToken, // Set the access token
        mapboxgl: mapboxgl, // Set the mapbox-gl instance
        reverseGeocode: true,
        marker: false, // Use the geocoder's default marker style
    });

    map.addControl(geocoder, "top-left");

    geocoder.on("result", function(ev) {
        var searchResult = ev.result.geometry;
        var address = ev.result.place_name;
        var coords = {
            lng: searchResult.coordinates[0],
            lat: searchResult.coordinates[1],
        };

        newDropoff(coords, false, address);
    });

    // Listen for a click on the map
    map.on("dblclick", function(e) {
        // When the map is clicked, add a new drop-off point
        // and update the `dropoffs-symbol` layer
        newDropoff(map.unproject(e.point));
    });

    function newDropoff(coords, reverse = true, address = "") {
        // Not allowed to add point after finding optimized route
        if ($("#reset-route").is(":visible")) {
            return;
        }

        let key = Math.random().toString(36).substr(2, 9);
        let dropOffMarker = newMarker(coords, key);

        // if (reverse) {
        //     console.log("Reversing");
        //     query = coords.lng.toFixed(3).toString() + "," + coords.lat.toFixed(3).toString();
        //     $.ajax({
        //         method: "GET",
        //         url: "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?types=address&access_token=" + mapboxgl.accessToken
        //     }).done((data) => {
        //         console.log(data);
        //     })

        // }

        // Store the clicked point as a new GeoJSON feature with
        let pt = turf.point([coords.lng, coords.lat], {
            orderTime: Date.now(),
            key: key,
            address: address,
            marker: dropOffMarker,
        });
        dropoffs.features.push(pt);

        // findRoute();
        buildLocationList(dropoffs);
    }
});

function newMarker(coords, key) {
    // create DOM element for the marker
    let el = document.createElement("div");
    el.className = "marker";

    // create the marker
    marker = new mapboxgl.Marker(el).setLngLat(coords).addTo(map);

    el.addEventListener('click', function(e) {
        /* Fly to the point */
        flyToDropOff(coords);
        /* Close all other popups and display popup for clicked store */
        createPopUp([coords['lng'], coords['lat']]);
        /* Highlight listing in sidebar */
        var activeItem = document.getElementsByClassName('active');
        e.stopPropagation();
        if (activeItem[0]) {
            activeItem[0].classList.remove('active');
        }
        var listing = document.getElementById(
            'listing-' + key
        );
        listing.classList.add('active');
    });


    return marker;
}

/**
 * Create a Mapbox GL JS `Popup`.
 **/
function createPopUp(coords) {
    var popUps = document.getElementsByClassName('mapboxgl-popup');
    if (popUps[0]) popUps[0].remove();
    var popup = new mapboxgl.Popup({
            closeOnClick: true
        })
        .setLngLat(coords)
        .setHTML(
            '<h3>' +
            coords[0].toFixed(3).toString() + ", " + coords[1].toFixed(3).toString() +
            '</h3>'
        )
        .addTo(map);
}

function buildLocationList(data) {
    if (data.features.length === 0) {
        $("#drop-list").hide();
        return;
    }

    $("#drop-list").show();
    var listings = document.getElementById("listings");
    while (listings.children.length > 1) {
        listings.removeChild(listings.firstChild);
    }

    var btn = listings.firstChild;

    data.features.forEach(function(store, i) {
        /**
         * Create a shortcut for `store.properties`,
         * which will be used several times below.
         **/
        var prop = store.properties;

        /* Add a new listing section to the sidebar. */
        var listings = document.getElementById("listings");
        var listing = listings.insertBefore(document.createElement("div"), btn);
        /* Assign a unique `id` to the listing. */
        listing.id = "listing-" + prop.key;
        /* Assign the `item` class to each listing for styling. */
        listing.className = "item";

        /* Add the link to the individual listing created above. */
        var span_link = listing.appendChild(document.createElement("span"));
        var link = span_link.appendChild(document.createElement("a"));
        link.href = "#";
        link.className = "title";
        link.id = "link-" + prop.key;
        coords = store.geometry.coordinates;
        link.innerHTML =
            coords[0].toFixed(3).toString() + ", " + coords[1].toFixed(3).toString();

        var span_remove = listing.appendChild(document.createElement("span"));
        span_remove.className = "remove-drop";
        span_remove.style.float = "right";
        var remove_tag = span_remove.appendChild(document.createElement("a"));
        remove_tag.href = "#";
        var remove_icon = remove_tag.appendChild(document.createElement("i"));
        remove_icon.className = "fa fa-window-close-o remove-drop-off";
        remove_icon.dataset["key"] = prop.key;

        /* Add details to the individual listing. */
        var details = listing.appendChild(document.createElement("div"));
        details.innerHTML = prop.address;

        /**
         * Listen to the element and when it is clicked, do four things:
         * 1. Update the `currentFeature` to the store associated with the clicked link
         * 2. Fly to the point
         **/
        link.addEventListener("click", function(e) {
            for (var i = 0; i < data.features.length; i++) {
                if (this.id === "link-" + data.features[i].properties.key) {
                    var clickedListing = data.features[i];
                    flyToDropOff(clickedListing.geometry.coordinates);
                    createPopUp(clickedListing.geometry.coordinates);
                    break;
                }
            }
            var activeItem = document.getElementsByClassName("active");
            if (activeItem[0]) {
                activeItem[0].classList.remove("active");
            }
            this.parentNode.parentNode.classList.add("active");
        });
    });

    $(".remove-drop-off").on("click", function(e) {
        key = $(this).data("key");
        idx = dropoffs.features.findIndex((item) => item.properties.key === key);
        dropoffs.features[idx].properties.marker.remove();
        dropoffs.features.splice(idx, 1);
        buildLocationList(dropoffs);

        if (dropoffs.features.length > 0) {
            // findRoute();
        } else {
            map.getSource("route").setData(nothing);
        }
    });

    $("#listings").sortable({
        // Element dragging ended
        onEnd: function( /**Event*/ evt) {
            // var itemEl = evt.item; // dragged HTMLElement
            // evt.to; // target list
            // evt.from; // previous list
            // evt.oldIndex; // element's old index within old parent
            // evt.newIndex; // element's new index within new parent
            // evt.oldDraggableIndex; // element's old index within old parent, only counting draggable elements
            // evt.newDraggableIndex; // element's new index within new parent, only counting draggable elements
            // evt.clone; // the clone element
            // evt.pullMode; // when item is in another sortable: `"clone"` if cloning, `true` if moving
            temp = dropoffs.features[evt.oldIndex]
            dropoffs.features[evt.oldIndex] = dropoffs.features[evt.newIndex]
            dropoffs.features[evt.newIndex] = temp

            // findRoute();
        },
        draggable: ".item",
        ghostClass: 'blue-background-class'
    });
}

function flyToDropOff(coords) {
    map.flyTo({
        center: coords,
        zoom: 14,
    });
}

function findRoute(save = false) {
    // Make a request to the Optimization API
    $.ajax({
        method: "GET",
        url: assembleQueryURL(),
    }).done(function(data) {
        // console.log(data);
        // Create a GeoJSON feature collection
        let routeGeoJSON = nothing;
        lastResult = data;

        // If there is no route provided, reset
        if (data.trips[0]) {
            routeGeoJSON = turf.featureCollection([
                turf.feature(data.trips[0].geometry),
            ]);
            $("#result").html((data.trips[0].distance / 1000).toFixed(2) + ' Km - ' + Math.floor(data.trips[0].duration / 60) + ' min')
        } else {
            alert("No route find!!!");
        }
        // Update the `route` source by getting the route source
        // and setting the data equal to routeGeoJSON
        map.getSource("route").setData(routeGeoJSON);

        if (data.waypoints.length === 12) {
            alert(
                "Maximum number of points reached. Read more at docs.mapbox.com/api/navigation/#optimization."
            );
        }

        if (save) {
            $.ajax({
                method: "POST",
                url: "https://optimal-delivery-route.herokuapp.com/history/save",
                headers: {
                    'Authorization': 'Bearer ' + getCookieValueOf(jwt_token),
                    'Content-Type': 'application/json',
                },
                data: JSON.stringify(lastResult)
            })
        }
    });
}

// Here you'll specify all the parameters necessary for requesting a response from the Optimization API
function assembleQueryURL() {
    // Store the location of the truck in a letiable called coordinates
    let coordinates = [userLocation];
    if (userLocation == null) {
        coordinates = [];
    }
    let distributions = [];

    // Create an array of GeoJSON feature collections for each point
    let restJobs = dropoffs.features;

    // If there are any orders from this restaurant
    if (coordinates.length > 0 || restJobs.length > 0) {
        // Check to see if the request was made after visiting the restaurant
        // let needToPickUp = restJobs.filter(function(d, i) {
        //     return d.properties.orderTime > lastAtRestaurant;
        // }).length > 0;

        // // If the request was made after picking up from the restaurant,
        // // Add the restaurant as an additional stop
        // if (needToPickUp) {
        //     let restaurantIndex = coordinates.length;
        //     // Add the restaurant as a coordinate
        //     coordinates.push(warehouseLocation);
        //     // push the restaurant itself into the array
        //     keepTrack.push(pointHopper.warehouse);
        // }

        restJobs.forEach(function(d, i) {
            // Add dropoff to list
            coordinates.push(d.geometry.coordinates);
            // if order not yet picked up, add a reroute
            // if (needToPickUp && d.properties.orderTime > lastAtRestaurant) {
            //     distributions.push(restaurantIndex + ',' + (coordinates.length - 1));
            // }
        });
    }

    // Set the profile to `driving`
    // Coordinates will include the current location of the truck,
    return (
        "https://api.mapbox.com/optimized-trips/v1/mapbox/" + profile + "/" +
        coordinates.join(";") +
        "?distributions=" +
        distributions.join(";") +
        "&overview=full&steps=true&geometries=geojson&source=first&destination=last&roundtrip=false&access_token=" +
        mapboxgl.accessToken
    );
}

$(document).ready(function() {
    $(".heading .toggle").on("click", function(e) {
        target = $(this).data("toggle");
        $(target).toggle(200, "swing");
    });

    $("#save-history").on("click", function(e) {
        if (dropoffs.features.length > 0) {
            findRoute(save = true);
            $("#save-history").toggle();
            $("#reset-route").toggle();
            $(".remove-drop").hide();
        }
    });

    $("#reset-route").on("click", function(e) {
        if (dropoffs.features.length > 0) {
            $("#result").html("");
            $("#save-history").toggle();
            $("#reset-route").toggle();
            $(".mapboxgl-popup").remove();
            $(".marker").remove();
            dropoffs = turf.featureCollection([]);
            buildLocationList(dropoffs);
            map.getSource("route").setData(nothing);
        }
    })

    $("input[type=radio][name=profile]").change(function(e) {
        profile = $(this).val();
    })
});