<?php

function addRecord(&$firstName, &$lastName, $username, $email, &$userID, &$id, $con, &$errorMsg){
    $sql_query = "select * from userData where username like '$username';";
    $result = mysqli_query($con, $sql_query);
    if($result && mysqli_num_rows($result) > 0){
        $row = mysqli_fetch_assoc($result);
        
        $userID = $row["id"];
        $firstName = $row["firstName"];
        $lastName = $row["lastName"];
        
        if($email != $row["email"]){
            $errorMsg = "Wrong email!";
            return 0;
        }
        
        $id = randomID(128);

        $time = time() + 3600;
        
        $sql_query = "insert into resetPasswordRecords (id, userID, time) values('$id', '$userID', '$time');";
        
        if(mysqli_query($con, $sql_query)){
        	return 1;
        } else {
            $errorMsg = "Error when inserting data";
        	return 0;
        }
    } else {
        $errorMsg = "No connection with server!";
        return 0;
    }
}

function randomID($len = 128) {

    $sets = array();
    $sets[] = 'ABCDEFGHJKLMNPQRSTUVWXYZ';
    $sets[] = 'abcdefghjkmnpqrstuvwxyz';
    $sets[] = '23456789';

    $password = '';
    
    foreach ($sets as $set) {
        $password .= $set[array_rand(str_split($set))];
    }

    while(strlen($password) < $len) {
        $randomSet = $sets[array_rand($sets)];
        
        $password .= $randomSet[array_rand(str_split($randomSet))]; 
    }

    return str_shuffle($password);
}

?>