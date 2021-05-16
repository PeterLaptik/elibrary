<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
   <head>
      <title>Login page</title>
	  <link rel="stylesheet" type="text/css" href="../css/login.css" />
   </head>
   <body>
		<form name="loginform" method="POST">
			<div class="login_form">
				<script>
					let checkData = function(){
						let nameBox = input_name;
						let passBox = input_password;
						if(nameBox.value!='' && passBox.value!=''){
							formaction;
						} else {
							nameBox.value=='' ? 
								error_field.innerHTML = 'Fill login field!'
								: error_field.innerHTML = 'Fill password field!';
							login.disabled = true;
						}
					};
					
					let cleanMsg = function(){
						error_field.innerHTML = '&nbsp;';
						login.disabled = false;
					}
				</script>
				<p> Login: </p>
				<input id="input_name" name="login" type="text" size="25" maxlength="30" 
							value="" onclick="cleanMsg()" /> <br />
				<input id="input_password" name="pass" type="password" size="25" maxlength="30" value="" onclick="cleanMsg()" /> <br />
				<p id="error_field"> &nbsp; </p>
				<p style="margin-top:0px;"> 
					<button name="action" value="sign_up" formaction="/registration"> Sign up </button> 
					<button id="login" name="action" value="login" onclick="checkData()" formaction="/logging"> Login </button> 
				</p>
			</div>
		</form>
   </body>
</html>