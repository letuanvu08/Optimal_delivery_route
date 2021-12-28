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
document.getElementById('login').addEventListener('click', function(){
	var username_login = document.getElementById('user_login').value;
	var password_login = document.getElementById('pwd_login').value;
    callLoginAPi(username_login,password_login);
});

//xử lý signup
document.getElementById('signup').addEventListener('click', function(){
	var user_signup = document.getElementById('user_signup').value;
	var password_signup = document.getElementById('pwd_signup').value;
	var email_signup = document.getElementById('email_signup').value;
    callSignupAPi(user_signup,password_signup,email_signup);
});

const callLoginAPi = function (_username,_password){
	var url_login = "https://optimal-delivery-route.herokuapp.com/users/login";
	axios.post(url_login,{
		"username" : _username,
		"password" : _password
	}).then(response => {
		// luu vao cookie
		document.cookie = "jwtToken"+"="+response.data.jwtToken+";";
	});
}

const callSignupAPi = function (_username,_password,_email){
	var url_signup = "https://optimal-delivery-route.herokuapp.com/users/register";
	axios.post(url_signup,{
		"username" : _username,
		"password" : _password,
		"email": _email
	});
}