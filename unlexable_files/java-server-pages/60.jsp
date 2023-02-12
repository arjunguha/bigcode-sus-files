<%-- 
    Document   : manageMinistryAcc
    Created on : Oct 15, 2021, 8:12:11 PM
    Author     : user
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%> 

<sql:setDataSource 
    var="data" 
    driver="org.apache.derby.jdbc.ClientDriver"     
    url="jdbc:derby://localhost:1527/sample"
    user="app"  
    password="app"/> 

<sql:query             
    dataSource="${data}" 
    var="resultMS">
    SELECT * from vsuser WHERE userrole = 'MS'
</sql:query>

<sql:query             
    dataSource="${data}" 
    var="resultCS">
    SELECT * from vsuser WHERE userrole = 'CS'
</sql:query>

<sql:query             
    dataSource="${data}" 
    var="resultPU">
    SELECT * from vsuser WHERE userrole = 'PU'
</sql:query>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Ministry Staff Account Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">   
    </head>
    <body>
        <jsp:include page="bannerMinistry.jsp"/>
        <div class="container p-3">
            <h1>User Management</h1>
            <br>
            <!-- Tabs navs -->
            <ul class="nav nav-tabs mb-3" id="ex1" role="tablist">
                <li class="nav-item" role="presentation">
                    <a
                        class="nav-link active"
                        id="ministry-tab"
                        data-bs-toggle="tab"
                        data-bs-target="#ministry"
                        type="button"
                        aria-controls="ministry"
                        aria-selected="true"
                        >Ministry</a
                    >
                </li>
                <li class="nav-item" role="presentation">
                    <a
                        class="nav-link"
                        id="clinic-tab"
                        data-bs-toggle="tab"
                        data-bs-target="#clinic"
                        type="button"
                        aria-controls="clinic"
                        aria-selected="false"
                        >Clinic</a
                    >
                </li>
                <li class="nav-item" role="presentation">
                    <a
                        class="nav-link"
                        id="publicUser-tab"
                        data-bs-toggle="tab"
                        data-bs-target="#publicUser"
                        type="button"
                        aria-controls="publicUser"
                        aria-selected="false"
                        >Public User</a
                    >
                </li>
            </ul>

            <!-- Tabs content -->
            <div class="tab-content" id="tab-content">
                <div class="tab-pane fade show active" id="ministry" role="tabpanel" aria-labelledby="ministry-tab">
                    <form action="DeleteMinistryAcc" method="POST">
                        <div class="row">
                            <h3>Ministry Staff</h3>
                            <div class="row">
                                <div class="col-sm-2">
                                    <div class="form-floating mb">
                                        <input class="form-control me-2" id="inputName" type="search" placeholder="Search for names" aria-label="Search" onkeyup="filterName()">
                                        <label for="floatingInput">Search for names</label>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <table class="table table-striped" id="ministryTable">
                                <thead>
                                    <tr>
                                        <th scope="col">*</th>
                                        <th scope="col">Username</th>
                                        <th scope="col">Password</th>
                                        <th scope="col">Role</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">Gender</th>
                                        <th scope="col">Phone</th>
                                        <th scope="col">IC</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Address</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${resultMS.rows}" var="row">
                                        <tr>
                                            <td><input class="form-check-input" type="checkBox" name="checkBox1" value="${row.id}"></td>
                                            <td style="width: 10%"><c:out value="${row.id}"/></td>
                                            <td style="width: 10%"><c:out value="${row.password}"/></td>
                                            <td style="width: 5%"><c:out value="${row.userRole}"/></td>
                                            <td style="width: 10%"><c:out value="${row.name}"/></td>
                                            <td style="width: 7%"><c:out value="${row.gender}"/></td>
                                            <td style="width: 10%"><c:out value="${row.phone}"/></td>
                                            <td style="width: 10%"><c:out value="${row.ic}"/></td>
                                            <td style="width: 10%"><c:out value="${row.email}"/></td>
                                            <td style="width: 23%"><c:out value="${row.address}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row justify-content-center"> 
                                <div class="col-sm-3 text-center">
                                    <div class="row">
                                        <div class="col">
                                            <input class="btn btn-danger" type="submit" value="Detele">
                                        </div>
                                        <div class="col">
                                            <a href="updateMinistryPage.jsp" class="btn btn-success" role="button">Edit</a>
                                        </div>
                                        <div class="col">
                                            <a href="addMinistryPage.jsp" class="btn btn-primary" tabindex="-1" role="button">Add</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="clinic" role="tabpanel" aria-labelledby="clinic-tab">
                    <form action="DeleteClinicAcc" method="POST">
                        <div class="row">
                            <h3>Clinic</h3>
                            <div class="row">
                                <div class="col-sm-2">
                                    <div class="form-floating mb">
                                        <input class="form-control me-2" id="inputNameCS" type="search" placeholder="Search for names" aria-label="Search" onkeyup="filterNameCS()">
                                        <label for="floatingInput">Search for clinic names</label>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <table class="table table-striped" id="clinicTable">
                                <thead>
                                    <tr>
                                        <th scope="col">*</th>
                                        <th scope="col">Username</th>
                                        <th scope="col">Password</th>
                                        <th scope="col">Clinic ID</th>
                                        <th scope="col">Clinic Name</th>
                                        <th scope="col">Clinic Address</th>
                                        <th scope="col">State</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">Gender</th>
                                        <th scope="col">Phone</th>
                                        <th scope="col">IC</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Address</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${resultCS.rows}" var="row">
                                        <tr>
                                            <td><input class="form-check-input" type="checkBox" name="checkBox2" value="${row.id}"></td>
                                            <td><c:out value="${row.id}"/></td>
                                            <td><c:out value="${row.password}"/></td>
                                            <td><c:out value="${row.clinicId}"/></td>
                                            <td><c:out value="${row.clinicName}"/></td>
                                            <td><c:out value="${row.clinicAddress}"/></td>
                                            <td><c:out value="${row.clinicState}"/></td>
                                            <td><c:out value="${row.name}"/></td>
                                            <td><c:out value="${row.gender}"/></td>
                                            <td><c:out value="${row.phone}"/></td>
                                            <td><c:out value="${row.ic}"/></td>
                                            <td><c:out value="${row.email}"/></td>
                                            <td><c:out value="${row.address}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row justify-content-center"> 
                                <div class="col-sm-2 text-center">
                                    <div class="row">
                                        <div class="col">
                                            <input class="btn btn-danger" type="submit" value="Detele">
                                        </div>
                                        <div class="col">
                                            <a href="updateClinicPage.jsp" class="btn btn-success" role="button">Edit</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane fade" id="publicUser" role="tabpanel" aria-labelledby="publicUser-tab">
                    <form action="DeletePublicUserAcc" method="POST">
                        <div class="row">
                            <h3>Public User</h3>
                            <div class="row">
                                <div class="col-sm-2">
                                    <div class="form-floating mb">
                                        <input class="form-control me-2" id="inputNamePU" type="search" placeholder="Search for names" aria-label="Search" onkeyup="filterNamePU()">
                                        <label for="floatingInput">Search for names</label>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <table class="table table-striped" id="publicUserTable">
                                <thead>
                                    <tr>
                                        <th scope="col">*</th>
                                        <th scope="col">Username</th>
                                        <th scope="col">Password</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">Gender</th>
                                        <th scope="col">Phone</th>
                                        <th scope="col">IC</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Address</th>
                                        <th scope="col">Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${resultPU.rows}" var="row">
                                        <tr>
                                            <td><input class="form-check-input" type="checkbox" name="checkBox3" value="${row.id}"></td>
                                            <td><c:out value="${row.id}"/></td>
                                            <td><c:out value="${row.password}"/></td>
                                            <td><c:out value="${row.name}"/></td>
                                            <td><c:out value="${row.gender}"/></td>
                                            <td><c:out value="${row.phone}"/></td>
                                            <td><c:out value="${row.ic}"/></td>
                                            <td><c:out value="${row.email}"/></td>
                                            <td><c:out value="${row.address}"/></td>
                                            <td><c:out value="${row.vaccinationStatus}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="row justify-content-center"> 
                                <div class="col-sm-2 text-center">
                                    <div class="row">
                                        <div class="col">
                                            <input class="btn btn-danger" type="submit" value="Detele">
                                        </div>
                                        <div class="col">
                                            <a href="updatePublicUserPage.jsp" class="btn btn-success" role="button">Edit</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <br>

        </div>

        <!-- Modal -->
        <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="staticBackdropLabel">Modal title</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
                        <button type="submit" class="btn btn-primary">Yes</button>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.min.js" integrity="sha384-j0CNLUeiqtyaRmlzUHCPZ+Gy5fQu0dQ6eZ/xAww941Ai1SxSY+0EQqNXNE6DZiVc" crossorigin="anonymous"></script>
        <script>
                                            function filterName() {
                                            // Declare variables
                                            var input, filter, table, tr, td, i, txtValue;
                                            input = document.getElementById("inputName");
                                            filter = input.value.toUpperCase();
                                            table = document.getElementById("ministryTable");
                                            tr = table.getElementsByTagName("tr");
                                            // Loop through all table rows, and hide those who don't match the search query
                                            for (i = 0; i < tr.length; i++) {
                                            td = tr[i].getElementsByTagName("td")[4];
                                            if (td) {
                                            txtValue = td.textContent || td.innerText;
                                            if (txtValue.toUpperCase().indexOf(filter) > - 1) {
                                            tr[i].style.display = "";
                                            } else {
                                            tr[i].style.display = "none";
                                            }
                                            }
                                            }
                                            }
                                            function filterNameCS() {
                                            // Declare variables
                                            var input, filter, table, tr, td, i, txtValue;
                                            input = document.getElementById("inputNameCS");
                                            filter = input.value.toUpperCase();
                                            table = document.getElementById("clinicTable");
                                            tr = table.getElementsByTagName("tr");
                                            // Loop through all table rows, and hide those who don't match the search query
                                            for (i = 0; i < tr.length; i++) {
                                            td = tr[i].getElementsByTagName("td")[4];
                                            if (td) {
                                            txtValue = td.textContent || td.innerText;
                                            if (txtValue.toUpperCase().indexOf(filter) > - 1) {
                                            tr[i].style.display = "";
                                            } else {
                                            tr[i].style.display = "none";
                                            }
                                            }
                                            }
                                            }
                                            function filterNamePU() {
                                            // Declare variables
                                            var input, filter, table, tr, td, i, txtValue;
                                            input = document.getElementById("inputNamePU");
                                            filter = input.value.toUpperCase();
                                            table = document.getElementById("publicUserTable");
                                            tr = table.getElementsByTagName("tr");
                                            // Loop through all table rows, and hide those who don't match the search query
                                            for (i = 0; i < tr.length; i++) {
                                            td = tr[i].getElementsByTagName("td")[3];
                                            if (td) {
                                            txtValue = td.textContent || td.innerText;
                                            if (txtValue.toUpperCase().indexOf(filter) > - 1) {
                                            tr[i].style.display = "";
                                            } else {
                                            tr[i].style.display = "none";
                                            }
                                            }
                                            }
                                            }

        </script>
    </body>
</html>
