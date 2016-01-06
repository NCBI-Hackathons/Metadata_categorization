
// See http://jsfiddle.net/rafael_cichocki/wwdg8/7/ for
// reference for functions below
function fnFormatDetails(table_id, html) {
    var sOut = "<table id=\"summaryTable_" + table_id + "\">";
    sOut += html;
    sOut += "</table>";
    return sOut;
}

var newRowData = summaryRecords;

var iTableCounter = 1;
  var oTable;
  var oInnerTable;
  var detailsTableHtml;

  $(document).ready(function () {

      detailsTableHtml = $("#detailsTable").html();

      //Insert a 'details' column to the table
      var nCloneTh = document.createElement('th');
      var nCloneTd = document.createElement('td');
      nCloneTd.innerHTML = '<img src="http://i.imgur.com/SD7Dz.png">';
      nCloneTd.className = "center";

      $('#summaryTable thead tr').each(function () {
          this.insertBefore(nCloneTh, this.childNodes[0]);
      });

      $('#summaryTable tbody tr').each(function () {
          this.insertBefore(nCloneTd.cloneNode(true), this.childNodes[0]);
      });


      //Initialse DataTables, with no sorting on the 'details' column
      var oTable = $('#summaryTable').dataTable({
          "bJQueryUI": true,
          "aaData": newRowData,
          "bPaginate": true,
          "aoColumns": [
            {
               "mDataProp": null,
               "sClass": "control center",
               "sDefaultContent": '<img src="http://i.imgur.com/SD7Dz.png">'
            },
            { "mDataProp": "sourceCellLine"},
            { "mDataProp": "sourceCellType"},
            { "mDataProp": "sourceCellTreatment"},
            { "mDataProp": "sourceAnatomy"},
            { "mDataProp": "sourceSpecies"},
            { "mDataProp": "sourceDisease"}
          ],
          "oLanguage": {
    		    "sInfo": "_TOTAL_ entries"
      		},
          "aaSorting": [[1, 'desc']]
      });

      /* Add event listener for opening and closing details
      * Note that the indicator for showing which row is open is not controlled by DataTables,
      * rather it is done here
      */
      $('#summaryTable tbody td img').live('click', function () {
          var nTr = $(this).parents('tr')[0];
          var nTds = this;

          if (oTable.fnIsOpen(nTr)) {
              /* This row is already open - close it */
              this.src = "http://i.imgur.com/SD7Dz.png";
              oTable.fnClose(nTr);
          }
          else {
              /* Open this row */
              var rowIndex = oTable.fnGetPosition( $(nTds).closest('tr')[0] );
              var detailsRowData = newRowData[rowIndex].individualRecords;

              this.src = "http://i.imgur.com/d4ICC.png";
              oTable.fnOpen(nTr, fnFormatDetails(iTableCounter, detailsTableHtml), 'details');
              oInnerTable = $("#summaryTable_" + iTableCounter).dataTable({
                  "bJQueryUI": true,
                  "bFilter": false,
                  "aaData": detailsRowData,
                  "bSort" : true, // disables sorting
                  "aoColumns": [
                    { "mDataProp": "sourceCellLine"},
                    { "mDataProp": "annotCellLine"},
                    { "mDataProp": "sourceCellType"},
                    { "mDataProp": "sourceAnatomy"},
                    { "mDataProp": "sourceCellTreatment"},
                    { "mDataProp": "sourceSpecies"},
                    { "mDataProp": "sourceDisease"}
    	            ],
                  "bPaginate": true,
                  "oLanguage": {
					          "sInfo": "_TOTAL_ entries"
		              }
              });
              iTableCounter = iTableCounter + 1;
          }
      });


  });

$(document).ready(function() {
  $("#queue").DataTable();
});
