$(document).ready(function(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $(".deleteLesson").click(function(e){
        e.preventDefault();
        var id = e.target.id;
        var path = root + "lessons/" + id.substr(6) + "/delete" ;

        $.post(path, "", function (data) {
            var tr = e.target.closest("tr");
            $(tr).children().eq(2).html("canceled");
            $(tr).children().eq(3).html("&mdash;");
            $(tr).children().eq(4).html("");
        });
    });

});