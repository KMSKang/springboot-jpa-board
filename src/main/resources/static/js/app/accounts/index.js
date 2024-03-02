const username = document.getElementById('username')
const password = document.getElementById('password')
const btnSubmit = document.getElementById('btn-submit')

btnSubmit.addEventListener('click', function () {
    $.LoadingOverlay('show')

    let params = {
        'username': username.value,
        'password': password.value
    }

    axios.post('/api/accounts', params, {
        header: { header, token },
        params: { '_csrf': csrf }
    }).then((response) => {
        const responseData = response.data
        const code = responseData.code
        const message = responseData.message
        const data = responseData.data
        // console.log(responseData)
        // console.log('code', code)
        // console.log('message', message)
        // console.log('data', data)

        alert('가입 완료')
        windowHistoryBack()
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
