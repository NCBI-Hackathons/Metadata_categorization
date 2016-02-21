var currentSR;
var dialogHeight;

function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}
$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            var csrftoken = Cookies.get('csrftoken');
            xhr.setRequestHeader('X-CSRFToken', csrftoken);
        }
    }
});

function renderBiosampleId(instance, td, row, col, prop, value, cellProperties) {

  var href = 'https://www.ncbi.nlm.nih.gov/biosample/' + value,
      title = 'View full BioSample record'
      link = '<a href="' + href + '" target="blank" title="' + title + '">' +
        value + '</a>';

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

function renderSourceOrAnnot(instance, td, row, col, prop, value, cellProperties) {

  var rowIndex = cellProperties.physicalRow, // current row index (perhaps after custom sorting)
      srIndex = cellProperties.row, // original summary record index
      sr = summaryRecords[srIndex],
      annotProp = prop,
      annotValue = sr[annotProp],
      sourceProp = prop.replace('annot', 'source'),
      sourceValue = sr[sourceProp];

  if (sr[annotProp] == '') {
    $(td).addClass('source-value').removeClass('annot-value').html(sourceValue);
  } else {
    $(td).addClass('annot-value').removeClass('source-value').html(annotValue);
  }

}

function renderIRSourceOrAnnot(instance, td, row, col, prop, value, cellProperties) {

  var rowIndex = cellProperties.physicalRow, // current row index (perhaps after custom sorting)
      irIndex = cellProperties.row, // original summary record index
      sr = currentSR,
      individualRecord = sr['individualRecords'][irIndex],
      annotProp = prop,
      annotValue = individualRecord[annotProp],
      sourceProp = prop.replace('annot', 'source'),
      sourceValue = individualRecord[sourceProp];

  if (individualRecord[annotProp] == '') {
    $(td).addClass('source-value').removeClass('annot-value').html(sourceValue);
  } else {
    $(td).addClass('annot-value').removeClass('source-value').html(annotValue);
  }

}

plusEditor.prototype.prepare = function(row, col, prop, td, originalValue, cellProperties){

  //Invoke the original method
  Handsontable.editors.BaseEditor.prototype.prepare.apply(this, arguments);

  var rowIndex = cellProperties.physicalRow, // current row index (perhaps after custom sorting)
      srIndex = cellProperties.row, // original summary record index
      summaryRecord = summaryRecords[srIndex],
      individualRecords = summaryRecord['individualRecords'],
      sourceCellLine = summaryRecord['sourceCellLine'],
      irContainer = document.getElementById('irContainer');


  dialogHeight = window.innerHeight - 200;
  var dialogWidth = window.innerWidth - 50;
  var tableHeight = dialogHeight - 100;

  var irQueue = new Handsontable(irContainer, {
    data: individualRecords,
    height: tableHeight,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [30, , , , , , , , , 25, , , ],
    colHeaders: [
      'ID',
      'Source cell line*', 'Sample name', 'Sample title',
      'Cell line*', 'Cell type',
      'Treatment', 'Anatomy', 'Dev. stage', 'Sex',
      'Species',
      'Disease*',
      'Note'
    ],
    columns: [
      {data: 'id', readOnly: true, renderer: renderBiosampleId},
      {data: 'sourceCellLine'},
      {data: 'sampleName'},
      {data: 'sampleTitle'},
      {data: 'annotCellLine', renderer: renderIRSourceOrAnnot},
      {data: 'annotCellType', renderer: renderIRSourceOrAnnot},
      {data: 'annotCellTreatment', renderer: renderIRSourceOrAnnot},
      {data: 'annotAnatomy', renderer: renderIRSourceOrAnnot},
      {data: 'annotDevStage', renderer: renderIRSourceOrAnnot},
      {data: 'annotSex', renderer: renderIRSourceOrAnnot},
      {data: 'annotSpecies', renderer: renderIRSourceOrAnnot},
      {data: 'annotDisease', renderer: renderIRSourceOrAnnot},
      {data: 'note'}
    ],
    afterInit: function() {
      currentSR = summaryRecords[srIndex];
    },
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
        'annotCellLine': data[4],
        'annotCellType': data[5],
        'annotCellTreatment': data[6],
        'annotAnatomy': data[7],
        'annotDevStage': data[8],
        'annotSex': data[9],
        'annotSpecies': data[10],
        'annotDisease': data[11],
        'note': data[12]
      };

      summaryRecords[srIndex][irIndex] = editedIndividualRecord;

      $.ajax({
        'url': '/record/' + id,
        'method': 'POST',
        'data': {'records': JSON.stringify(editedIndividualRecord)}
      });

    }
  })

  $('#irDialog').dialog({
    title: 'Edit individual BioSample records',
    height: dialogHeight,
    width: dialogWidth,
    modal: true,
    create: function(event, ui) {
      // Fix minor UI artifacts
      $('.ui-dialog-titlebar-close .ui-button-text').remove();
    },
    close: function(event, ui) {
      irQueue.destroy();
    }
  });

};

$(document).ready(function() {

  var container = document.getElementById('queue');

  var queueHeight = window.innerHeight - 160;

  var queue = new Handsontable(container, {
    data: summaryRecords,
    height: queueHeight,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [7, 10, , , , , 40,],
    colHeaders: [
      '', '#', 'Source cell line*', 'Cell line*', 'Cell type', 'Anatomy',
      'Species', 'Disease*', 'Note'
    ],
    columns: [
      {
        data: '', disableVisualSelection: true, editor: plusEditor,
        renderer: renderPlus,
      },
      {data: 'recordsCount', readOnly: true},
      {data: 'sourceCellLine'},
      {data: 'annotCellLine', renderer: renderSourceOrAnnot},
      {data: 'annotCellType', renderer: renderSourceOrAnnot},
      {data: 'annotAnatomy', renderer: renderSourceOrAnnot},
      {data: 'annotSpecies', renderer: renderSourceOrAnnot},
      {data: 'annotDisease', renderer: renderSourceOrAnnot},
      {data: 'note'}
    ],
    afterChange: function (change, source) {
      if (source === 'loadData' || source === 'external') {
        return; //don't save this change
      }

      var srIndex = change[0][0], // e.g. 0
          column = change[0][1], // e.g. annotCellLine
          oldValue = change[0][2], // e.g. null
          newValue = change[0][3], // e.g. HeLa
          data = this.getDataAtRow(srIndex),
          id = data[0];

      var editedRecords = [];

      var thisSummaryRecord = summaryRecords[srIndex],
          irList = thisSummaryRecord['individualRecords'],
          irField,
          srValue,
          editedIR;


      var editedIndividualRecord = {
        'id': id,
        //'sourceCellLine': data[1],
        'annotCellLine': data[2],
        'annotCellType': data[3],
        'annotAnatomy': data[4],
        'annotSpecies': data[5],
        'annotDisease': data[6],
        'note': data[7]
      };

      var srDataIndexToIRFieldMap = {
        '3': 'annotCellLine',
        '4': 'annotCellType',
        '5': 'annotAnatomy',
        '6': 'annotSpecies',
        '7': 'annotDisease',
        '8': 'note'
      }

      var editedIRs = [];

      for (var j = 0; j < irList.length; j++) {
        for (var i = 0; i < data.length; i++) {
            srValue = data[i];
            irField = srDataIndexToIRFieldMap[i];
            if (srValue != '') {
              summaryRecords[srIndex]['individualRecords'][j][irField] = srValue;
            }
        }
        editedIR = summaryRecords[srIndex]['individualRecords'][j];
        editedIRs.push(editedIR);
      }

      console.log(editedIRs)

      editedIRs = JSON.stringify(editedIRs);

      $.ajax({
        'url': '/records/',
        'type': 'POST',
        'data': {'records': editedIRs}
      });

    }

  })
})
