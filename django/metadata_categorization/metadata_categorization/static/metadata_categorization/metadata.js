
$(document).ready(function() {

  var container = document.getElementById("queue");

  var hot = new Handsontable(container, {
    data: summaryRecords,
    height: 396,
    rowHeaders: true,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colHeaders: [
      "Submitted cell line", "Cell line", "Cell type", "Anatomy", "Species",
      "Disease"
    ],
    columns: [{
      data: "sourceCellLine",
      type: "text"
     }, {
      data: "annotCellLine",
      type: "text"
     }, {
      data: "sourceCellType",
      type: "text"
     }, {
      data: "sourceCellAnatomy",
      type: "text"
     }, {
      data: "sourceSpecies",
      type: "text"
    }, {
     data: "sourceDisease",
     type: "text"
   }]
  });
})
