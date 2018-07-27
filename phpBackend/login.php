<?php

	require "connection.php";
	
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$sql_query = "select * from userData where username like '$username';";
	
	$result = mysqli_query($con, $sql_query);
	
	if ($result && mysqli_num_rows($result) > 0){
	    $row = mysqli_fetch_assoc($result);
	    
	    if ($row["tries"] >= 3){
	        echo "User locked. Contact admins for more information.";
	        
    	} else {
    	    $sql_query = "select * from userData where username like '$username' and password like '$password';";
    	    $result = mysqli_query($con, $sql_query);
    	    
    	    if ($result && mysqli_num_rows($result) > 0){
    	        $row = mysqli_fetch_assoc($result);
    	        $tries = 0;
                $id = $row["id"];
    	        $sql_query = "update userData set tries='$tries' where id='$id';";
    	        mysqli_query($con, $sql_query);
    	        echo "Welcome: ".$row["firstName"]." ".$row["lastName"];
    	        
    	    } else {
    	        $tries = $row["tries"] + 1;
                $id = $row["id"];
        	    $sql_query = "update userData set tries='$tries' where id='$id';";
        	    mysqli_query($con, $sql_query);
        	    $remaining = 3-$tries;
        		echo "Wrong password! ".$remaining." tries remaining.";
        		
    	    }
    	    
    	}
	    
    } else {
        echo "No user found!";
        
    }

?>