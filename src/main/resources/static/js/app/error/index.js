const exception = new URLSearchParams(location.search).get('exception')

// 인가 (로그인 X)
if ('UNAUTHORIZED' === exception) {
    alert('Unauthorized Exception (인가 오류)')
}

// 인증 (권한)
if ('FORBIDDEN' === exception) {
    alert('Forbidden Exception (인증 오류)')
}

windowHistoryBack()
