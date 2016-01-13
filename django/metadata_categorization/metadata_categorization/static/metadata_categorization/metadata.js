function renderBiosampleId(instance, td, row, col, prop, value, cellProperties) {
  // Converts "id" to BioSample accession, and hyperlinks it
  // BioSample accessions have the form "SAMN", plus 8 digits,
  // E.g. id 2730062 -> SAMN02730062
  var id = "" + value, // cast int to string
      leadingZeros = Array(8 - id.length + 1).join("0"),
      biosampleAcc = "SAMN" + leadingZeros + value;

  var href = "https://www.ncbi.nlm.nih.gov/biosample/" + biosampleAcc,
      title = "View full BioSample record"
      link = '<a href="' + href + '" target="blank" title="' + title + '">' +
        biosampleAcc + '</a>';

  $(td).html(link);
}

function renderPlus (instance, td, row, col, prop, value, cellProperties) {

  var plusIcon =
    '<svg class="icon icon--plus" viewBox="0 0 5 5" height="20px" width="20px">' +
      '<path d="M2 1 h1 v1 h1 v1 h-1 v1 h-1 v-1 h-1 v-1 h1 z" />' +
    '</svg>';

  $(td).html(plusIcon);
}

var plusEditor = Handsontable.editors.BaseEditor.prototype.extend();

plusEditor.prototype.prepare = function(row, col, prop, td, originalValue, cellProperties){

  //Invoke the original method
  Handsontable.editors.BaseEditor.prototype.prepare.apply(this, arguments);

  var rowIndex = cellProperties.physicalRow, // current row index (perhaps after custom sorting)
      srIndex = cellProperties.row, // original summary record index
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
    colWidths: [, , , , , , ],
    colHeaders: [
      "BioSample ID", "Source cell line", "Cell line", "Cell type", "Anatomy",
      "Species", "Disease"
    ],
    columns: [
      {data: "id", readOnly: true, renderer: renderBiosampleId},
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
    width: 950,
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
    colWidths: [7, 10, , , , , ,],
    colHeaders: [
      "", "#", "Source cell line", "Cell line", "Cell type", "Anatomy",
      "Species", "Disease"
    ],
    columns: [
      {
        data: "", disableVisualSelection: true, editor: plusEditor,
        renderer: renderPlus,
      },
      {data: "recordsCount", readOnly: true},
      {data: "sourceCellLine"},
      {data: "annotCellLine"},
      {data: "sourceCellType"},
      {data: "sourceCellAnatomy"},
      {data: "sourceSpecies"},
      {data: "sourceDisease"}
    ]
  })
})
