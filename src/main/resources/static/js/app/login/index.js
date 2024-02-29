const username = document.getElementById('username')
const password = document.getElementById('password')
const btnSubmit = document.getElementById('btn-submit')

btnSubmit.addEventListener('click', function () {
    let params = {
        'username': username.value,
        'password': password.value,
    }

    axios.post('/api/login', params, {
        header: { header, token },
        params: { '_csrf': csrf }
    }).then((response) => {
        const responseData = response.data
        const code = responseData.code
        const message = responseData.message
        const data = responseData.data
        // console.log(responseData)
        // console.log(code)
        // console.log(message)
        // console.log(data)

        location.href = data
    }).catch((error) => {
        console.log(error)
        console.log(error.response.data)
        alertError(error.response.data.data)
    }).finally(() => {
        $.LoadingOverlay('hide')
    })
})
