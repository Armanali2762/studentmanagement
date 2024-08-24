<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%@
taglib prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="form" uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmtt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page language="java"
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Student Management System</title>

    <!-- Bootstrap CSS -->
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
      integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
      crossorigin="anonymous"
    />

    <!-- jQuery from Google CDN -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <!-- DataTables JS -->
    <link
      rel="stylesheet"
      type="text/css"
      href="//cdn.datatables.net/2.0.8/css/dataTables.dataTables.min.css"
    />
    <script
      type="text/javascript"
      src="//cdn.datatables.net/2.0.8/js/dataTables.min.js"
    ></script>
    <style>
      /* Custom styles */
      #addStudentForm {
        max-width: 500px;
        margin: auto;
      }

      #editStudentForm {
        max-width: 500px;
        margin: auto;
      }

      #editStudentModal {
        display: none;
        background: rgba(0, 0, 0, 0.5);
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9999;
        padding: 20px;
        overflow-y: auto;
      }

      /* Modal styles */
      #addStudentModal {
        display: none;
        background: rgba(0, 0, 0, 0.5);
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9999;
        padding: 20px;
        overflow-y: auto;
      }

      .modal-content {
        background: white;
        padding: 20px;
        max-width: 500px;
        margin: auto;
        position: relative;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      }

      .close {
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
      }

      .modal-content h4 {
        margin-top: 0;
      }

      .markTable {
        width: 100%;
        border-collapse: collapse;
      }

      .markTable th,
      .markTable td {
        padding: 10px;
        text-align: center;
        border: 1px solid #ddd;
      }

      .markTable th {
        background-color: #f2f2f2;
      }

      .markTable input[type="number"] {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
        border: 1px solid #ccc;
        border-radius: 4px;
        transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
      }

      .markTable input[type="number"]:focus {
        outline: none;
        border-color: #007bff;
        box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
      }
    </style>
  </head>

  <body>

    <!--Edit Student Modal-->
    <div id="editStudentModal" class="modal">
      <div class="modal-content" id="content">
        <span class="close" id="closeEditStudentModal">&times;</span>
        <h2 class="mb-4">Edit Student Form</h2>
        <form id="editStudentForm" method="post">
          <div class="form-group">
            <label for="editName">Name</label>
            <input
              type="text"
              class="form-control"
              id="editName"
              name="name"
              spellcheck="true"
              required
            />
          </div>

          <div class="form-group">
            <label>Gender</label><br />
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                name="gender"
                id="editMale"
                value="Male"
                required
              />
              <label class="form-check-label" for="editMale">Male</label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                name="gender"
                id="editFemale"
                value="Female"
                required
              />
              <label class="form-check-label" for="editFemale">Female</label>
            </div>
          </div>

          <div class="form-group">
            <label for="editCourses">Courses</label><br />
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                name="course"
                id="editCourse1"
                value="Computer Science"
              />
              <label class="form-check-label" for="editCourse1"
                >Computer Science</label
              >
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                name="course"
                id="editCourse2"
                value="Mathematics"
              />
              <label class="form-check-label" for="editCourse2"
                >Mathematics</label
              >
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                name="course"
                id="editCourse3"
                value="History"
              />
              <label class="form-check-label" for="editCourse3">History</label>
            </div>
          </div>

          <div class="form-group">
            <label for="editCountry">Country</label>
            <select
              class="form-control"
              id="editCountry"
              name="country"
              required
            >
              <option value="">Select Country</option>
              <option value="India">India</option>
              <option value="China">China</option>
              <option value="Japan">Japan</option>
            </select>
          </div>

          <div class="form-group">
            <label for="editState">State</label>
            <select
              class="form-control"
              id="editState"
              name="state"
              required
            ></select>
          </div>

          <div class="form-group">
            <label for="editCity">City</label>
            <select
              class="form-control"
              id="editCity"
              name="city"
              required
            ></select>
          </div>

          <div class="form-group">
            <label for="editAddress">Address</label>
            <input
              type="text"
              class="form-control"
              id="editAddress"
              name="address"
              value="${student.address}"
              spellcheck="true"
              required
            />
          </div>

          <button type="submit" class="btn btn-primary mr-2">
            Save Changes
          </button>
          <a href="index.jsp" class="btn btn-secondary">Cancel</a>
        </form>
      </div>
    </div>
    <div class="container">
      <div class="text-center mt-5">
        <h2>Welcome To Student Management System</h2>
        <button type="button" class="btn btn-primary" id="openAddStudentModal">
          Add Student
        </button>
      </div>

      <!-- Add Student Modal -->
      <div id="addStudentModal">
        <div class="modal-content">
          <span class="close" id="closeAddStudentModal">&times;</span>
          <h3 class="mb-4">Add Student Form</h3>
          <form id="addStudentForm" onsubmit="return false;">
            <div class="form-group">
              <label for="name">Name</label>
              <input
                type="text"
                class="form-control"
                id="name"
                name="name"
                spellcheck="true"
                required
              />
            </div>
            <div class="form-group">
              <label>Gender</label><br />
              <div class="form-check form-check-inline">
                <input
                  class="form-check-input"
                  type="radio"
                  name="gender"
                  id="male"
                  value="male"
                  required
                />
                <label class="form-check-label" for="male">Male</label>
              </div>
              <div class="form-check form-check-inline">
                <input
                  class="form-check-input"
                  type="radio"
                  name="gender"
                  id="female"
                  value="female"
                  required
                />
                <label class="form-check-label" for="female">Female</label>
              </div>
            </div>
            <div class="form-group">
              <div class="form-check form-check-inline">
                <input
                  class="form-check-input"
                  name="course"
                  type="checkbox"
                  id="course1"
                  value="Computer Science"
                />
                <label class="form-check-label" for="course1"
                  >Computer Science</label
                >
              </div>
              <div class="form-check form-check-inline">
                <input
                  class="form-check-input"
                  name="course"
                  type="checkbox"
                  id="course2"
                  value="Mathematics"
                />
                <label class="form-check-label" for="course2"
                  >Mathematics</label
                >
              </div>
              <div class="form-check form-check-inline">
                <input
                  class="form-check-input"
                  name="course"
                  type="checkbox"
                  id="course3"
                  value="History"
                />
                <label class="form-check-label" for="course3">History</label>
              </div>
            </div>
            <div class="form-group">
              <label for="country">Country</label>
              <select class="form-control" name="country" id="country" required>
                <option value="">Select Country</option>
                <option value="India">India</option>
                <option value="China">China</option>
                <option value="Japan">Japan</option>
              </select>
            </div>
            <div class="form-group">
              <label for="state">State</label>
              <select
                class="form-control"
                id="state"
                name="state"
                required
              ></select>
            </div>
            <div class="form-group">
              <label for="city">City</label>
              <select
                class="form-control"
                name="city"
                id="city"
                required
              ></select>
            </div>
            <div class="form-group">
              <label for="address">Address</label>
              <input
                type="text"
                class="form-control"
                id="address"
                name="address"
                spellcheck="true"
                required
              />
            </div>
            <div class="form-group mt-5">
              <h4>Add Student Marks</h4>
              <table name="studentMarks" class="markTable mt-2">
                <thead>
                  <tr>
                    <th>Mathematics</th>
                    <th>Science</th>
                    <th>English</th>
                    <th>Hindi</th>
                    <th>
                      <a class="btn btn-secondary" onclick="addRow()">+</a>
                    </th>
                  </tr>
                </thead>
                <tbody id="tBody">
                  <tr id="tRow">
                    <td>
                      <input
                        type="number"
                        name="math0"
                        id="mathmarks"
                        placeholder="enter math marks"
                        required
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        name="science0"
                        id="sciencemarks"
                        placeholder="enter science marks"
                        required
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        name="english0"
                        id="englishmarks"
                        placeholder="enter english marks"
                        required
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        name="hindi0"
                        id="hindimarks"
                        placeholder="enter hindi marks"
                        required
                      />
                    </td>
                    <td>
                      <a class="btn btn-primary" onclick="delRow(this)">-</a>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <input hidden type="number" name="countrow" id="totlerow" value="1" />
            <button type="button" id="submitForm" class="btn btn-primary">
              Submit
            </button>
            <button type="reset" id="resetForm" class="btn btn-primary">
              Reset
            </button>
          </form>
        </div>
      </div>
      <!-- Student Data Table -->
      <!-- <div class="mt-1">
        <table
          id="dataTable"
          class="table table-striped table-bordered"
          style="width: 100%"
        >
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Gender</th>
              <th>Course</th>
              <th>Country</th>
              <th>State</th>
              <th>City</th>
              <th>Address</th>
              <th>Edit</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody></tbody>
        </table>
      </div> -->

      
      <div class="table-container">
        <table id="datatable" class="box table table-hover table-condensed table-bordered" cellspacing="0">
          <thead id="header">
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/javascript/Pagination.js"></script>
    <script>

    url = "pagination/data/" + 1;
		var buttonsParam = {
			list: {
				link: "company-bank-details-list"
			},
			file: {
				name: "Company Bank Details List"
			},
			ajaxDaata: {
				value: 1,
				topHeaderUrl: 'pagination/table-header/'+1,
				actUrl1: 'pagination/header/'+1,      
				dataUrl: url
			}
		};
		loadDTAjaxV2(buttonsParam);
    
      function addRow() {
        var maxrow = 3;
        var currentRow = $("#tBody tr").length;
        $("#totlerow").val(currentRow);
        if (currentRow < maxrow) {
          var v = $("#tRow").clone().appendTo("#tBody");
          $(v).find("input").val("");
          var subject = ["math", "science", "english", "hindi"];
          $(v)
            .find("input")
            .each(function (index) {
              $(this).attr("name", subject[index] + currentRow);
            });
          $("#totlerow").val(currentRow + 1);
        } else {
          alert("Sorry you can only create 2 rows !!");
        }
      }
      function delRow(ref) {
        var len = $("#tBody tr").length;
        if (len <= 1) {
          alert("Sorry, you can't remove further !!");
          return;
        }
        $(ref).parent().parent().remove();
      }
      // <!-- This funtion for delete the student By Id -->
      function deleteStudent(studentId) {
        if (confirm("Are you sure you want to delete this student?")) {
          $.ajax({
            type: "DELETE",
            url: "/deletestudent/" + studentId,
            success: function (response) {
              console.log("Student deleted successfully");
              alert("Student deleted successfully !!");
              var table = $("#dataTable").DataTable();
              table.destroy();
              fetchStudents();
            },
            error: function (xhr, status, error) {
              console.error("Error deleting student:", error);
              alert("something went wrong !!");
            },
          });
        }
      }

      $("#country").change(function () {
        var country = $(this).val();
        var states = {
          India: ["Select State", "Delhi", "Maharashtra", "Karnataka"],
          China: ["Select State", "Beijing", "Shanghai", "Guangdong"],
          Japan: ["Select State", "Tokyo", "Osaka", "Kyoto"],
        };
        var stateOptions = states[country];
        $("#state").empty();
        $("#state").append(
          $("<option>").text("Select State").attr("value", "")
        );
        stateOptions.forEach(function (state) {
          $("#state").append($("<option>").text(state).attr("value", state));
        });
        $("#city").empty();
      });

      $("#state").change(function () {
        var state = $(this).val();
        var cities = {
          Delhi: ["Select City", "New Delhi", "North Delhi", "South Delhi"],
          Maharashtra: ["Select City", "Mumbai", "Pune", "Nagpur"],
          Karnataka: ["Select City", "Bangalore", "Mysore", "Hubli"],
          Beijing: ["Select City", "Chaoyang", "Haidian", "Dongcheng"],
          Shanghai: ["Select City", "Pudong", "Hongkou", "Jingan"],
          Guangdong: ["Select City", "Guangzhou", "Shenzhen", "Dongguan"],
          Tokyo: ["Select City", "Shinjuku", "Shibuya", "Chiyoda"],
          Osaka: ["Select City", "Osaka City", "Sakai", "Higashi Osaka"],
          Kyoto: ["Select City", "Kyoto City", "Fushimi", "Nakagyo"],
        };
        var cityOptions = cities[state];
        $("#city").empty();
        $("#city").append($("<option>").text("Select City").attr("value", ""));
        cityOptions.forEach(function (city) {
          $("#city").append($("<option>").text(city).attr("value", city));
        });
      });
      function fetchStudents() {
        $("#dataTable").DataTable({
          processing: true,
          serverSide: true,
          ajax: "/getstudents",
        });
      }
      function updateStudent(studentId) {
        $.ajax({
          url: "/editStudent/" + studentId,
          type: "GET",
          success: function (student) {
            console.log("Student data:", student);
            $("#editName").val(student.name);
            if (student.gender == "Male") {
              $("#editMale").prop("checked", true);
            } else {
              $("#editFemale").prop("checked", true);
            }
            if (student.course.includes("Computer Science")) {
              $("#editCourse1").prop("checked", true);
            }
            if (student.course.includes("Mathematics")) {
              $("#editCourse2").prop("checked", true);
            }
            if (student.course.includes("History")) {
              $("#editCourse3").prop("checked", true);
            }
            $("#editAddress").val(student.address);

            var states = {
              India: ["Select State", "Delhi", "Maharashtra", "Karnataka"],
              China: ["Select State", "Beijing", "Shanghai", "Guangdong"],
              Japan: ["Select State", "Tokyo", "Osaka", "Kyoto"],
            };
            var stateOptions = states[student.country];
            $("#editState").empty();
            $("#editState").append(
              $("<option>").text("Select State").attr("value", "")
            );
            stateOptions.forEach(function (state) {
              $("#editState").append(
                $("<option>").text(state).attr("value", state)
              );
            });

            var cities = {
              Delhi: ["Select City", "New Delhi", "North Delhi", "South Delhi"],
              Maharashtra: ["Select City", "Mumbai", "Pune", "Nagpur"],
              Karnataka: ["Select City", "Bangalore", "Mysore", "Hubli"],
              Beijing: ["Select City", "Chaoyang", "Haidian", "Dongcheng"],
              Shanghai: ["Select City", "Pudong", "Hongkou", "Jingan"],
              Guangdong: ["Select City", "Guangzhou", "Shenzhen", "Dongguan"],
              Tokyo: ["Select City", "Shinjuku", "Shibuya", "Chiyoda"],
              Osaka: ["Select City", "Osaka City", "Sakai", "Higashi Osaka"],
              Kyoto: ["Select City", "Kyoto City", "Fushimi", "Nakagyo"],
            };
            var cityOptions = cities[student.state];
            $("#editCity").empty();
            $("#editCity").append(
              $("<option>").text("Select City").attr("value", "")
            );
            cityOptions.forEach(function (city) {
              $("#editCity").append(
                $("<option>").text(city).attr("value", city)
              );
            });
            $("#editCountry").val(student.country);
            $("#editState").val(student.state);
            $("#editCity").val(student.city);

            $("#editStudentModal").css("display", "block");

            $("#editStudentForm").attr(
              "action",
              "/updateStudent/" + student.id
            );
          },
          error: function (error) {
            console.error("Error fetching student data:", error);
            alert("Failed to fetch student data.");
          },
        });
        $("#closeEditStudentModal").on("click", function () {
          $("#editStudentModal").css("display", "none");
        });
        var tableRef = $("#dataTable").DataTable();
        $(tableRef).destroy();
      }

      $(document).ready(function () {
        fetchStudents();
        $("#openAddStudentModal").on("click", function () {
          $("#addStudentModal").css("display", "block");
        });

        $("#closeAddStudentModal").on("click", function () {
          $("#addStudentModal").css("display", "none");
        });

        // Submit form button click handler
        $("#submitForm").on("click", function () {
          if (validateForm()) {
            var formData = $("#addStudentForm").serialize();
            $.ajax({
              type: "POST",
              url: "/savestudent",
              data: formData,
              success: function (response) {
                console.log("Student added successfully");
                alert("Student added successfully");
                $("#addStudentModal").css("display", "none");
                $("#addStudentForm")[0].reset();
                var ref = $("");
                var table = $("#dataTable").DataTable();
                table.destroy();
                fetchStudents();
              },
              error: function (xhr, status, error) {
                console.error("Error adding student:", error);
              },
            });
          }
        });

        // Reset form button click handler
        $("#resetForm").on("click", function () {
          $("#addStudentForm")[0].reset();
        });
      });
      function validateForm() {
        var name = document.getElementById("name").value.trim();
        var genderChecked = document.querySelector(
          'input[name="gender"]:checked'
        );
        var coursesChecked = document.querySelector(
          'input[name="course"]:checked'
        );
        var country = document.getElementById("country").value;
        var state = document.getElementById("state").value;
        var city = document.getElementById("city").value;
        var address = document.getElementById("address").value.trim();

        var mathmarks = document.getElementById("mathmarks");
        var sciencemarks = document.getAnimations("sciencemarks");
        var englishmarks = document.getElementById("englishmarks");
        var hindimarks = document.getElementById("hindimarks");

        //Validate Marks
        if(mathmarks && sciencemarks && englishmarks && hindimarks > 100 || mathmarks && sciencemarks && englishmarks && hindimarks < 0 ){
          alert("Please enter correct marks")
          return false;
        }

        // Validate NameS
        if (name === "") {
          alert("Please enter a name.");
          return false;
        }

        // Validate Gender
        if (!genderChecked) {
          alert("Please select a gender.");
          return false;
        }

        // Validate Courses (At least one course must be selected)
        if (!coursesChecked) {
          alert("Please select at least one course.");
          return false;
        }

        // Validate Country
        if (country === "") {
          alert("Please select a country.");
          return false;
        }

        // Validate State
        if (state === "") {
          alert("Please select a state.");
          return false;
        }

        // Validate City
        if (city === "") {
          alert("Please select a city.");
          return false;
        }

        // Validate Address
        if (address === "") {
          alert("Please enter an address.");
          return false;
        }

        // Form validation successful
        return true;
      }
      
    </script>
    
  </body>
</html>
