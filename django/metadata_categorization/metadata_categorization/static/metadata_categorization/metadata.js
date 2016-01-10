
$(document).ready(function() {

  var container = document.getElementById("queue");


  function plusRenderer (instance, td, row, col, prop, value, cellProperties) {

    var plusIcon =
      '<svg class="icon icon--plus" viewBox="0 0 5 5" height="20px" width="20px">' +
        '<path d="M2 1 h1 v1 h1 v1 h-1 v1 h-1 v-1 h-1 v-1 h1 z" />' +
      '</svg>';

    $(td).empty().append(plusIcon);
  }

  var hot = new Handsontable(container, {
    data: summaryRecords,
    height: 396,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [7, , , , , ,],
    colHeaders: [
      "", "Submitted cell line", "Cell line", "Cell type", "Anatomy",
      "Species", "Disease"
    ],
    columns: [
      {
        data: "", disableVisualSelection: true, editor: false,
        renderer: plusRenderer
      },
      {data: "sourceCellLine"},
      {data: "annotCellLine"},
      {data: "sourceCellType"},
      {data: "sourceCellAnatomy"},
      {data: "sourceSpecies"},
      {data: "sourceDisease"}
    ]
  });
})
