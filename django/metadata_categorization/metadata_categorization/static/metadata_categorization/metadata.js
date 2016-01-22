function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}
$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            var csrftoken = Cookies.get('csrftoken');
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
        }
    }
});

function biosampleAccToId(biosampleAcc) {
  // Converts BioSample accession to individual record ID
  // BioSample accessions have the form "SAMN" + 8 digits
  // E.g. SAMN02730062 -> 2730062
  var id = biosampleAcc.split(/SAMN[0]+/)[1];

  return id;
}

function idToBiosampleAcc(id) {
  // Converts individual record ID to BioSample accession
  // BioSample accessions have the form "SAMN" + 8 digits
  // E.g. 2730062 -> SAMN02730062
  var id = "" + id, // cast int to string
      leadingZeros = Array(8 - id.length + 1).join("0"),
      biosampleAcc = "SAMN" + leadingZeros + id;

  return biosampleAcc;
}

function renderBiosampleId(instance, td, row, col, prop, value, cellProperties) {

  var biosampleAcc = idToBiosampleAcc(value);

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
    ],
    afterChange: function (change, source) {
      if (source === 'loadData') {
        return; //don't save this change
      }

      var irIndex = change[0][0], // e.g. 0
          column = change[0][1], // e.g. annotCellLine
          oldValue = change[0][2], // e.g. null
          newValue = change[0][3], // e.g. HeLa
          data = this.getDataAtRow(irIndex),
          id = data[0];

      var editedIndividualRecord = {
        'id': id,
        //'sourceCellLine': data[1],
        'annotCellLine': data[2],
        'annotCellType': data[3],
        'annotCellAnatomy': data[4],
        'annotSpecies': data[5],
        'annotDisease': data[6]
      };

      summaryRecords[srIndex][irIndex] = editedIndividualRecord;

      $.ajax({
        'url': '/record/' + id,
        'method': 'POST',
        'data': editedIndividualRecord
      });

    }
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
