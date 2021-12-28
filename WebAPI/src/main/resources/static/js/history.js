const jwt_token = 'jwt_token'
const getCookieValueOf = function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}


function readOnStart() {
    $(document).ready(function () {
        var dt = $('#table').dataTable();
        dt.fnDestroy();
    });
}
function getHistoryTable(pageNumber, size,keyword="")
{
    $(document).ready(function() {
    var url = 'https://optimal-delivery-route.herokuapp.com/history/get';
    $.ajax({
        type: 'GET',
        url: url,
        data: {
            page:pageNumber,
            size: size
        },
        success: function(data) {
            $('#table').DataTable({
                data: data,
                columns: [{
                        data: 'duration'
                    }, {
                        data: 'distance'
                    }, {
                        data: 'createdtime'
                    }, {
                        data: 'locations',
                        render: '[ -> ]',
                        className: "left"
                    }
                    // {
                    //     data: null,
                    //     className: "center",
                    //     defaultContent: '<a class="directions" href=home.html><i class="material-icons" style="font-size: 30px; color: green;">directions</i></a>'
                    // }
                ]
            });
        },
        crossDomain: true,
        contentType: "application/json",
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + getCookieValueOf(jwt_token),
            'Content-Type': 'application/json',
        },
    });

});
}
getHistoryTable(1,50);
$('#table').on('click', 'a.directions', function(e) {
    e.preventDefault();
});
$("[name='table_length']").on('change', function(){
    var size = this.value*5;
    // var page = $('.current').attr('data-dt-idx');
    getHistoryTable(1,size);
})
