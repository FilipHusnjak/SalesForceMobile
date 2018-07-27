
<html>
<head>
	<title>Reset password</title>
</head>
<body>
	<?php
	    require "connection.php";
        $id = $_GET["id"];
        $sql_query = "select * from resetPasswordRecords where id like '$id';";
        $result = mysqli_query($con, $sql_query);
        if($result && mysqli_num_rows($result) > 0){
           $row = mysqli_fetch_assoc($result);
           if($row["time"] > time()){
               if(isset($_POST["newPassword"]) && isset($_POST["confirmPassword"])){
                    if($_POST["newPassword"] == $_POST["confirmPassword"]){
                        $newPass = hash("sha3-512", $_POST["newPassword"]);
                        $userID = $row["userID"];

                        $sql_query = "update userData set password='$newPass' where id='$userID';";
                        $sql_query2 = "update resetPasswordRecords set time=0 where id='$id';";
                        if(mysqli_query($con, $sql_query2) && mysqli_query($con, $sql_query)){
                            echo "<h2>Success</h2>";
                        } else {
                            echo "Error. Please check your internet connection and try again.";
                        }
                        
                    } else {
                        echo "<form method=\"post\" action=\"?id=$id\">
                            	New Password:
                            	<input type=\"password\" name=\"newPassword\"><br/>
                            	Confirm Password:
                            	<input type=\"password\" name=\"confirmPassword\"><br/>
                            	<input type=\"submit\" value=\"Change\">
                            </form><br/>
                            Password do not match";
                    }
               } else {
                   echo "<form method=\"post\" action=\"?id=$id\">
                        	New Password:
                        	<input type=\"password\" name=\"newPassword\"><br/>
                        	Confirm Password:
                        	<input type=\"password\" name=\"confirmPassword\"><br/>
                        	<input type=\"submit\" value=\"Change\">
                        </form>";
               }
               
           } else {
                echo "Expired!";
           }
           
        } else {
           echo "
				<table>
					<tr><th>Name</th></tr>
					<tr><td>SURNAME</td></tr>
				</table>
		   ";
	   }
	?>
</body>
</html>