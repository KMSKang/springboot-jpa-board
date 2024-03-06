const id = document.getElementById('id') // 순번
const title = document.getElementById('title') // 제목
const content = document.getElementById('content') // 내용
const createdBy = document.getElementById('createdBy') // 등록자명
const createdAt = document.getElementById('createdAt') // 등록일
const view = document.getElementById('view') // 조회수

const boardId = document.location.href.split("/").slice(-1)[0]

load()
function load() {
    const params = {
        'id': boardId
    }

    const queryString = new URLSearchParams(params).toString()
    axios.get(`/api/boards/detail?${queryString}`, {
        header: { header, token },
        params: { _csrf: csrf }
    }).then((response) => {
        const responseData = response.data
        const code = responseData.code
        const message = responseData.message
        const data = responseData.data
        console.log(responseData)
        console.log(code)
        console.log(message)
        console.log(data)

        id.value = data.id
        title.value = data.title
        content.value = data.content
        createdBy.value = data.createdBy
        createdAt.value = data.createdAt
        view.value = data.view
        document.getElementById(`isScret-${data.isScret}`).checked = true
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
        //
    })
}