
function toggleInnerTableRow(event, row, isToggleAll, doShow) {

    var togglerCell = row.find("td:eq(0)"),
        togglerElement = togglerCell.find(".arrow"),
        innerList = row.next(),
        nestedTable = innerList.find("table");

    // Don't expand the table if a user clicks on a link.
    if (typeof isToggleAll === "undefined" && event.target.tagName == "A") {
        return;
    }

    var showOrHide;

    if (typeof doShow === "undefined") {
        var doShow = innerList.is(":visible");
    }

    if (doShow) {
        togglerCell.attr("title", "Click to show individual records");
        togglerElement.attr("class", "arrow closed");
        innerList.hide();
        showOrHide = "hide";
    } else {
        togglerCell.attr("title", "Click to hide individual records");
        togglerElement.attr("class", "arrow open");
        if (!isToggleAll) {
            innerList.show();
        }
        showOrHide = "show";
    }
    return showOrHide;
}


// Expands and contracts inner table upon clicking a row
$(document).on("click", "tr.expandable", function(event) {

    // Don't expand the table if a user clicks on a link.
    if (event.target.tagName == "A") {
        return;
    }

    var row = $(this);
    toggleInnerTableRow(event, row);

});

$(document).ready(function() {
  //$("#queue").DataTable();
});
