const title = document.getElementById('title') // 제목
const content = document.getElementById('content') // 내용
const file = document.getElementById('file') // 파일
const keywordType = document.getElementById('keywordType') // 검색
const keyword = document.getElementById('keyword') // 내용
const btnSearch = document.getElementById('btn-search') // 버튼) 조회
const btnReset = document.getElementById('btn-reset') // 버튼) 초기화
const group = document.getElementById('group') // 테이블 영역
const selectSize = document.getElementById('select-size') // 페이지 당 데이터 개수
const btnArea = document.getElementById('btn-area')
const btnGroup = document.getElementById('btn-group') // 버튼 영역
const btnBack = document.getElementById('btn-back') // 버튼) 이전
const btnNext = document.getElementById('btn-next') // 버튼) 다음
const btnSubmit = document.getElementById('btn-submit') // 버튼) 등록

let page = 0
let size = 10
let contents = ''

load()
function load() {
    const params = {
        'page': page,
        'size': size
    }

    // 내용 값 있을 경우
    if (!isNull(keyword.value)) {
        params.keywordType = keywordType.value.toUpperCase()
        params.keyword = keyword.value
    }

    const queryString = new URLSearchParams(params).toString()
    axios.get(`/api/boards?${queryString}`, {
        header: { header, token },
        params: { _csrf: csrf }
    }).then((response) => {
        const responseData = response.data
        const data = responseData.data
        // console.log(responseData)
        // console.log(data)

        add(data)
        reset(data)
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

function add(obj) {
    // console.log(obj)

    // !첫 페이지 로딩 && 0 === 데이터
    if (0 !== page && 0 === obj.numberOfElements) {
        page = obj.totalPages-1
        load()
        return false
    }

    contents = ''

    // 테이블 영역
    const list = obj.content
    list.forEach(row => contents += create(row))
    for (let i=0; i<(size-list.length); i++) { contents += create({}) }
    group.innerHTML = ''
    group.insertAdjacentHTML('beforeend', contents)

    // 버튼 영역
    const pageNumber = obj.pageable.pageNumber
    const pageSize = obj.pageable.pageSize
    const totalPages = obj.totalPages
    const startPage = Math.floor(pageNumber/pageSize) * pageSize + 1
    const tempEndPage = startPage + pageSize - 1
    const endPage = tempEndPage < totalPages ? tempEndPage : totalPages
    // console.log(pageNumber)
    // console.log(pageSize)
    // console.log(totalPages)
    // console.log(startPage)
    // console.log(tempEndPage)
    // console.log(endPage)

    contents = ''
    for (let i=startPage; i<endPage+1; i++) { contents += createButton(i) }
    btnGroup.innerHTML = ''
    btnGroup.insertAdjacentHTML('beforeend', contents)
}

function create(obj) {
    // console.log(obj)
    const id = isNull(obj.id) ? '' : obj.id
    const title = isNull(obj.title)  ? '' : obj.title
    const createdBy = isNull(obj.createdBy)  ? '' : obj.createdBy
    const createdAt = isNull(obj.createdAt)  ? '' : obj.createdAt
    const view = isNull(obj.view)  ? '' : obj.view

    return `<tr>
                <td class="center">${id}</td>
                <td class="left">${title}</td>
                <td class="center">${createdBy}</td>
                <td class="center">${createdAt}</td>
                <td class="center">${view}</td>
            </tr>`
}

function createButton(value) {
    // console.log(value)
    return `<button type="button" id="button-${value}" onclick="movePage(${value}-1)">${value}</button>`
}

function movePage(value) {
    // console.log(value)
    page = value
    load()
}

function reset(obj) {
    // console.log(obj)
    const isFirst = obj.first
    const isLast = obj.last
    const isEmpty = 0 === obj.numberOfElements
    // console.log('isFirst:', isFirst)
    // console.log('isLast:', isLast)

    btnArea.querySelectorAll('button').forEach(element => element.disabled = false)
    if (isFirst) btnBack.disabled = true
    if (isLast) btnNext.disabled = true

    // 0 < 데이터
    if (!isEmpty) {
        document.getElementById(`button-${page+1}`).disabled = true
    }
}

selectSize.addEventListener('change', function () {
    size = this.value
    load()
})

btnSearch.addEventListener('click', function () {
    if (isNull(keyword.value)) {
        alert('검색 내용을 입력해 주세요')
        return false
    }
    page = 0
    load()
})

btnReset.addEventListener('click', function () {
    keywordType.value = ''
    keyword.value = ''
    selectSize.value = 10
    page = 0
    size = 10
    load()
})

btnSubmit.addEventListener('click', function () {
    $.LoadingOverlay('show')

    let params = {
        'title': title.value,
        'content': content.value
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

        title.value = ''
        content.value = ''
        file.value = ''

        page = 0
        load()
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
