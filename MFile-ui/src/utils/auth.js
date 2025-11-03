import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

function getCurrentAuthority() {
  return JSON.parse(localStorage.getItem('authData'))
}

export function check(authority) {
  console.log('authority', authority)
  const authList = getCurrentAuthority()
  console.log('authList', authList)

  // console.log('authList.includes(authority)', authList.includes(authority));
  if (authList) {
    return authList.includes(authority)
  }
  // return false;
}
