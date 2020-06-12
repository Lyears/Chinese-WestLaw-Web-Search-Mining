$('#sortType').children("a").click(function () {
    let sortType = $(this).html()
    let searchType = $('#searchType').children('.active').children().attr('id');
    console.log(sortType)
    console.log(searchType)
    switch (sortType) {
        case "Sorted by Age":
            break;
        case "Sorted by Date":
            break;
        case "Sorted by Name":
            break;
        case "Sorted by Date":
            break;
        case "Sorted by Age":
            break;

    }
})

$('#queryButton').click(function () {
    let queryStr = $('#inputStr').val()
    window.location.href = "/list?" + "searchType=query&" + "searchValue=" + queryStr + "sortType=0"
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

