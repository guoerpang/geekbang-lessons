<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>
</head>
<body>
	<div class="container-lg">
		<h1>注册页面</h1>
		<form id="register">
			姓名：<input type="text" name="name"/></br>
			密码：<input type="text" name="password"/></br>
			邮箱：<input type="text" name="email"/></br>
			手机号：<input type="text" name="phoneNumber"/></br>
			<input type="button" name="submit" value="注册" >
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/static/js/jquery.min.js" crossorigin="anonymous"></script>

<script type="text/javascript" >
	$(document).ready(function () {
		$("input[name='submit']").click(function(){
			var obj = $('#register').serialize();
			console.log("obj"+obj);
			$.ajax({
				url:"${pageContext.request.contextPath}/register/registerSave",
				param:obj,
				success:function(data){
					// alert(data);
					console.log("data:"+data);
				}
			});
		});
	});

</script>
</body>
