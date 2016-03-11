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
  var dialogWidth = window.innerWidth - 20;
  var tableHeight = dialogHeight - 100;

  var irQueue = new Handsontable(irContainer, {
    data: individualRecords,
    height: tableHeight,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [40, , , , , , , , , , 25, , ],
    colHeaders: [
      'ID',
      'Source cell line*', 'Sample name', 'Sample title',
      'Cell line*', 'Cell type',
      'Treatment', 'Anatomy', 'Harvest site',
      'Dev. stage', 'Sex', 'Species',
      'Disease*',
      'Note'
    ],
    columns: [
      {data: 'id', readOnly: true, renderer: renderBiosampleId},
      {data: 'sourceCellLine', readOnly: true},
      {data: 'sampleName', readOnly: true},
      {data: 'sampleTitle', readOnly: true},
      {data: 'annotCellLine', renderer: renderIRSourceOrAnnot},
      {data: 'annotCellType', renderer: renderIRSourceOrAnnot},
      {data: 'annotCellTreatment', renderer: renderIRSourceOrAnnot},
      {data: 'annotAnatomy', renderer: renderIRSourceOrAnnot},
      {data: 'harvestSite'},
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
        'harvestSite': data[8],
        'annotDevStage': data[9],
        'annotSex': data[10],
        'annotSpecies': data[11],
        'annotDisease': data[12],
        'note': data[13]
      };

      summaryRecords[srIndex][irIndex] = editedIndividualRecord;

      // Ensures that edits which should be proprogated to summary level are
      // Issue #27
      var individualRecords = summaryRecords[srIndex]['individualRecords'];
      var consistentIRFields = individualRecords[0];
      var ir;
      if (individualRecords.length > 1) {
        for (var i = 1; i < individualRecords.length; i++) {
          ir = individualRecords[i];
          for (var field in ir) {
            if (ir[field] != consistentIRFields[field]) {
              delete consistentIRFields[field];
            }
          }
        }
      }
      $.extend(summaryRecords[srIndex], consistentIRFields);

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

  var queueHeight = window.innerHeight - 170;

  queue = new Handsontable(container, {
    data: summaryRecords,
    height: queueHeight,
    stretchH: 'all',
    sortIndicator: true,
    columnSorting: true,
    contextMenu: true,
    colWidths: [7, 10, , , , , , 15, 30,],
    colHeaders: [
      '', '#', 'Source cell line*', 'Cell line*', 'Cell type', 'Anatomy',
      'Dev. stage', 'Sex', 'Species',
      'Disease*', 'Note'
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
      {data: 'annotDevStage', renderer: renderSourceOrAnnot},
      {data: 'annotSex', renderer: renderSourceOrAnnot},
      {data: 'annotSpecies', renderer: renderSourceOrAnnot},
      {data: 'annotDisease', renderer: renderSourceOrAnnot},
      {data: 'note'}
    ],
    // Work in progress for issue 23 (slow editing)
    //beforeChange: function(change, source) {
    //  console.log(this);
    //  this.pauseObservingChanges();
    //},
    afterChange: function(change, source) {
      if (source === 'loadData' || source === 'external') {
        return; //don't save this change
      }
      // WIP for issue 23
      //this.resumeObservingChanges();

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

      var srDataIndexToIRFieldMap = {
        '3': 'annotCellLine',
        '4': 'annotCellType',
        '5': 'annotAnatomy',
        '6': 'annotDevStage',
        '7': 'annotSex',
        '8': 'annotSpecies',
        '9': 'annotDisease',
        '10': 'note'
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

      editedIRs = JSON.stringify(editedIRs);

      $.ajax({
        'url': '/records/',
        'type': 'POST',
        'data': {'records': editedIRs}
      });

    }

  })
})
