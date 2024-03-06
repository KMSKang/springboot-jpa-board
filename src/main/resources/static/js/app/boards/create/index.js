const title = document.getElementById('title') // 제목
const content = document.getElementById('content') // 내용
const btnSubmit = document.getElementById('btn-submit')

btnSubmit.addEventListener('click', function () {
    $.LoadingOverlay('show')

    let params = {
        'title': title.value,
        'content': content.value,
        'isScret': document.querySelector('input[name="isScret"]:checked').value
    }

    let formData = new FormData()
    formData.append('dto', new Blob([JSON.stringify(params)], { type: 'application/json' }))
    formData.append('file', file.files[0])

    axios.post('/api/boards', formData, {
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

        alert('등록되었습니다')
        windowLocationReload()
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