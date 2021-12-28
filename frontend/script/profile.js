// TODO: retrieve and store token here
const token = '' 

// profile elements
var userNameElm = document.getElementById('user-name')
var firstNameElm = document.getElementById('first-name')
var lastNameElm = document.getElementById('last-name')
var emailElm = document.getElementById('email')
var phoneElm = document.getElementById('phone')
var addressElm = document.getElementById('address')

// change password elements
var currentPasswordElm = document.getElementById('current-password')
var newPasswordElm = document.getElementById('new-password')
var comfirmationPasswordElm = document.getElementById('confirmation-password')

// render user info 
const renderUserInfo = async () => {
    const response = await fetch('https://optimal-delivery-route.herokuapp.com/users/get',{
        method: 'GET',
        headers:{
            'Authorization': 'Bearer ' + token + '\''
        },
    })
    const userInfo = await response.json()
    userNameElm.innerText = userInfo.username
    firstNameElm.value = userInfo.firstname
    lastNameElm.value = userInfo.lastname
    emailElm.value = userInfo.email
    phoneElm.value = userInfo.phone
    addressElm.value = userInfo.address
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
    }else if (newPasswordElm.value != comfirmationPasswordElm.value){
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
        "email": emailElm.innerText,
        "phone": phoneElm.innerText,
        "firstname":firstNameElm.innerText,
        "lastname":lastNameElm.innerText,
        "address": addressElm.innerText
    }
    const response = await fetch('https://optimal-delivery-route.herokuapp.com/users/update',{
        method: 'PUT',
        headers:{
            'Authorization': 'Bearer ' + token + '\'',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateProfileBody)
    })
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
        headers:{
            'Authorization': 'Bearer ' + token + '\'',
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(changePasswordBody)
    })
    renderUserInfo()
}

document.getElementById('btn-update-profile').addEventListener('click',requestUpdateProfile)

document.getElementById('btn-change-password').addEventListener('click',requestChangePassword)

renderUserInfo()





