<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <div @click.prevent="handleLink()" class="aClass">
      <!-- {{ this.title }} -->
    </div>

  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'
// import { getClassifiedList, addLocalTable } from '@/api/dataAbove'
export default {
  data () {
    return {
      classifyTotal: 0,
      levelList: null,
      title: '',
      dialogVisible: false,
      classifyData: [],
      multipleSelection: [],
      name: '',
      searchParams: {
        // userid: JSON.parse(localStorage.getItem('pgis_user')).userName,
        channelId: '',
        layerType: '',
        tNameZh: '',
        pageNo: 1,
        pageSize: 12,
        tableName: ''
      }
    }
  },
  watch: {
    $route () {
      // this.getBreadcrumb()
    }
  },
  created () {
    // this.$bus.$on('deletemenu', () => {
    //   this.title = '当前位置：' + '数据资源'
    //   sessionStorage.setItem('fullPath', '默认分类')
    // })
    // this.getBreadcrumb()
    // this.$bus.$on('menu', (data) => {
    //   this.title = data.fullPath ? data.fullPath : data.channelName
    //   if (this.$route.path == '/table/index') {
    //     this.title = '当前位置：' + '数据资源 > ' + this.title
    //   } else if (this.$route.path == '/issueDetails') {
    //     this.title = '当前位置：' + '数据资源 > ' + this.title + ' > 数据详情'
    //   } else if (this.$route.path == '/editDetails') {
    //     this.title = '当前位置：' + '数据资源 > ' + this.title + ' > 数据资源编辑'
    //   } else if (this.$route.path == '/dataExtract') {
    //     this.title = '当前位置：' + '数据资源 > ' + this.title + ' > 数据资源提取'
    //   }
    // })
    // this.$bus.$on('findListMenu', () => {
    //   if (this.$route.path == '/table/index') {
    //     this.title =
    //       '当前位置：' + '数据资源 > ' + sessionStorage.getItem('fullPath')
    //   } else if (this.$route.path == '/issueDetails') {
    //     this.title =
    //       '当前位置：' +
    //       '数据资源 > ' +
    //       sessionStorage.getItem('fullPath') +
    //       ' > 数据详情'
    //   } else if (this.$route.path == '/editDetails') {
    //     this.title =
    //       '当前位置：' +
    //       '数据资源 > ' +
    //       sessionStorage.getItem('fullPath') +
    //       ' > 数据资源编辑'
    //   } else if (this.$route.path == '/dataExtract') {
    //     this.title =
    //       '当前位置：' +
    //       '数据资源 > ' +
    //       sessionStorage.getItem('fullPath') +
    //       ' > 数据资源提取'
    //   }
    // })
    // if (this.$route.path == "/example/table") {
    // if (this.title == "") {
    //   this.title =
    //     sessionStorage.getItem("fullPath") == null
    //       ? "当前位置：" + "数据资源 "
    //       : "当前位置：" +
    //         "数据资源 " +
    //         "> " +
    //         sessionStorage.getItem("fullPath");
    //   // "当前位置：" + "数据资源 " + sessionStorage.getItem('fullPath')== null?'/ '+sessionStorage.getItem('fullPath'):'';
    // } else {
    //   "当前位置：" + "数据资源 > " + this.$store.state.menu.menu.fullPath;
    // }
    // }
  },
  mounted () {
    // this.getClassified()
    //   const puser = this.getCookie("pgis_user");
    // if (puser.userName) {
    //   sessionStorage.removeItem("fullPath")
    //   sessionStorage.setItem("fullPath", "默认分类");
    // } else {
    //   if (puser == "") {
    //     sessionStorage.removeItem("fullPath")
    //     sessionStorage.setItem("fullPath", "默认分类");
    //   } else {
    //     sessionStorage.setItem("fullPath", "默认分类");
    //   }
    // }
  },
  methods: {
    getCookie (cname) {
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
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
      console.log(';this.multipleSelection', this.multipleSelection)
    },
    async addLocal () {
      this.multipleSelection.forEach((ele) => {
        ele.channelId = sessionStorage.getItem('parentId')
      })
      const res = await addLocalTable(this.multipleSelection)
      console.log('res', res)
      if (res.code == 0) {
        this.$message({
          message: '恭喜你，导入成功！',
          type: 'success'
        })
        // this.getClassified()
        this.$bus.$emit('wfl', sessionStorage.getItem('parentId'))
      }
      //
    },
    // 未分类列表  classifyData
    async getClassified () {
      this.searchParams.tableName = this.name
      const res = await getClassifiedList(this.searchParams)
      if (res.code == 0) {
        this.classifyData = res.dataList
        this.classifyTotal = res.total
      }
    },
    // 本地导入
    localityImport () {
      this.dialogVisible = true
    },
    handleClose () {
      this.dialogVisible = false
    },
    // getBreadcrumb () {
    //   // only show routes with meta.title
    //   const matched = this.$route.matched.filter(
    //     (item) => item.meta && item.meta.title
    //   )
    //   const first = matched[0]

    //   // if (!this.isDashboard(first)) {
    //   //   matched = [{ path: '/dashboard', meta: { title: '通知公告' }}].concat(matched)
    //   // }

    //   this.levelList = matched.filter(
    //     (item) => item.meta && item.meta.title && item.meta.breadcrumb !== false
    //   )
    // },
    isDashboard (route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return (
        name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
      )
    },
    pathCompile (path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink (item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 16px;
  line-height: 50px;
  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
.aClass {
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  line-height: 57px;
  letter-spacing: 0.08em;
  color: #8691a3;
}
</style>
