const exception = new URLSearchParams(location.search).get('exception')
const username = document.getElementById('username')
const password = document.getElementById('password')
const btnSubmit = document.getElementById('btn-submit')

if (exception) {
    if ('InternalAuthenticationServiceException' === exception) {
        alert('아이디를 찾을 수 없습니다')
        windowHistoryBack()
    }

    if ('BadCredentialsException' === exception) {
        alert('비밀번호가 일치하지 않습니다')
        windowHistoryBack()
    }
}

btnSubmit.addEventListener('click', function () {
    $.LoadingOverlay('show')

    let params = {
        'username': username.value,
        'password': password.value
    }

    axios.post('/api/login', params, {
        header: { header, token },
        params: { '_csrf': csrf , 'remember-me': true }
    }).then((response) => {
        const responseData = response.data
        const code = responseData.code
        const message = responseData.message
        const data = responseData.data
        // console.log(responseData)
        // console.log(code)
        // console.log(message)
        // console.log(data)

        location.href = '/'
    }).catch((error) => {
        console.log(error)
        const responseData = error.response.data
        const code = responseData.code
        const message = responseData.message
        const data = responseData.data
        console.log(responseData)
        console.log(code)
        console.log(message)
        console.log(data)
        alertError(message)
    }).finally(() => {
        $.LoadingOverlay('hide')
    })
})
