document.getElementById("calculateBtn").addEventListener("click", function () {
    var pointA = document.getElementById("pointA").value;
    var pointB = document.getElementById("pointB").value;

    if (pointA.includes("[a-zA-Z]")) {
        calculateDistance(pointA, pointB);
    } else {
        geocodeAddress(pointA, pointB);
    }
});

function calculateDistance(pointA, pointB) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/calculate?pointA=" + pointA + "&pointB=" + pointB, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("result").textContent = xhr.responseText;
        }
    };
    xhr.send();
}

function geocodeAddress(addressA, addressB) {
    var xhrA = new XMLHttpRequest();
    var xhrB = new XMLHttpRequest();

    xhrA.open("GET", "/geocode?address=" + addressA, true);
    xhrB.open("GET", "/geocode?address=" + addressB, true);

    xhrA.onreadystatechange = function () {
        if (xhrA.readyState === 4 && xhrA.status === 200) {
            var responseA = JSON.parse(xhrA.responseText);
            if (responseA.results.length > 0) {
                var coordinatesA = responseA.results[0].geometry;
                var latitudeA = coordinatesA.lat;
                var longitudeA = coordinatesA.lng;

                xhrB.onreadystatechange = function () {
                    if (xhrB.readyState === 4 && xhrB.status === 200) {
                        var responseB = JSON.parse(xhrB.responseText);
                        if (responseB.results.length > 0) {
                            var coordinatesB = responseB.results[0].geometry;
                            var latitudeB = coordinatesB.lat;
                            var longitudeB = coordinatesB.lng;

                            calculateDistance(latitudeA + "," + longitudeA, latitudeB + "," + longitudeB);
                        }
                    }
                };
                xhrB.send();
            }
        }
    };
    xhrA.send();
}
