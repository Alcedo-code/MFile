import request from '@/utils/request'
import Vue from 'vue'
// export function login(data) {
//   if (window.SystemConfig.Baseurl == 'http://10.0.15.34') {
//     return request({
//       url: window.SystemConfig.Baseurl + '/uam/api/users/login',
//       method: 'post',
//       data: {
//         password: data.password,
//         username: data.username,
//       }

//     })
//   } else {
//     return request({
//       url: window.SystemConfig.Baseurl + '/nuserlogin/nUserLogin',
//       method: 'post',
//       data: {
//         password: data.password,
//         account: data.username
//       }
//     })
//   }
// }

// export function getInfo(token) {
//   return request({
//     url: 'http://localhost:9528/dev-api/vue-admin-template/user/info',
//     method: 'get',
//     params: { token }
//   })
// }

// export function logout() {
//   return request({
//     url: 'http://localhost:9528/dev-api/vue-admin-template/user/logout',
//     method: 'post'
//   })
// }
// 查询所有用户
export function selectUserByGroupId (params) {
  return request({
    url: window.SystemConfig.userBaseUrl + `/uam/api/users/selectUserByGroupId?keyword=&order=asc&offset=0&limit=${params.pageSize}`,
    method: 'get',
    params
  })
}
// 查询所有用户
export function getUserResources (params) {
  return request({
    url: window.SystemConfig.uam + `uam/api/users/getUserResources?id=${params.id}`,
    method: 'get'
    // params
  })
}
