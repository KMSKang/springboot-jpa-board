/**
 * 시큐리티 POST 보안
 */
const csrf = document.querySelector('input[name="_csrf"]').value
const token = document.querySelector('meta[name="_csrf"]').content
const header = document.querySelector('meta[name="_csrf_header"]').content
const isLogin = document.getElementById('isLogin') !== null

function windowLocationHref(url) {
    window.location.href = url
}

function windowHistoryBack() {
    window.history.back()
}

function alertError(data) {
    // console.log(data)
    if (Array.isArray(data)) {
        data = data.join(",").replaceAll(',', '\n')
    }
    alert(data)
}

function isNull(value) {
    return value === '' || value === ' ' || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)
}

function removeClass(element, value) {
    element.classList.remove(value)
}
