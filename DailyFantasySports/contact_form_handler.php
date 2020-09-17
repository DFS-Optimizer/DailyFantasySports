<?php
if(!isset($_POST['submit']))
{
  echo "error; you need to submit the form!";
}


  $name = $_POST['name'];
  $visitor_email = $_POST['email']
  $message = $_POST['message'];
  
 if(empty($name)||empty($visitor_email))
 {
  echo "Name and email are mandatory"
  exit;
 }
 
 if(IsInjected($visitor_email))
 {
 echo "Bad email value!";
 exit;
 }
  
  $email_from = 'ccbrown414@gmail.com';
  
  $email_subject = "New Form Submission";
  
  $email_body = "User Name: $name.\n".
                  "User Email: $visitor_email.\n".
                        "User Message: $message.\n";
                        
  $to = "ccbrown414@gmail.com"
  
  $headers = "From: $email_from \r\n";
  
  $headers .= "Reply-To: $visitor_email \r\n";
  mail($to, $email_subject, $email_body, $headers);
  
  header("Location: bioPage.html");
  
  //Function to protect against email injection attempts
  function IsInjected($str)
  {
  $injections = array('\n+)',
      '(\r+)',
      '(\t+)',
      '(%0A+)',
      '(%0D+)',
      '(%08+)',
      '(%09+)',
      );
  $inject = join('|', $injections);
  $inject = "/$inject/i";
  if(preg_match($inject,$str))
    {
    return true;
    }
   else{
   return false;
   }
   
  }
  
  ?>
