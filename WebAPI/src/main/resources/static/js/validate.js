function checkEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }
  function checkPhone(phoneNum) {
    if (phoneNum.length!=10||isNaN(phoneNum)) return false;
    return true;
  }
  function checkPassword(password) {
    if (password.length<8) return false;
    return true;
  }
  function validateEmail() {
    const $emailResult = $("#emailchecker");
    const email = $("#email_signup").val();
    $emailResult.text("");
  
    if (checkEmail(email)) {
      $emailResult.text("Email is valid");
      $emailResult.css("color", "green");
    } else {
      $emailResult.text("Email is invalid");
      $emailResult.css("color", "red");
    }
  }

  function validatePhone() {
    const $phoneResult = $("#phonechecker");
    const phoneNum = $("#phone_signup").val();
    $phoneResult.text("");
    if (checkPhone(phoneNum)) {
      $phoneResult.text("Phone Numbers is valid");
      $phoneResult.css("color", "green");
    } else {
      $phoneResult.text("Phone Numbers have 10 digits");
      $phoneResult.css("color", "red");
    }
    return false;
  }
  function validatePassword() {
    const $passwordResult = $("#passwordchecker");
    const password = $("#pwd_signup").val();
    $passwordResult.text("");
    if (checkPassword(password)) {
      $passwordResult.text("Password is valid");
      $passwordResult.css("color", "green");
    } else {
      $passwordResult.text("Password is more than 8 characters");
      $passwordResult.css("color", "red");
    }
    return false;
  }
  $("#email_signup").on("input", validateEmail);
  $("#phone_signup").on("input", validatePhone);
  $("#pwd_signup").on("input", validatePassword);

