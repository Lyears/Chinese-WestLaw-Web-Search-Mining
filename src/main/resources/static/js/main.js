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