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

function windowOpen(url) {
    window.open(url, '_blank')

}

function windowHistoryBack() {
    window.history.back()
}

function windowLocationReload() {
    window.location.reload()
}

function windowClose() {
    window.close()
}

function alertError(data) {
    alert(data)
}

function isNull(value) {
    return value === '' || value === ' ' || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)
}

function removeClass(element, value) {
    element.classList.remove(value)
}
