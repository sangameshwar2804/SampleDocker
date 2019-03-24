<script src="/webjars/jquery/3.3.1-1/jquery.min.js/"></script>
<script src="/webjars/popper.js/1.14.3/umd/popper.min.js/"></script>
<script src="/webjars/datatables/1.10.19/js/jquery.dataTables.min.js/"></script>
<script src="/webjars/bootstrap/4.1.3/js/bootstrap.min.js/"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {

						//for delete user
						$('.table .delBtn').on('click', function(event) {
							event.preventDefault();
							var href = $(this).attr('href');
							$('#removeModalCenter #delRef').attr('href', href);
							$('#removeModalCenter').modal('show');
						});
						$('#tableitems').dataTable( {
						        "lengthMenu": [[5, 10, 25, 50, 100, -1], [5, 10, 25, 50, 100, "All"]]
						    } );
					});
</script>
<footer class="footer">
	<!-- 				<div class="container"> -->
	<div class="container-fluid">
		<!-- 					<span>Open-source software development. -->
		<!-- 			Enterprise Solutions. Spring Boot Rocks - 2018.</span> -->
		<span class="text-muted">SPRING BOOT ROCKS. Open-source
			software development 2019</span>
	</div>
</footer>
</body>
</html>
