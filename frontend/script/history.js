$(document).ready(function() {
    var dt = $('#table').dataTable();
    dt.fnDestroy();
});

$(document).ready(function() {
    var url = 'https://optimal-delivery-route.herokuapp.com/history/get';

    $.ajax({
        type: 'GET',
        url: url,
        data: {
            page: 1,
            size: 10
        },
        success: function(data) {
            $('#table').DataTable({
                data: data,
                columns: [{
                        data: 'id'
                    }, {
                        data: 'duration'
                    }, {
                        data: 'distance'
                    }, {
                        data: 'createdtime'
                    }, {
                        data: 'locations[ -> ].name'
                    },
                    {
                        data: null,
                        className: "center",
                        defaultContent: '<a class="directions" href=home.html><i class="material-icons" style="font-size: 30px; color: green;">directions</i></a>'
                    }
                ]
            });
        },
        crossDomain: true,
        contentType: "application/json",
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2dSIsImlhdCI6MTYyNzIwNTM4OSwiZXhwIjoxNjI3MjkxNzg5fQ._nts2hbcoZqOoBT9l-s69nmtSjTNeUFTe2jn_BInIDg',
            'Content-Type': 'application/json',
        },
    });

});

$('#table').on('click', 'a.directions', function(e) {
    e.preventDefault();


});