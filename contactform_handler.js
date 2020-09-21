document.addEventListener("DOMContentLoaded", function() {
 fields.firstName = document.getElementById('firstName');
 fields.lastName = document.getElementById('lastName');
 fields.email = document.getElementById('email');
 fields.password = document.getElementById('password');
 fields.passwordCheck = document.getElementById('passwordCheck');
 fields.question = document.getElementById('question');
})



class User {
 constructor(firstName, lastName, message, email) {
 this.firstName = firstName;
 this.lastName = lastName;
 this.message = message;
 this.email = email;
 }
}

function sendContact(){
if(isValid()){
  let usr = new User(firstname.value,lastname.value, email.value, message.value)
  alert('${usr.firstname} thanks for your registering.')
 }
 else{
  alert("There was an error")
  }
 }


function isNotEmpty(value) {
 if (value == null || typeof value == 'undefined' ) return false;
 return (value.length > 0);
}

function isEmail(email) {
 let regex = /^[a-zA-Z0-9.!#$%&'*+/=?^_'{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
return regex.test(String(email).toLowercase());
}




function fieldValidation(field, validationFunction) {
 if (field == null) return false;

 let isFieldValid = validationFunction(field.value)
 if (!isFieldValid) {
 field.className = 'placeholderRed';
 } else {
 field.className = '';
 }

 return isFieldValid;
}



function isValid() {
 var valid = true;
 
 valid &= fieldValidation(fields.firstName, isNotEmpty);
 valid &= fieldValidation(fields.lastName, isNotEmpty);
 valid &= fieldValidation(fields.message, isNotEmpty);
 valid &= fieldValidation(fields.email, isEmail);


 return valid;
}


