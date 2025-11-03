import request from '@/utils/request'
import Vue from 'vue'
// 自动获取端口+ip
// var pathName = document.location.host
// var baseUrl = window.location.protocol + '//' + pathName
var baseUrl = window.SystemConfig.baseUrl
// 查询左侧应用
export function getList(params) {
  return request({
    url: baseUrl + '/minio/queryList',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    method: 'post',
    params
  })
}
// 创建应用
export function addSystem(data) {
  return request({
    url: baseUrl + `/minio/createBucket/${data}`,
    method: 'post',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}
// 删除应用
export function removeSystem(data) {
  return request({
    url: baseUrl + `/minio/removeBucket/${data}`,
    method: 'POST'
  })
}
// 查询系统路径下的内容
export function queryBucket(data) {
  return request({
    url: baseUrl + '/minio/queryBucket',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}
// 获取预览图片前缀
export function getUrl(params) {
  return request({
    url: baseUrl + '/public/getUrl',
    method: 'get',
    params
  })
}

// 文件上传
export function uploadFile(data) {
  return request({
    url: baseUrl + '/minio/uploadFile',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data
  })
}
// zip压缩包上传
export function uploadZip(data) {
  return request({
    url: baseUrl + '/minio/uploadZip',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data
  })
}
// 文件下载
export function downfile(params) {
  return request({
    url: baseUrl + `/minio/downloadFile?bucketName=${params.bucketName}&prefix=${params.prefix}`,
    method: 'get',
    responseType: 'blob'

  })
}
// 文件夹下载
export function downloadZip(params) {
  return request({
    url: baseUrl + `/minio/downloadZip?bucketName=${params.bucketName}&prefix=${params.prefix}`,
    method: 'get',
    responseType: 'blob'

  })
}
// 创建文件夹
export function createFolder(data) {
  return request({
    url: baseUrl + '/minio/createFolder',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    data: data
  })
}
// 文件删除
export function deleteFile(params) {
  return request({
    url: baseUrl + '/minio/deleteFile',
    method: 'POST',
    params
  })
}
// 全选删除
export function deleteAll(data) {
  return request({
    url: baseUrl + '/minio/deleteAll',
    method: 'POST',
    data
  })
}
