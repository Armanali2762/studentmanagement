/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var allRowArray = [];
var slectedRowArray = [];
var selectedCheckboxArray = [];
var selectedCheckboxArrayNew = [];
var buttonLen = 0;
var buttons = [];

function loadDTDTaskPagination(aurl, aurl1, aurl2, fpk, aLink, eLink, dLink, rlink, fname, expression, help) {
	loadSpinnerPage();

	tablelist = '';
	allRowArray = [];
	slectedRowArray = [];
	buttonLen = 0;
	buttons = [];
	if (aLink.link !== "") {

		buttons.push({
			text: '<i class=\"fa fa-lg fa-plus\"></i> ' + aLink.name,
			titleAttr: 'Add New Record',
			action: function(e, dt, node, config) {
				if (aLink.name == 'Add') {
					$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + aLink.name + " " + $("#headerText").text());
				} else {
					$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + aLink.name);
				}
				$("#contentcontent").removeClass("hide");
				$("#formcontent").load(aLink.link);
			}

		});
		buttonLen++;
	}

	if (eLink !== "") {
		buttons.push({
			text: '<i class=\"fa fa-lg fa-pencil\"></i> Edit',
			titleAttr: 'Edit Record',
			action: function(e, dt, node, config) {
				var checkedList = tablelist.rows('.active').data();
				if (slectedRowArray.length > 0) {
					if (slectedRowArray.length > 1) {
						BootstrapDialog.alert("To perform this operation, please select only one row of record.");
					} else {

						if (checkedList[0].Status === 'Work In Progress' || checkedList[0].Status === 'Completed') {
							BootstrapDialog.alert("Task '" + checkedList[0].Status + "' can't be edited. ")
						} else {
							$(".header_name").html('<i class=\"fa fa-lg fa-pencil-square\"></i> Edit' + $("#headerText").text());
							$("#contentcontent").removeClass("hide");
							//to Edit  button hiberlink
							$("#formcontent").load(eLink + "?pk=" + slectedRowArray[0]);
							document.getElementById('contentcontent').scrollIntoView();
						}
					}

				} else {
					BootstrapDialog.alert("Select any row for edit.");
				}
			}
		});
		buttonLen++;
	}

	if (dLink !== "") {
		buttons.push({
			text: '<i class=\"fa fa-lg fa-trash\"></i> Delete',
			titleAttr: 'Delete Record',
			action: function(e, dt, node, config) {
				var pks = '';
				if (slectedRowArray.length > 0) {
					for (var i = 0; i < slectedRowArray.length; i++) {
						if (i == 0) {
							pks += slectedRowArray[i];
						} else {
							pks += ',' + slectedRowArray[i];
						}
					}
				}
				if (slectedRowArray.length > 0) {
					if (confirm("Are you sure you want to delete?"))
						//to delete ajax submission
						$.ajax({
							type: "GET",
							url: dLink,
							data: {
								"pks": pks
							},
							datatype: "json",
							async: false,
							timeout: 5000,
							cache: false,
							beforeSend: function(xhr, options) {

							},
							success: function(data) {
								if (data.deletedRecordCounter == 0) {
									BootstrapDialog.alert("Task(s) already in use !")
								} else {
									BootstrapDialog.alert(data.deletedRecordCounter + " Record  deleted successfully !");
									var ids = data.deletedPks.toString().trim().split(",");
									for (var y = 0; y < ids.length; y++) {
										tablelist.row("#" + ids[y]).remove().draw();
										slectedRowArray.splice($.inArray(slectedRowArray[i], slectedRowArray), 1);
									}
								}
							},
							error: function(response) {
								$().toastmessage('showWarningToast', '<strong><i>Something went wrong!</i></strong><br>Error code: <i>' + response.statusText + '</i><br>Error Message: <i>' + response.status + '.</i>');
							}
						});
				} else {
					BootstrapDialog.alert("Select row for delete.");
				}
			}
		});
		buttonLen++;
	}
	if (expression === 1) {
		buttons.push({
			text: '<i class="fa fa-clone"></i> Clone',
			action: function(e, dt, node, config) {
				if (slectedRowArray.length > 0) {
					if (slectedRowArray.length > 1) {
						BootstrapDialog.alert("To perform this operation, please select only one row of record.");
					} else {
						$(".header_name").html("<i class='fa fa-clone'></i> Clone " + $("#headerText").text());
						$("#contentcontent").removeClass("hide");
						//to Edit  button hiberlink
						$("#formcontent").load("cloneTaskManagement?pk=" + slectedRowArray[0]);
					}
				} else {
					BootstrapDialog.alert("Select any row for view.");
				}
			}
		});
	}
	buttons.push({
		extend: 'pdfHtml5',
		orientation: 'landscape',
		pageSize: 'A4',
		customize: function(doc) {
			doc.pageMargins = [10, 10, 10, 10];
		},
		download: 'open',
		text: '<i class=\"fa fa-lg fa-file-pdf-o\"></i>',
		titleAttr: 'Download as PDF',
		//        orientation: "landscape",
		//        size: '210mm 297mm',
		//        margin: '15mm',
		title: fname,
		exportOptions: {
			columns: ':visible'
		}
	});
	buttons.push({
		extend: 'print',
		autoPrint: false,
		text: '<i class=\"fa fa-lg fa-print\"></i>',
		titleAttr: ' Print Selected Data',
		exportOptions: {
			columns: ':visible'
		},
		title: fname,
		customize: function(win) {
			$(win.document.body)
				.css('font-size', '10pt')
				.find('h1').html("")
				.prepend(
					'<img class="pull-right" src="http://localhost:8080/YCCECE/resources/login/img/YCCECE_sis_logo_01.png" style="top: 5px;padding-right:10px;height: 40px;" /><a class="btn btn-primary  pull-right hidden-print printhide" style="margin-top: 10px;margin-right: 10px;" id="printbutton" href="javascript:window.print();"><i class="fa fa-print"></i></a> <h4 class="text-center">' + fname + '</h4>'
				);
			$(win.document.body).find('table')
				.addClass('compact')
				.css('font-size', 'inherit');
		}
	});
	buttons.push({
		extend: 'colvis',
		title: fname,
		text: '<i class=\"fa fa-lg fa-eye\"></i>',
		titleAttr: 'Filter Columns',
		exportOptions: {
			columns: ':visible'
		}

	});
	buttons.push({
		text: '<i class=\"fa fa-lg fa-refresh\"></i>',
		titleAttr: 'Refresh',
		action: function(e, dt, node, config) {
			$("#formcontent").removeClass("hide");
			//to add button hiberlink  
			loadForm(rlink, rolename, roleIDold);
			//loadForm(rlink);
		}
	});
	if (help != "") {

		buttons.push({
			text: '<b>Help</b>',
			titleAttr: 'Help',
			action: function(e, dt, node, config) {
				window.open(help, '_blank');
			}
		});
	}
	buttons.push({
		extend: 'excelHtml5',
		title: fname,
		text: '<i class=\"fa fa-lg fa-file-excel-o\"></i>',
		titleAttr: 'Download to Excel',
		exportOptions: {
			columns: ':visible'
		}
	});

	var filterrow = '<tr id="filterrow" class="no-print">';
	$.ajax({
		type: "GET",
		url: aurl,
		datatype: "json",
		success: function(jsonHeader) {
			var header = '<tr class="bg-teal">';
			$.each(jsonHeader, function(index, value) {

				header += '<th>' + value.mDataProp + '</th>';
				filterrow += '<th></th>';
			});
			$("#header").append(header + "</tr>");
			$("#header").append(filterrow + "</tr>");
			tablelist = $('#datatable').DataTable({
				"sPaginationType": "full_numbers",
				"bStateSave": false,
				"deferLoading": 0,
				"responsive": false,
				"bAutoWidth": false,
				"processing": true,
				"serverSide": true,
				"orderCellsTop": true,
				"scrollX": true,
				"ajax": aurl1,
				"aoColumnDefs": jsonHeader,
				"oLanguage": {
					"sSearch": "<span'>Filter records:</span>",
					"sLengthMenu": "<span'>Display _MENU_ records per page</span>",
					"sInfo": "<span'>_START_ - _END_ of _TOTAL_</span>"

				},
				"fnDrawCallback": function(oSettings) {
					try {
						populateSeqDropDown();
					} catch (eee) {
					}
				},
				"initComplete": function(oSettings) {
					console.log("initComplete 1");
					$.ajax({
						type: "GET",
						url:   "/v1/common/query-data/1",
						data: {
							pk: fpk
						},
						datatype: "json",
						success: function(data) {
							// $('#datatable tfoot tr#filterrow td')
							$('.dataTables_scroll thead tr:eq(1) th').each(function() {
								var title = $('#datatable thead th').eq($(this).index()).text();
								if (data[$(this).index()].is_searchable === true) {
									if (data[$(this).index()].searchable_type === true) {
										$(this).html('<select id="action">\n\
                                                            <option value="">All</option>\n\
                                                            <option value="0">No Action Yet</option> \n\
                                                            <option value="1">Work In Progress</option> \n\
                                                            <option value="2">Completed</option>\n\
                                                          </select>');
									} else {
										$(this).html('<input type="text" style="font-weight:normal;width:100%;border:0px;" placeholder="Search ' + title + '" />');
									}
								}
							});
							//                            $("#datatable tbody tr").each(function() { 
							$('.dataTables_scroll thead tr:eq(1)').each(function() {
								var id = this.id;
								if (id != "") {
									var index = $.inArray(id, allRowArray);
									if (index === -1) {
										allRowArray.push(id);
									} else {
										allRowArray.splice(index, 1);
									}
								}
							});
							//                            $("#datatable thead input").on('keyup change', function () {
							////
							//                                if (this.value.length >= 3) {
							//// Call the API search function
							//                                tablelist.column($(this).parent().index() + ':visible')
							//                                        .search(this.value)
							//                                        .draw();
							//                                }
							//// Ensure we clear the search if they backspace far enough
							//                                if (this.value == "") {
							//                                tablelist.column($(this).parent().index() + ':visible')
							//                                        .search(this.value)
							//                                        .draw();
							//                                }
							//                            });


							//                            $('#datatable thead input').keydown(function (event) {
							//
							//                                if (event.which === 9 || event.which === 13) {
							//
							//                                    tablelist.column($(this).parent().index() + ':visible')
							//                                            .search(this.value)
							//                                            .draw();
							//                                }
							//                            });
							//
							//                            $('#datatable thead input').keydown(function (event) {
							//
							//                                if (event.which === 9 || event.which === 13) {
							//
							//                                    tablelist.column($(this).parent().index() + ':visible')
							//                                            .search(this.value)
							//                                            .draw();
							//                                }
							//                            });


							/*  Search filter table reset*/
							$(".dataTables_scroll thead input").on('keyup change', function() {
								tablelist.column($(this).parent().index() + ':visible')
									.search(this.value)
									.draw();
							});
							$(".dataTables_scroll thead select").on('change', function() {
								tablelist.column($(this).parent().index() + ':visible')
									.search(this.value)
									.draw();
							});
						}
					});
				}

			});
			new $.fn.dataTable.Buttons(tablelist, {
				buttons: buttons
			});
			//            tablelist.button(buttonLen).text("<i class=\"fa fa-lg fa-file-excel-o\"></i>");
			//            tablelist.button(buttonLen + 1).text("<i class=\"fa fa-lg fa-file-pdf-o\"></i>");
			//            tablelist.button(buttonLen + 2).text("<i class=\"fa fa-lg fa-print\"></i>");
			//            tablelist.button(buttonLen + 3).text("<i class=\"fa fa-lg fa-eye\"></i>");
			$('#button_wrapper').append(tablelist.buttons(0, null).container());
			$('.dataTables_scroll tbody').on('click', 'tr', function() {
				var id = this.id;
				if (id != "") {
					var index = $.inArray(id, slectedRowArray);
					if (index === -1) {
						slectedRowArray.push(id);
					} else {
						slectedRowArray.splice(index, 1);
					}
					$(this).toggleClass('active');
				}
			});
		}

	});
}

function loadDTAjaxV2(buttonsParam) {
	debugger;
	//loadSpinnerPage();
	allRowArray = [];
	slectedRowArray = [];
	selectedCheckboxArray = [];
	selectedCheckboxArrayNew = [];
	buttonLen = 0;
	buttons = [];


	console.log(buttonsParam);

	switch (buttonsParam.ajaxDaata.value) {

		case -1:
			break;
		default:
			var defaultLink =   "pagination/default-data";
			var deferLoading = 1;
			if (typeof (buttonsParam.ajaxDaata.dataUrl !== 'undefined') && buttonsParam.ajaxDaata.dataUrl !== '') {
				defaultLink = buttonsParam.ajaxDaata.dataUrl;
				deferLoading = null;
			}

			
			var filterrow = '<tr id="filterrow" class="no-print">';
			$.ajax({
				type: "GET",
				url: buttonsParam.ajaxDaata.topHeaderUrl,
				datatype: "json",
				success: function(data) {
					$.ajax({
						type: "GET",
						url: buttonsParam.ajaxDaata.actUrl1,
						datatype: "json",
						success: function(colData) {

							if(data.status){

							var header = '<tr class="bg-primary text-white">';
							var flag = true;
							var row = 1;
							$.each(data.headerTitleList, function(index, value) {
								if (value.rowspan > 1) {
									flag = false;
									//                                    console.log('INNN 11')
									row = 2;
									header += '<th rowspan="' + value.rowspan + '">' + value.display_name + '</th>';
								} else {
									if (flag) {
										header += '<th rowspan="' + value.rowspan + '">' + value.display_name + '</th>';
									}
								}
								if (typeof (value.table_field) !== 'undefined'
									&& typeof (value.start_top_header) !== 'undefined'
									&& value.table_field !== null
									&& value.start_top_header !== null) {
									//                                    console.log('INNN')
									header += '<th colspan="' + value.colspan + '">' + value.table_field + '</th>';
								}
								filterrow += '<th></th>';

							});
							header += '</tr>';
							if (!flag) {
								header += '<tr class="bg-primary text-white">';
								$.each(data.headerSub, function(index, value) {
									header += '<th>' + value.display_name + '</th>';
								});
								header += "</tr>";
							}
							filterrow += "</tr>";
							$("#header").append(header);
							$("#header").append(filterrow);
							tablelist = $('#datatable').DataTable({
								"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
								"sPaginationType": "full_numbers",
								"bStateSave": false,
								"deferLoading": deferLoading,
								"responsive": false,
								"bAutoWidth": false,
								"processing": true,
								"serverSide": true,
								"orderCellsTop": true,
								"scrollX": true,
								"aaSorting": [],
								// "ajax": defaultLink,
								"ajax": {
									"url": defaultLink,
									"type": "GET",
									"dataSrc": function (json) {
										
										if (!json.status) {
											toastMessage('error', "Something went wrong! Please try again later.");
										}
										return json.data;
									},
									"error": function (xhr, error, thrown) {
										enableMenuLinks();
										if (xhr.status === 0) {
											toastMessage('error', "Network error: Please check your internet connection.");
										} else if (xhr.status === 401) {
											window.location.href =   "login?sessionExpired=true";
										}  else {
											toastMessage('error', "Something went wrong! Please try again later.");
										}
										// $('#datatable').DataTable().clear().draw();
									}
								},
								"columns": colData.columns,
								"columnDefs": colData.columnDefs,
								"oLanguage": {
									"sSearch": "<span'>Filter records:</span>",
									"sLengthMenu": "<span'>Display _MENU_ records per page</span>",
									"sInfo": "<span'>_START_ - _END_ of _TOTAL_</span>"

								},
								"fnDrawCallback": function(oSettings) {
									try {
										populateSeqDropDown();
									} catch (eee) {
									}
									//commonFileCount();
								},
								"initComplete": function(oSettings) {
									console.log('initComplete 2')
									
									$.ajax({
										type: "GET",
										url:   "/v1/common/query-data/1",
										data: {
											pk: buttonsParam.ajaxDaata.value
										},
										datatype: "json",
										success: function(data) {
											enableMenuLinks();
											$('.dataTables_scroll thead tr:eq(' + row + ') th').each(function() {
												var title = $('#datatable thead th').eq($(this).index()).text();
												if (data[$(this).index()].is_searchable === true) {
													if (data[$(this).index()].searchable_type === true) {
														var ind = data[$(this).index()].order;

														// AUTO FILL DROP DOWN CODE BELOW
														$(this).html('<select class="form-control" id="action' + data[$(this).index()].order + '"></select>');
														var checkedList = tablelist
															.columns(ind)
															.data()
															.eq(0)      // Reduce the 2D array into a 1D array of data
															.sort()       // Sort data alphabetically
															.unique()     // Reduce to unique values
															.join(',');
														var fieldArray = checkedList.split(",");
														$("#action" + ind).append('<option value="">All</option>');
														$.each(fieldArray, function(index, value) {
															$("#action" + ind).append('<option value="' + value + '">' + value + '</option>');
														});
													} else {

														$(this).html('<input type="text" style="font-weight:normal;width:100%;border:0px;" class="form-control" placeholder="Search ' + title + '" />');
													}
												}
											});
											//                                            $("#datatable tbody tr").each(function() {
											$('.dataTables_scroll thead tr:eq(' + row + ')').each(function() {
												var id = this.id;
												if (id != "") {
													var index = $.inArray(id, allRowArray);
													if (index === -1) {
														allRowArray.push(id);
													} else {
														allRowArray.splice(index, 1);
													}
												}
											});
											/*  Search filter table reset*/
											//                                            $("#datatable thead input").on('keyup change', function () {
											//
											//                                                if (this.value.length >= 3) {
											//// Call the API search function
											//                                                    tablelist.column($(this).parent().index() + ':visible')
											//                                                            .search(this.value)
											//                                                            .draw();
											//                                                }
											//// Ensure we clear the search if they backspace far enough
											//                                                if (this.value == "") {
											//                                                    tablelist.column($(this).parent().index() + ':visible')
											//                                                            .search(this.value)
											//                                                            .draw();
											//                                                }
											//                                            });
											//
											//                                            $('#datatable thead input').keydown(function (event) {
											//
											//                                                if (event.which === 9 || event.which === 13) {
											//
											//                                                    tablelist.column($(this).parent().index() + ':visible')
											//                                                            .search(this.value)
											//                                                            .draw();
											//                                                }
											//                                            });
											//
											//                                            $('#datatable thead input').keydown(function (event) {
											//
											//                                                if (event.which === 9 || event.which === 13) {
											//
											//                                                    tablelist.column($(this).parent().index() + ':visible')
											//                                                            .search(this.value)
											//                                                            .draw();
											//                                                }
											//                                            });



											/*  Search filter table reset*/
											$(".dataTables_scroll thead input").on('keyup', function() {
												tablelist.column($(this).parent().index() + ':visible')
													.search(this.value)
													.draw();
											});
											$(".dataTables_scroll thead select").on('change', function() {
												tablelist.column($(this).parent().index() + ':visible')
													.search(this.value)
													.draw();
											});
										},
										error: function (xhr, error, thrown) {
											enableMenuLinks();
											if (xhr.status === 0) {
												toastMessage('error', "Network error: Please check your internet connection.");
											} else if (xhr.status === 401) {
												window.location.href =   "login?sessionExpired=true";
											}  else {
												toastMessage('error', "Something went wrong! Please try again later.");
											}
											// $('#datatable').DataTable().clear().draw();
										}
									});
								}
							});
							new $.fn.dataTable.Buttons(tablelist, {
								buttons: buttons
							});
							$('#button_wrapper').append(tablelist.buttons(0, null).container());
							$('.dataTables_scroll tbody').on('click', 'tr', function() {
								var id = this.id;
								if (id != "") {
									var index = $.inArray(id, slectedRowArray);
									if (index === -1) {
										slectedRowArray.push(id);
									} else {
										slectedRowArray.splice(index, 1);
									}
									$(this).toggleClass('active');
								}
							});
						}else{
							enableMenuLinks();
							toastMessage('error', "Something went wrong! Please try again later.");
						}
						},
						error: function (xhr, error, thrown) {
							enableMenuLinks();
							if (xhr.status === 0) {
								toastMessage('error', "Network error: Please check your internet connection.");
							} else if (xhr.status === 401) {
								window.location.href =   "login?sessionExpired=true";
							}  else {
								toastMessage('error', "Something went wrong! Please try again later.");
							}
							// $('#datatable').DataTable().clear().draw();
						}

					});
				},
				error: function (xhr, error, thrown) {
					enableMenuLinks();
					if (xhr.status === 0) {
						toastMessage('error', "Network error: Please check your internet connection.");
					} else if (xhr.status === 401) {
						window.location.href =   "login?sessionExpired=true";
					}  else {
						toastMessage('error', "Something went wrong! Please try again later.");
					}
					// $('#datatable').DataTable().clear().draw();
				}

			});
	}

	switch (buttonsParam.list.link) {
		case "-1":

			break;
		default:

			{
				buttons.push({
					text: '<i class=\"fa fa-lg fa-refresh\"></i>',
					titleAttr: 'Refresh',
					action: function(e, dt, node, config) {
						$("#formcontent").removeClass("hide");
						loadMenu(buttonsParam.list.link);
					}
				});
			}

	}

	buttons.push({
		extend: 'pdfHtml5',
		orientation: 'landscape',
		pageSize: 'A4',
		customize: function(doc) {
			doc.pageMargins = [10, 10, 10, 10];
		},
		download: 'open',
		//        orientation: "landscape",
		title: buttonsParam.file.name,
		mColumns: 'visible',
		text: '<i class=\"fa fa-lg fa-file-pdf-o\"></i>',
		titleAttr: 'Download as PDF',
		exportOptions: {
			columns: ':visible'
		}


	});
	buttons.push({
		extend: 'print',
		autoPrint: false,
		title: buttonsParam.file.name,
		text: '<i class=\"fa fa-lg fa-print\"></i>',
		titleAttr: ' Print Selected Data',
		exportOptions: {
			columns: ':visible'
		},
		customize: function(win) {
			//            $(win.document.body).remove();
			$(win.document.body)
				.css('font-size', '10pt')

				.find('h1').html("")
				.prepend(
					'<img class="pull-right" src="http://localhost:8080/YCCECE/resources/login/img/YCCECE_sis_logo_01.png" style="top: 5px;padding-right:10px;height: 40px;" /><h4 class="text-center">' + fname + '</h4>'
				);

			$(win.document.body).find('table')
				.addClass('compact')
				.css('font-size', 'inherit');
		}
	});
	buttons.push({
		extend: 'colvis',
		title: buttonsParam.file.name,
		text: '<i class=\"fa fa-lg fa-eye\"></i>',
		titleAttr: 'Filter Columns',
		exportOptions: {
			columns: ':visible'
		}

	});

	buttons.push({
		extend: 'excelHtml5',
		title: buttonsParam.file.name,
		mColumns: 'visible',
		text: '<i class=\"fa fa-lg fa-file-excel-o\"></i>',
		titleAttr: 'Download to Excel',
		exportOptions: {
			columns: ':visible'
		}
	});

	switch (null != buttonsParam.editUserDefinedField && undefined != buttonsParam.editUserDefinedField
	&& buttonsParam.editUserDefinedField.value) {
		case 1:
			break;
		default:
			if (null != buttonsParam.editUserDefinedField && undefined != buttonsParam.editUserDefinedField
				&& buttonsParam.editUserDefinedField.name !== "") {
				console.log("enter")
				buttons.push({
					text: '<i class=\"fa fa-lg fa-pencil\"></i> ' + buttonsParam.editUserDefinedField.name,
					titleAttr: 'Edit Record',
					action: function(e, dt, node, config) {
						if (slectedRowArray.length > 0) {
							if (slectedRowArray.length > 1) {
								BootstrapDialog.alert("To perform this operation, please select only one row of record.");
							} else {
								if (typeof (buttonsParam.editUserDefinedField.displayName) !== "undefined" && buttonsParam.editUserDefinedField.displayName !== "") {
									$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + buttonsParam.editUserDefinedField.displayName);
								} else {
									if (buttonsParam.add.name == 'Edit') {
										$(".header_name").html('<i class="fa fa-lg fa-pencil-square"></i> ' + buttonsParam.editUserDefinedField.name + " " + $("#headerText").text());
									} else {
										$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + buttonsParam.editUserDefinedField.name);
									}
								}
								$("#contentcontent").removeClass("hide");
								//to Edit  button hiberlink
								$("#formcontent").load(buttonsParam.editUserDefinedField.link + "?pk=" + slectedRowArray[0]);
								document.getElementById('contentcontent').scrollIntoView();

							}

						} else {
							BootstrapDialog.alert("Select any row for edit.");
						}
					}
				});
			}
	}


	switch (null != buttonsParam.definePrerequisite && undefined != buttonsParam.definePrerequisite
	&& buttonsParam.definePrerequisite.value) {

		case 1:
			break;
		default:
			if (null != buttonsParam.definePrerequisite && undefined != buttonsParam.definePrerequisite
				&& buttonsParam.definePrerequisite.name !== "") {
				buttons.push({
					text: '<i class=\"fa fa-lg fa-pencil\"></i> ' + buttonsParam.definePrerequisite.name,
					titleAttr: 'Edit Record',
					action: function(e, dt, node, config) {
						if (slectedRowArray.length > 0) {
							if (slectedRowArray.length > 1) {
								BootstrapDialog.alert("To perform this operation, please select only one row of record.");
							} else {
								if (typeof (buttonsParam.definePrerequisite.displayName) !== "undefined" && buttonsParam.definePrerequisite.displayName !== "") {
									$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + buttonsParam.definePrerequisite.displayName);
								} else {
									if (buttonsParam.add.name == 'Edit') {
										$(".header_name").html('<i class="fa fa-lg fa-pencil-square"></i> ' + buttonsParam.definePrerequisite.name + " " + $("#headerText").text());
									} else {
										$(".header_name").html('<i class=\"fa fa-lg fa-plus-square\"></i> ' + buttonsParam.definePrerequisite.name);
									}
								}
								$("#contentcontent").removeClass("hide");
								//to Edit  button hiberlink
								$("#formcontent").load(buttonsParam.definePrerequisite.link + "?pk=" + slectedRowArray[0]);
								document.getElementById('contentcontent').scrollIntoView();

							}

						} else {
							BootstrapDialog.alert("Select any row for edit.");
						}
					}
				});
			}
	}


	switch (null != buttonsParam.coursesUpload && undefined != buttonsParam.coursesUpload
	&& buttonsParam.coursesUpload.value) {
		case 1:
			break;
		default:
			if (null != buttonsParam.coursesUpload && undefined != buttonsParam.coursesUpload
				&& buttonsParam.coursesUpload.name !== "") {
				console.log("enter")
				buttons.push({
					text: '<i class=\"fa fa-lg fa-upload\"></i> Upload Courses',
					titleAttr: 'Upload Courses',
					action: function(e, dt, node, config) {
						var sessionId = $('#sessionId').val();
						$('html, body').animate({ scrollTop: $('.content-header').offset().top }, 'slow');
						$(".header_name").html("<i class='fa fa-lg'></i> Upload Courses");
						$("#contentcontent").html();
						$("#contentcontent").removeClass("hide");
						$("#formcontent").load("uploadCourseTimeTable?sessionId=" + sessionId);
					}
				});
			}
	}



}

function loadSpinnerPage() {
	$("#formcontent").html('<div class="text-center row" style="margin-top:30px;"><i class="fa fa-circle-o-notch fa-spin fa-3x"></></div>');
}
$.getScript('resources/theme/AdminLTE-2.3.0/plugins/datatables/extensions/LoadDT/js/loadDTMethodForInventory.js', function() {
});


function resetMainRowSelectionForTwoDataTablePage(slectedRowArray) {
	if ($("#two_list").length > 0) {
		slectedRowArray = [];
		const myElement = document.getElementById(".dataTables_scroll");
		const allRows = myElement.querySelectorAll("tr");
		allRows.forEach((row) => {
			const id = row.getAttribute("id");
			const activeClass = row.getAttribute("class");
			if (row.classList.contains("active")) {
				slectedRowArray.push(id);
			}
		});
	}
	return slectedRowArray;
}