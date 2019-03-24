<%@include file="../fragments/header.jsp"%>
<body>

	<div class="container">
		<div class="row">
			<div class="panel-body">
				<div class="row">

					<div class="col-lg-12">
						<strong style="color: red;"><h2>Access Denied !!!</h2></strong>

						<div class="authbar">
							<h4>
								<span>Dear <strong>${loggedinuser}</strong>, You are not
									authorized for this operation.
								</span> <span class="floatRight"><a
									href="<c:url value='/logout' />"
									class="btn btn-sm btn-warning "><strong>LOGOUT</strong></a></span> <a
									href="<c:url value='/list' />" class="btn btn-sm btn-success "><strong>Go
										Back</strong></a>

							</h4>
						</div>
						<br>

						<div class="alert alert-info" role="alert">
							<strong>Heads up!</strong> This alert needs your attention, but
							it's not super important.
							<p></p>
						</div>
						<div class="alert alert-warning" role="alert">
							<strong>This you can do... </strong> Change a few things up and
							try submitting again.
						</div>
						<div class="alert alert-danger" role="alert">
							<strong>Please make sure</strong> you have appropriate
							permissions. Contact Administrator if need further assistance.
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<%@include file="../fragments/footer.jsp"%>

