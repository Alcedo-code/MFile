import Vue from 'vue'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN.js' // lang i18n
import '@/styles/index.scss' // global css
import Api from '@/interface/index'
import App from './App'
import store from './store'
import router from './router'
import './utils/rem.js'
import vAuthDirective from '@/utils/user'
import bus from '@/utils/bus'
import global from '@/utils/global'
import '@/icons' // icon
// import '@/permission' // permission control
Vue.prototype.global = global
function px2rem(px) {
  if (/%/ig.test(px)) { // 有百分号%，特殊处理，表述pc是一个有百分号的数，比如：90%
    return px
  } else {
    return (parseFloat(px) / 37.5) + 'rem'
  }
}
Vue.prototype.$px2rem = px2rem // 放到全局
Vue.prototype.$bus = bus
Vue.prototype.userName = ''
Vue.prototype.validForbid = function (value, number = 255) {
  value = value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '').replace(/\s/g, '')
  if (value.length >= number) {
    this.$message({
      type: 'warning',
      message: `输入内容不能超过${number}个字符`
    })
  }
  return value
}

if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}
Vue.prototype.$Api = Api
// set ElementUI lang to EN
Vue.use(ElementUI, { locale })
Vue.use(vAuthDirective)
Vue.prototype.authData = []

// 如果想要中文版 element-ui，按如下方式声明

// 定义外部接口可配置
// import axios from 'axios'
const startApp = function () {
  // axios.get('/energizePlatform/static/config.json').then((res) => {
  // axios.get('/DataEnpowerWeb/package.json').then((res) => {
  //   console.log('jsonres', res);
  //   if(res.data.json){
  //     // Vue.prototype.Baseurl = res.data.json[0].Baseurl;
  //     // Vue.prototype.SocketUrl = res.data.json[0].SocketUrl;
  //     // Vue.prototype.headName = res.data.json[0].headName;
  //   }
  new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
  })
  // })
}
startApp()
Vue.config.productionTip = false
