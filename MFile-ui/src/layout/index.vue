<template>
  <div :class="classObj" class="app-wrapper">
    <div class="topApp">
      <!-- <img src="@/assets/images/bj.png" class="header-logo" /> -->
      <div class="header-font">
        {{ this.headName
        }}<span style="font-size: 14px;">{{version}}</span>
        <!-- <span style="font-size: 13px">{{ this.version }}</span> -->
      </div>
      <img src="@/assets/images/logo.png" class="logoImg" />
        <el-row>
      <div class="userTex">
          <!-- <el-col :span="6">
            <div
              @click="$router.push('/dashboard')"
              :class="[
                this.$route.path == '/dashboard' ? 'pitchOn' : 'NotToChoose',
              ]"
            >
              <span> 首页</span>
            </div>
            <img
              src="../assets/images/tbxz.png"
              class="sy"
              v-if="this.$route.path == '/dashboard'"
              alt=""
            />
          </el-col> -->
          <!-- <el-col :span="6" @click="$router.push('/table/index')">
            <div
              @click="$router.push('/table/index')"
              :class="[
              this.$route.path == '/dataExtract' ||
              this.$route.path == '/editDetails' ||
                this.$route.path == '/table/index' ||
                this.$route.path == '/issueDetails' ||
                this.$route.path == '/dataUploading' ||
                this.$route.path == '/details' ||
                this.$route.path == '/databaseConnectionConfiguration' ||
                this.$route.path == '/databaseConnection/addData' ||
                this.$route.path == '/treatmentStrategy' ||
                this.$route.path == '/fileTreatmentStrategy'
                  ? 'pitchOn'
                  : 'NotToChoose',
              ]"
            >
              数据资源
            </div>
            <img
              src="../assets/images/tbxz.png"
              class="sy"
              v-if="
                this.$route.path == '/dataExtract' ||
                this.$route.path == '/editDetails' ||
                this.$route.path == '/table/index' ||
                this.$route.path == '/issueDetails' ||
                this.$route.path == '/dataUploading' ||
                this.$route.path == '/details' ||
                this.$route.path == '/databaseConnectionConfiguration' ||
                this.$route.path == '/databaseConnection/addData' ||
                this.$route.path == '/treatmentStrategy' ||
                this.$route.path == '/fileTreatmentStrategy'
              "
              alt=""
            />
          </el-col>
          <el-col :span="6">
            <div
              @click="$router.push('/serve/index')"
              :class="[
                this.$route.path == '/serve/serveDetails' ||
                this.$route.path == '/serve/index'
                  ? 'pitchOn'
                  : 'NotToChoose',
              ]"
            >
              数据服务
            </div>
            <img
              src="../assets/images/tbxz.png"
              class="sy"
              v-if="
                this.$route.path == '/serve/serveDetails' ||
                this.$route.path == '/serve/index'
              "
              alt=""
            />
          </el-col>
          <el-col :span="6">
            <div
              @click="$router.push('/dataAnalysis/index')"
              :class="[
                this.$route.path == '/dataAnalysis/index' ||
                this.$route.path == '/dataAnalysis/analysisDetails'
                  ? 'pitchOn'
                  : 'NotToC',
              ]"
            >
              数据分析
            </div>
            <img
              src="../assets/images/tbxz.png"
              class="sy"
              v-if="
                this.$route.path == '/dataAnalysis/index' ||
                this.$route.path == '/dataAnalysis/analysisDetails'
              "
              alt=""
            />
          </el-col> -->

          <!-- <el-col :span="4">
            <div
              @click="$router.push('/404')"
              :class="[
                this.$route.path == '/404'
                  ? 'pitchOn'
                  : 'NotToChoose',
              ]"
            >
              帮助
            </div>
             <img
              src="../assets/images/tbxz.png"
              style="position: absolute; bottom: -21px"
              v-if="
                this.$route.path == '/404'
              "
              alt=""
            />
          </el-col>
          <el-col :span="4">
            <div
              @click="$router.push('/404')"
              :class="[
                this.$route.path == '/404'
                  ? 'pitchOn'
                  : 'NotToChoose',
              ]"
            >
              设置
            </div>
             <img
              src="../assets/images/tbxz.png"
              style="position: absolute; bottom: -21px"
              v-if="
                this.$route.path == '/404'
              "
              alt=""
            />
          </el-col> -->
      </div>

        </el-row>
      <el-row class="tcdl">
        <el-col :span="24">
          <el-popover
            placement="bottom"
            trigger="hover"
            popper-class="myPopover"
            v-model="visible"
          >
            <!-- <div class="exit" @click="$router.push('/404')" v-if="userName != '未登录'">帮助</div> -->
            <!-- v-auth="settingsVariable" -->
            <!-- <div
              class="exit"
              @click="$router.push('/systemSet')"

              v-if="userName != '未登录'"
            >
              设置
            </div> -->
            <!-- <div class="exit" @click="exit" v-if="userName != '未登录'">退出登录</div>
            <div class="exit" @click="exit" v-if="userName == '未登录'">返回登录</div>
            <div slot="reference" @click="open">
              <img src="@/assets/images/system_logo2.png" class="tcdl1" />
               <span style="margin-left: 5px">
                {{ userName }}
              </span>
            </div> -->
          </el-popover>
        </el-col>
      </el-row>
    </div>
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
      </div>
      <app-main />
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { getUserResources } from '@/api/user'
export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  data() {
    return {
      settingsVariable: '',
      sizeList: ['small'],
      authorityData: [],
      authData: [],
      headName: '',
      version: '',
      visible: false
      // userName: localStorage.getItem("pgis_user")!=null?JSON.parse(localStorage.getItem("pgis_user")).userName:'未登录',
    }
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    classObj() {
      return {
        // hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    getCookie(cname) {
      const name = cname + '='
      const cookies = document.cookie.split(';')
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
    // delCookie(name) {
    //   var exp = new Date();
    //   exp.setTime(exp.getTime() + -1 * 24 * 60 * 60 * 1000);
    //   var cval = this.getCookie(name);
    //   document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
    // },
    delCookie(name) {
      var exp = new Date()
      exp.setTime(exp.getTime() - 1)
      var cval = this.getCookie(name)
      if (cval != null) { document.cookie = name + '=' + cval + ';expires=' + exp.toGMTString() + ';path=/' }
    },
    exit() {
      this.visible = !this.visible
      this.delCookie('pgis_user')
      localStorage.removeItem('pgis_user')
      sessionStorage.removeItem('fullPath')
      sessionStorage.removeItem('parentId')
      sessionStorage.setItem('fullPath', '默认分类')
      this.$bus.$emit('deletemenu')
      // window.location.href = `${window.SystemConfig.loginUrl}?redirectUrl=${window.SystemConfig.projectUrl}`;
      window.location.href = `${window.SystemConfig.loginUrl}?redirectUrl=${encodeURIComponent(location.href.split('#')[0])}`
    },
    open() {
      this.visible = !this.visible
    },
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    logout() {
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    // 获取用户权限
    async authority(item) {
      localStorage.removeItem('authData')
      // const form = new FormData();
      // form.append("tId", item.tId);
      const res = await getUserResources({
        id: JSON.parse(localStorage.getItem('pgis_user')).id
      })
      if (res.code == 0) {
        this.authorityData = JSON.parse(res.result)
        this.authorityData.forEach((ele) => {
          // console.log("ele.name", ele.name, ele.code);
          this.authData.push(ele.code)
        })
        localStorage.setItem('authData', JSON.stringify(this.authData))
      }
    }
  },
  mounted() {
    // console.log('localStorage.getItem("pgis_user")',localStorage.getItem("pgis_user"));
    // this.$http.get("./package.json").then((res) => {
    //   console.log("json数据为:" + res); //此处的res对象包含了json的文件信息和数据，我们需要的json数据存在于body属性中
    // });
    var pjson = require('../../package.json')
    // console.log(" document.body.clientWidth", document.body.clientWidth);

    this.headName = window.SystemConfig.headName
    this.version = window.SystemConfig.versions
    // this.version = "v" + pjson.version;

    if (
      this.authData.includes('rc_settings') == true) {
      this.settingsVariable = 'rc_settings'
    } else if (this.authData.includes('rc_settings') == false) {
      this.settingsVariable = 'rc_settings'
    }
    if (localStorage.getItem('pgis_user') != null) {
      this.authority()
    }
  }
}
</script>

<style lang="scss" scoped>
// @import "~@/styles/mixin.scss";
// @import "~@/styles/variables.scss";
.logoImg {
  position: absolute;
  left: 31px;
  top: 18px;
  width: 5%;
}
.sy {
  margin-top: 14px;
  // margin-left: 21%;
  margin-left: 25px;
  width: 108px;
}
.tcdl1 {
  vertical-align: middle;
  width: 28px;
}
.tcdl {
  float: right;
  position: absolute;
  right: 33px;
  top: 19px;
  color: #fff;
  font-size: 14px;
}
.app-wrapper {
  // @include clearfix;
  // position: relative;
  // height: 80%;
  // overflow: hidden;
  width: 100%;
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}
.pitchOn {
  text-align: center;
  cursor: pointer;
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 600;
  font-size: 18px;
  // line-height: 60px;
  color: #ffffff;
  border-right: 1.6px solid #b4b9bf;
  height: 12px;
  line-height: 12px;
  margin-top: 28px;
  //  background-image: url("../assets/images/tbxz.png");
}
.NotToChoose {
  text-align: center;
  cursor: pointer;
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  // line-height: 60px;
  color: #b4b9bf;
  border-right: 1.6px solid #b4b9bf;
  height: 12px;
  line-height: 12px;
  margin-top: 28px;
}
.NotToC {
  text-align: center;
  cursor: pointer;
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  // line-height: 60px;
  color: #b4b9bf;
  // border-right: 1.6px solid #b4b9bf;
  height: 12px;
  line-height: 12px;
  margin-top: 28px;
}
.userTex {
  float: right;
  // height: 65px;
  // line-height: 65px;
  color: #fff;
  // margin-right: 170px;
  width: 36%;
  font-size: 14px;
  position: absolute;
  top: 0;
  right: 200px;
}
.topApp {
  // background: #16253c;
  width: 100%;
  height: 62px;
  background: #223248;
  box-shadow: 0px 2px 4px rgba(60, 60, 60, 0.08);
}
.drawer-bg {
  // background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}
.header-logo {
  // width: 560px;
  width: 100%;
  height: 68px;
}
.user-dropdown {
  position: absolute;
  top: 50px;
  left: 1828px;
  transform-origin: center top;
  z-index: 2001;
}
.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  // width: calc(100% - #{$sideBarWidth});
  transition: width 0.28s;
}

// .hideSidebar .fixed-header {
//   width: calc(100% - 54px);
// }

.mobile .fixed-header {
  width: 100%;
}
/////@at-root
.header-font {
  position: absolute;
  // left: calc(50% - 205px / 2 - 766.5px);
  left: 140px;
  // left: 79px;
  // top: 15px;
  // font-family: "Microsoft YaHei";
  // font-style: normal;
  // font-weight: 700;
  // // font-size: 32px;
  // font-size: 29px;
  // line-height: 42px;
  // letter-spacing: 0.08em;
  // // background: linear-gradient(180deg, #ffffff 0%, #bcc8d0 100%);
  // background: #fff;
  // -webkit-background-clip: text;
  // -webkit-text-fill-color: transparent;
  // background-clip: text;
  // text-fill-color: transparent;
  font-family: "PangMenZhengDao";
  font-style: normal;
  font-weight: 400;
  font-size: 29px;
  height: 62px;
  line-height: 65px;
  letter-spacing: 0.08em;
  color: #ffffff;
}
.avatar-wrapper {
  margin-top: 5px;
  position: relative;

  .user-avatar {
    cursor: pointer;
    width: 40px;
    height: 40px;
    border-radius: 10px;
  }

  .el-icon-caret-bottom {
    cursor: pointer;
    position: absolute;
    right: -20px;
    top: 25px;
    font-size: 12px;
  }
}
</style>
<style>
.el-popover.myPopover.el-popper[x-placement^="bottom"] .popper__arrow::after {
  /* border-bottom-color: red !important; */
}

.el-popover.myPopover {
  padding: 0;
  text-align: center;
  /* height: 105px; */
}
.exit {
  cursor: pointer;
  /* padding: 5px; */
  line-height: 32px;
}
.exit:hover {
  background: #f5f5f5;
  color: #202428;
}
</style>
