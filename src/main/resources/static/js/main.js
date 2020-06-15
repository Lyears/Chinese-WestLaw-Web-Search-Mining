$('#sortType').children("a").click(function () {
    let sortType = $(this).html()
    let searchType = $('#searchType').children('.active').children().attr('id');
    console.log(sortType)
    console.log(searchType)
    let queryStr = $('#inputStr').val()
    switch (sortType) {
        case "Sorted by Age":
            let params = {
                "searchType": searchType,
                "searchValue": queryStr,
                "sortType": 1,
                "page": 1
            }
            window.location.href = "/list?" + "searchType=" + searchType + "&searchValue=" + queryStr + "&sortType=0&page=0"
            break;
        case "Sorted by Date":
            break;
        case "Sorted by Name":
            break;
        // case "Sorted by Date":
        //     break;
        // case "Sorted by Age":
        //     break;

    }
})

function httpPost(url, args) {
    var form = $("<form method='post'></form>")
    form.attr({"action": url})
    for (let arg in args) {
        var input = $('<input type="hidden">')
        input.attr({'name': arg})
        input.val(args[arg])
        form.append(input)
    }
    form.submit()
}

$('#queryButton').click(function () {
    let queryStr = $('#inputStr').val()
    window.location.href = "/instruments?" + "searchType=query&" + "searchValue=" + queryStr + "&sortType=0"
})

$('#query').click(function () {
    $('#queryButton').css('display', 'block')
    $('#dropButton').css('display', 'none')
})
$('#boolean').click(function () {
    $('#queryButton').css('display', 'none')
    $('#dropButton').css('display', 'block')
})
$('#one-shot').click(function () {
    $('#queryButton').css('display', 'none')
    $('#dropButton').css('display', 'block')
})

