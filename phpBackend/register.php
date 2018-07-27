<?php

	require "connection.php";
	
	$firstName = $_POST["firstName"];
	$lastName = $_POST["lastName"];
	$username = $_POST["username"];
	$email = $_POST["email"];
	$password = $_POST["password"];
	$tries = 0;
	$firstLogin = false;
	
	$sql_query = "select username from userData where username like '$username';";
	
	$result = mysqli_query($con, $sql_query);
	
	if($result && mysqli_num_rows($result) > 0){
		echo "Someone already has that username.";
		
	} else {
    	$sql_query = "insert into userData (firstName, lastName, username, email, password, tries, firstLogin) values('$firstName', '$lastName', '$username', '$email', '$password', '$tries', '$firstLogin');";
    	
    	if(mysqli_query($con, $sql_query)){
    		echo "Registration successful.";
    	} else {
    		echo "Unknown error has occurred. Please try again.";
    	}
	}
	
	

?>