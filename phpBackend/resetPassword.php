<?php

require "connection.php";
require "addNewResetPasswordRecord.php";

$username = $_POST["username"];
$to = $_POST["email"];


if(isset($username) && isset($to)){
   if(addRecord($firstName, $lastName, $username, $to, $userID, $id, $con, $errorMsg)){
        $subject = "Password Reset - Sales Force Mobile";
        
        $headers = "From: " . strip_tags("salesforcemobile") . "\r\n";
        $headers .= "Reply-To: ". strip_tags("No reply") . "\r\n";
        $headers .= "MIME-Version: 1.0\r\n";
        $headers .= "Content-Type: text/html; charset=UTF-8\r\n";
        
        $message = strip_tags($firstName).' '.strip_tags($lastName).',
                    <p>We received a request to change your password on Sales Force Mobile.</p>
                    <p>Click the link below to set a new password:</p>
                    <p><a href="http://salesforcemobile.ga?id='.strip_tags($id).'">New password</a></p>
                    <p>['.strip_tags(time()).']If you don\'t want to change your password, please ignore this email.</p>
                    <p>Thank you, <br/> Sales Force Mobile team</p>';
        
        if(mail($to, $subject, $message, $headers)){
            echo "Success";
        } else {
            echo "Sending mail Failed";
        }
   } else {
       echo $errorMsg;
   }
}

?>

