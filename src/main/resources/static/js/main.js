$('#sortType').children("a").click(function () {
    let sortType = $(this).html()
    let searchType = $('#searchType').children('.active').children().attr('id');
    console.log(sortType)
    console.log(searchType)
    let queryStr = $('#inputStr').val()
    let params = {
        "searchType": searchType,
        "searchValue": queryStr,
        "page": 1
    }
    switch (sortType) {
        case "Sorted by Age":
            params['sortType'] = 1
            httpGet("/list", params)
            break;
        case "Sorted by Date":
            params['sortType'] = 2
            httpGet("/list", params)
            break;
        case "Sorted by Name":
            params['sortType'] = 3
            httpGet("/list", params)
            break;
        case "Sorted by Fines":
            params['sortType'] = 4
            httpGet("/list", params)
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
    $('body').append(form)
    form.submit()
}

function httpGet(url, args) {
    var urlStr = url + '?'
    for (let arg in args) {
        urlStr += (arg + '=' + args[arg] + '&')
    }
    urlStr = urlStr.substr(0, urlStr.length - 1)
    window.location.href = urlStr
}

$('#queryButton').click(function () {
    let queryStr = $('#inputStr').val()
    window.location.href = "/instruments?searchValue=" + queryStr
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

