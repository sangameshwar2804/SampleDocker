<%@include file="../fragments/header.jsp"%>
<body>

	<div class="container-fluid ">
		<div class="card-header bg-info text-white p-0">
			<div style="text-align: center">
				<h4>Manage Users</h4>
			</div>
		</div>
		<br>
		<table id="tableitems"
			class="table table-condensed table-hover table-responsive-sm width=100%">
			<!-- 			<thead class="thead-light"> -->
			<thead>
				<tr>
					<th>Id</th>
					<th>UserID</th>
					<th>Email-Id</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Address</th>
					<!-- 					<th>Roles</th> -->

					<th><sec:authorize
							access="hasAuthority('ADMIN') or hasAuthority('DBA')">
						</sec:authorize> <sec:authorize access="hasAuthority('ADMIN')">
						</sec:authorize>Actions</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${users}" var="user">
					<tr>
						<td>${user.id}</td>
						<td>${user.username}</td>
						<td>${user.useremail}</td>
						<td>${user.userfirstname}</td>
						<td>${user.userlastname}</td>
						<td>${user.useraddress}</td>
						<%-- 						<td><c:forEach items="${roles}" var="list"> --%>
						<%-- 							-${list.name}-</c:forEach></td> --%>

						<td><sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR') or hasAuthority('VIEWER')">
								<a href="<c:url value='/export-user-json-${user.username}'  />"
									class="btn btn-outline-success btn-sm" target="_blank">{ }</a>
							</sec:authorize> <sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR') or hasAuthority('VIEWER')">
								<a href="<c:url value='/export-user-pdf-${user.username}' />"
									class="btn btn-outline-danger btn-sm" target="_blank"><i
									class="fas fa-file-pdf"></i></a>
							</sec:authorize> <sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR') or hasAuthority('VIEWER')">
								<a href="<c:url value='/export-user-csv-${user.username}' />"
									class="btn btn-outline-success btn-sm" target="_blank"><i
									class="fas fa-file-code"></i></a>
							</sec:authorize>
							<sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR') or hasAuthority('VIEWER')">
								<a href="<c:url value='/export-user-xml-${user.username}' />"
									class="btn btn-outline-warning btn-sm" target="_blank"><i
									class="fas fa-file-code"></i></a>
							</sec:authorize> <sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR') or hasAuthority('VIEWER')">
								<a href="<c:url value='/view-user-${user.username}' />"
									class="btn btn-outline-info btn-sm"><i class="fas fa-user"></i></a>

							</sec:authorize> <sec:authorize
								access="hasAuthority('ADMIN') or hasAuthority('EDITOR')">
								<a href="<c:url value='/edit-user-${user.username}' />"
									class="btn btn-primary btn-sm"><span class="far fa-edit"></span></a>
							</sec:authorize> <sec:authorize access="hasAuthority('ADMIN')">
								<a href="<c:url value='/delete-user-${user.username}' />"
									class="btn btn-danger btn-sm delBtn"><span
									class="fas fa-trash-alt"></span></a>
							</sec:authorize></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<span class="floatRight"> <sec:authorize
				access="hasAuthority('ADMIN')">
				<a href="<c:url value='/registration' />"
					class="btn btn-primary btn-sm"><span class="fa fa-plus"></span>
					Add User</a>
			</sec:authorize>
		</span> <span class="floatRight"> <sec:authorize
				access="hasAuthority('ADMIN')">
				<a href="<c:url value='/alluserreportPDF'  />"
					class="btn btn-danger btn-sm" target="_blank"><i
					class="fas fa-file-pdf"></i> Export all users to PDF</a>
			</sec:authorize>
		</span> <span class="floatRight"> <sec:authorize
				access="hasAuthority('ADMIN')">
				<a href="<c:url value='/alluserreportCSV'  />"
					class="btn btn-success btn-sm"><i class="fas fa-file-code"></i>
					Export all users to CSV</a>
			</sec:authorize>
		</span> <span class="floatRight"> <sec:authorize
				access="hasAuthority('ADMIN')">
				<a href="<c:url value='/alluserreportExcel'  />"
					class="btn btn-primary btn-sm"><i class="fas fa-file-excel"></i>
					Export all users to MS-Excel</a>
			</sec:authorize>
		</span> <span class="floatRight"> <sec:authorize
				access="hasAuthority('ADMIN')">
				<a href="<c:url value='/alluserreportJSON'  />"
					class="btn btn-info btn-sm" target="_blank"> { } Export all
					users to JSON</a>
			</sec:authorize>
		</span>
	</div>

	<!-- #Modal for removing users -->
	<div class="modal fade" id="removeModalCenter" tabindex="-1"
		role="dialog" aria-labelledby="removeModalCenterTitle"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-top" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="removeModalCenterTitle">Confirm
						Delete</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p class="alert alert-danger">Are you sure you want to delete
						this User?</p>
				</div>
				<div class="modal-footer">
					<a href="" class="btn btn-danger" id="delRef"><span
						class="fa fa-times"> Yes Please</span></a>
					<button type="button" class="btn btn-success" data-dismiss="modal">
						<span class="fa fa-check"> No Thanks</span>
					</button>

				</div>
			</div>
		</div>
	</div>







	<%@include file="../fragments/footer.jsp"%>