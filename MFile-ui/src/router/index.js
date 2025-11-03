import Vue from 'vue'
import Router from 'vue-router'

/* Layout */
import Layout from '@/layout'
Vue.use(Router)
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'dashboard',
    meta: { title: '图标管理' },
    children: [
      {
        path: '/dashboard',
        name: 'dashboard',
        component: () => import('@/views/table/index'),
        meta: { title: '图标管理' }
      }
    ]
  }
]

const createRouter = () => new Router({
  // mode: 'hash', // require service support history
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
const router = createRouter()
export function getCookie(cname) {
  const name = cname + '='
  const cookies = document.cookie.split(';')
  // tslint:disable-next-line:prefer-for-of
  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim()
    if (cookie.indexOf(name) === 0) {
      const json = decodeURIComponent(
        cookie.substring(name.length, cookie.length)
      )
      return json.length > 0 ? JSON.parse(json) : ''
    }
  }
  return ''
}
router.beforeEach((to, from, next) => {
  // const puser = getCookie('pgis_user')
  // console.log('puserpuserpuserpuser', puser)
  next()
  // if (puser.userName) {
  //   next()
  //   localStorage.setItem('pgis_user', JSON.stringify(puser))
  // } else {
  //   // let token = localStorage.getItem('Authorization');
  //   if (puser == '' && to.path != '/dashboard') {
  //     window.location.href = `${window.SystemConfig.loginUrl //this.$loginUrl
  //       }?redirectUrl=${encodeURIComponent(location.href.split('#')[0])}`
  //   } else {
  //     next()
  //   }
  // }
})

export default router
