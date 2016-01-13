function plusRenderer (instance, td, row, col, prop, value, cellProperties) {

  var plusIcon =
    '<svg class="icon icon--plus" viewBox="0 0 5 5" height="20px" width="20px">' +
      '<path d="M2 1 h1 v1 h1 v1 h-1 v1 h-1 v-1 h-1 v-1 h1 z" />' +
    '</svg>';

  $(td).empty().append(plusIcon);
}

var plusEditor = Handsontable.editors.BaseEditor.prototype.extend();

plusEditor.prototype.prepare = function(row, col, prop, td, originalValue, cellProperties){

  //Invoke the original method
  Handsontable.editors.BaseEditor.prototype.prepare.apply(this, arguments);

  var srIndex = cellProperties.physicalRow,
      rowIndex = cellProperties.row,
      summaryRecord = summaryRecords[srIndex],
      individualRecords = summaryRecord["individualRecords"],
      sourceCellLine = summaryRecord["sourceCellLine"],
      irContainer = document.getElementById("irContainer");

  var irQueue = new Handsontable(irContainer, {
    data: individualRecords,
    height: 300,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [ , , , , , ],
    colHeaders: [
      "Source cell line", "Cell line", "Cell type", "Anatomy","Species",
      "Disease"
    ],
    columns: [
      {data: "sourceCellLine"},
      {data: "annotCellLine"},
      {data: "sourceCellType"},
      {data: "sourceCellAnatomy"},
      {data: "sourceSpecies"},
      {data: "sourceDisease"}
    ]
  })

  $("#irDialog").dialog({
    title: 'Edit individual records',
    height: 400,
    width: 800,
    create: function( event, ui ) {

      // Fix minor UI artifacts
      $(".ui-dialog-titlebar-close .ui-button-text").remove();
    }
  });

};

$(document).ready(function() {

  var container = document.getElementById("queue");

  var queue = new Handsontable(container, {
    data: summaryRecords,
    height: 396,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [7, , , , , ,],
    colHeaders: [
      "", "Source cell line", "Cell line", "Cell type", "Anatomy",
      "Species", "Disease"
    ],
    columns: [
      {
        data: "", disableVisualSelection: true, editor: plusEditor,
        renderer: plusRenderer,
      },
      {data: "sourceCellLine"},
      {data: "annotCellLine"},
      {data: "sourceCellType"},
      {data: "sourceCellAnatomy"},
      {data: "sourceSpecies"},
      {data: "sourceDisease"}
    ]
  })
})
