const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");


sign_up_btn.addEventListener("click", () => {
    container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
    container.classList.remove("sign-up-mode");
});

// xử lý login
document.getElementById('login').addEventListener('click', function () {
    var username_login = document.getElementById('user_login').value;
    var password_login = document.getElementById('pwd_login').value;
    callLoginAPi(username_login, password_login);
});

//xử lý signup
document.getElementById('signup').addEventListener('click', function () {
    var _username = document.getElementById('user_signup').value;
    var _password = document.getElementById('pwd_signup').value;
    var _firstname = document.getElementById('firstname_signup').value;
    var _lastname = document.getElementById('lastname_signup').value;
    var _transport = document.getElementById('transport').value;
    var _dateofbirth = document.getElementById('date_signup').value;
    var _address = document.getElementById('address_signup').value;
    var _phone = document.getElementById('phone_signup').value;
    var _email = document.getElementById('email_signup').value;

    var url_signup = "https://optimal-delivery-route.herokuapp.com/users/register";
    axios.post(url_signup, {
        "username": _username,
        "password": _password,
        "email": _email,
        "phone": _phone,
        "firstname": _firstname,
        "lastname": _lastname,
        "transport": _transport,
        "dateofbirth": _dateofbirth,
        "address": _address
    })
        .then(() => document.location.href = "/")
        .catch(err => alert("Something wrong"));});

const callLoginAPi = function (_username, _password) {

}

const callSignupAPi = function (_username, _password, _email) {


}
