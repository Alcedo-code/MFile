// npm run build打包前执行此段代码
const fs = require('fs')

// 返回package的json数据
function getPackageJson() {
  const data = fs.readFileSync('./package.json')// fs读取文件
  return JSON.parse(data)// 转换为json对象
}

const packageData = getPackageJson()// 获取package的json
// const arr = packageData.version.split('.')// 切割后的版本号数组
// arr[2] = parseInt(arr[2]) + 1
// packageData.version = arr.join('.')// 转换为以"."分割的字符串
// 用packageData覆盖package.json内容
fs.writeFile(
  './package.json',
  JSON.stringify(packageData, null, '\t'
  ),
  (err) => {}
)
