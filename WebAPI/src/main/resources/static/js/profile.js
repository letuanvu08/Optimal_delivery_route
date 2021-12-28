const jwt_token= 'jwt_token'
const getCookieValueOf = function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

// TODO: retrieve and store token here

// profile elements
var userNameElm = document.getElementById('user-name')
var firstNameElm = document.getElementById('first-name')
var lastNameElm = document.getElementById('last-name')
var emailElm = document.getElementById('email')
var phoneElm = document.getElementById('phone')
var addressElm = document.getElementById('address')
var transportElm = document.getElementById('transport')
var dateOfBirthElm = document.getElementById('date-of-birth')

// change password elements
var currentPasswordElm = document.getElementById('current-password')
var newPasswordElm = document.getElementById('new-password')
var confirmationPasswordElm = document.getElementById('confirmation-password')

// render user info 
const renderUserInfo = async () => {
    const response = await fetch('https://optimal-delivery-route.herokuapp.com/users/get',{
        method: 'GET',
        headers: {
            'Authorization': 'Bearer '+ getCookieValueOf(jwt_token),
            'Content-Type': 'application/json',
        },
    })
    const userInfo = await response.json()
    userNameElm.innerText = userInfo.username
    firstNameElm.value = userInfo.firstname
    lastNameElm.value = userInfo.lastname
    emailElm.value = userInfo.email
    phoneElm.value = userInfo.phone
    addressElm.value = userInfo.address
    transportElm.value = userInfo.transport
    dateOfBirthElm.value = userInfo.dateOfBirth

    currentPasswordElm.value =''
    newPasswordElm.value=''
    confirmationPasswordElm.value=''
}

function checkNewProfile(){
    if (!emailElm.value.includes('@')){
        alert('Email address is not valid')
        return false
    } else if (!/^\d+$/.test(phoneElm.value) || phoneElm.value.length!=10){
        alert('Phone number is not valid')
        return false
    }
    return true
}

function checkNewPassword(){
    if (newPasswordElm.value.length<8){
        alert('Password length must >=8')
        return false
    }else if (newPasswordElm.value != confirmationPasswordElm.value){
        alert('Password don\'t match')
        return false
    }
    return true
}

// put new profile
const requestUpdateProfile = async () => {
    if (!checkNewProfile()){
        return
    }
    let updateProfileBody = {
        "email": emailElm.value,
        "phone": phoneElm.value,
        "firstname":firstNameElm.value,
        "lastname":lastNameElm.value,
        "address": addressElm.value,
        "transport": transportElm.value,
        "dateOfBirth": dateOfBirthElm.value
    }
    const response = await fetch('https://optimal-delivery-route.herokuapp.com/users/update',{
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer '+getCookieValueOf(jwt_token),
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updateProfileBody)
    })
    if (response.status == 200){
        alert("Update Profile Successfully")
    }else{
        alert("Update Profile Failed!")
    }
    renderUserInfo()
}


// request change password
const requestChangePassword = async () => {
    if (!checkNewPassword()){
        return false
    }
    let changePasswordBody = {
        "oldpassword":currentPasswordElm.value,
        "newpassword":newPasswordElm.value
    }
    const response = await fetch('https://optimal-delivery-route.herokuapp.com/users/changepassword',{
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer '+getCookieValueOf(jwt_token),
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(changePasswordBody)
    })
    if (response.status == 200){
        alert("Change Password Successfully")
    } else {
        alert("Change Password Failed!")
    }
    renderUserInfo()
}

document.getElementById('btn-update-profile').addEventListener('click',requestUpdateProfile)

document.getElementById('btn-change-password').addEventListener('click',requestChangePassword)

renderUserInfo()





