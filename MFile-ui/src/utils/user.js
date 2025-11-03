import { Directive } from 'vue'
import { check } from '@/utils/auth'

export const vAuth = {
  // created(el, binding, vnode, prevVnode) {
  //   // 下面会介绍各个参数的细节
  //   // console.debug("el, binding, vnode, prevVnode", el, binding, vnode, prevVnode);
  //   console.debug("binding", binding.value);
  //   console.debug("el", el);
  //   console.debug("el.parentNode", el.parentNode);
  //   console.dir(el);
  //   el.parentNode && el.parentNode.removeChild(el);
  // },
  bind: function() {
    // console.log("DDDDDDDDDD")
  },
  update: function (el, binding, vnode) {
    // console.log("EEEEEEEEEEEE", binding.value, el)
    // console.dir(el);
    if (!binding.value) {
      el.style.display = 'block'
    } else if (!check(binding.value)) {
      el.style.display = 'none'
      // contains
      // el.parentNode && el.parentNode.appendChild(el);
    }
  },
  inserted: function(el, binding, vnode) {
    // console.log("eelelell");
    // console.log("binding", binding.value);
    if (!binding.value) return
    if (!check(binding.value)) {
      // el.parentNode && el.parentNode.removeChild(el);
      el.style.display = 'none'
    }
  }
}
// const vAuthDirective = {
//     install(vue) {
//         console.debug("AAAAAAAAAAAAAAAAAAAAAAAAAA", vue);
//         // vue.directive("auth", vAuth);
//     }
// }

const TestPlugin = {}

TestPlugin.install = function (Vue, options) {
  Vue.directive('auth', vAuth)
}

export default TestPlugin
