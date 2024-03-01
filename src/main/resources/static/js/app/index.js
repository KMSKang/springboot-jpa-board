const btnLogin = document.getElementById('btn-login')
const btnLogout = document.getElementById('btn-logout')
if (isLogin) {
    removeClass(btnLogout, 'hidden')
}
else {
    removeClass(btnLogin, 'hidden')
}