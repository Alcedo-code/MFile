<template>
  <div :class="{ 'has-logo': showLogo }" class="layout">
    <logo :collapse="isCollapse"></logo>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <!-- :style="{ height: divHeight }" -->
      <div class="panelMenu">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :unique-opened="false"
          :active-text-color="variables.menuActiveText"
          :collapse-transition="false"
          mode="vertical"
        >
          <div class="tree">
            <keep-alive>
              <el-tree
                :data="menuData"
                node-key="name"
                @node-click="channeClick"
                current-node-key="name"
                default-expand-all
                :render-after-expand="false"
                :highlight-current="true"
                ref="treeBox"
                :expand-on-click-node="false"
                :props="{
                  children: 'children',
                  label: 'systemName',
                }"
              >
                <span
                  class="custom-tree-node"
                  slot-scope="{ node, data }"
                  @mouseenter="mouseenter(data, node)"
                  @mouseleave="mouseleave(data, node)"
                >
                  <span class="flmc" :title="'名称：' + data.name">
                    <i class="el-icon-s-home"></i>
                    {{ node.label }}
                  </span>
                  <span style="display: flex">
                    <i
                      class="el-icon-download"
                      v-show="data.show"
                      @click="downloadBag(node)"
                      title="下载"
                    ></i>
                  </span>
                  <span style="display: flex">
                    <i
                      class="el-icon-delete"
                      v-show="data.show"
                      @click="handleCommand(node)"
                      title="删除"
                    ></i>
                  </span>
                </span>
              </el-tree>
            </keep-alive>
          </div>
        </el-menu>
      </div>
    </el-scrollbar>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import { component as VueContextMenu } from '@xunlei/vue-context-menu'
import { getList, removeSystem, downloadZip } from '@/api/system'
import '@/utils/rem'

export default {
  components: { SidebarItem, Logo, 'vue-context-menu': VueContextMenu },
  data() {
    return {
      clickCode: [],
      divHeight: 0,
      menuData: [],
      treeCurretNode: {},
      showTree: true,
      treeNodeData: '', // 存当前数据
      treeNode: '', // 存当前节点信息
      contextMenuVisible: false, // 让菜单显示
      contextMenuTarget: null,
      title: '',
      isShow: false,
      // 父级目录列表
      parentMenuList: [],
      node: {},
      fullPath: '',
      total: 0,
      ruleForm: {
        channelName: '',
        remark: '',
        parentId: '',
        userid: '',
        isStatus: 0
      },
      isFl: false,
      dialogVisible: false,
      rules: {
        channelName: [
          { required: true, message: '请填写名称', trigger: 'blur' }
        ],
        remark: [{ required: true, message: '请填写备注', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapGetters(['sidebar']),
    routes() {
      return this.$router.options.routes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    handleNodeClick() {},
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    handleCommand(data) {
      console.log('handleCommand', data)
      // if (data == 'delete') {
      this.handleDelete(data.data.name)
      // } else if (data == 'ren') {
      //   this.edit(0)
      // } else if (data == 'add') {
      //   this.title = '新增子目录'
      //   this.ruleForm.parentId = this.node.data.channelId
      //   this.$set(this.ruleForm, 'channelName', '')
      //   this.$set(this.ruleForm, 'remark', '')
      //   this.dialogVisible = true
      // }
    },

    mouseenter(data, node) {
      this.$set(data, 'show', true)
    },
    mouseleave(data, node) {
      // this.$set(data, "show", false);
      if (this.clickCode[0] && data.name === this.clickCode[0].name) {
        this.$set(data, 'show', true)
      } else {
        this.$set(data, 'show', false)
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    submitForm(formName, index) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          if (index == 2) {
            const res = await addMenu(this.ruleForm)
            if (res.code == 0) {
              this.findList()
              this.dialogVisible = false
            }
          } else if (index == 1) {
            const res = await editclassify(this.ruleForm)
            if (res.code == 0) {
              this.findList()
              this.dialogVisible = false
            }
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 下载
    async downloadBag(row) {
      const loading = this.$loading({
        lock: true,
        text: '文件夹下载中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
      try {
        // 发送请求到服务器获取 ZIP 文件
        const response = await downloadZip({
          bucketName: row.data.name,
          prefix: ''
        })

        if (response) {
          // 创建一个指向Blob对象的URL
          const url = window.URL.createObjectURL(new Blob([response]))
          // 创建一个下载链接
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', row.data.name + '.zip') // 设置下载文件的名称
          // 触发下载
          document.body.appendChild(link)
          link.click()
          // 释放URL对象
          window.URL.revokeObjectURL(url)
          // 可选：移除链接元素
          document.body.removeChild(link)
          // } else {
          //   // 处理错误情况
          //   console.error('下载失败', response);
          loading.close()
        }
      } catch (error) {
        // 处理请求错误
        console.error('下载 ZIP 文件时发生错误', error)
        loading.close()
      }
    },
    // 删除
    async handleDelete(data) {
      this.$confirm('是否确定删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async () => {
          const res = await removeSystem(data)
          if (res.code == 0) {
            this.$message({
              message: '删除成功！',
              type: 'success'
            })
            this.$bus.$emit('deletemenu')
            sessionStorage.removeItem('fullPath')
            this.findList()
          } else if (res.code == 1) {
            this.$message({
              message: res.msg,
              type: 'warning'
            })
          }
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 点击tree的节点，触发handleNodeClick事件
    channeClick(data, node, el) {
      if (this.clickCode.length) {
        if (this.clickCode[0].name === data.name) {
        } else {
          this.$set(this.clickCode[0], 'show', false)
          this.$set(data, 'show', true)
          this.clickCode.splice(0, 1, data)
        }
      } else {
        this.$set(data, 'show', true)
        this.clickCode.push(data)
      }
      this.node = node
      // 此处可添加点击事件的相关操作，其中data是树节点相关数据，node中亦存在树节点相关数据，另外还存在其parent和children
      // 若node未知，可根据以下方式获取相关节点的node值
      const currentNode = this.$refs.treeBox.getNode(data) // 该值与handelNodeClick函数参数node相同，ref.紧跟着的名称为el-tree组件
      // 获取对应节点的父级目录
      this.parentMenuList = []
      this.nodeClick(node)
      // remove()翻转的原因是，获取到的parentMenuList为层级大的在前，层级小的在后，故需要进行翻转再拼接
      // console.log("let parentMenuList ", this.parentMenuList);
      // let parentMenuList = this.parentMenuList.remove();
      // this.parentMenuList = parentMenuList;

      // 将获取到的父级目录列表进行组合输出，若点击三级1-1-1得到的结果为  一级 1/二级 1-1/三级 1-1-1
      let parentMenu = ''
      this.parentMenuList.forEach((item, index) => {
        if (index !== this.parentMenuList.length - 1) {
          parentMenu += item + ' > '
        } else {
          parentMenu += item
        }
      })
      this.fullPath = parentMenu
      data.fullPath = this.fullPath
      if (data.channelName == '默认分类') {
        this.$bus.$emit('notClassified', data)
        this.ruleForm.parentId = data.channelId
      }
      this.$store.commit('menu/SET_MENU', data)
      this.$bus.$emit('menu', data)
      sessionStorage.setItem('fullPath', this.fullPath)

      sessionStorage.removeItem('parentId')
      sessionStorage.setItem('parentId', data.channelId)
      this.$set(data, 'show', true)
    },
    // 获取tree任意节点父级列表
    nodeClick(node) {
      if (node.label) {
        this.parentMenuList.unshift(node.label)
      }
      // 迭代
      if (node.parent) {
        this.nodeClick(node.parent)
      }
    },

    // 列表方法
    async findList(data) {
      // this.ruleForm.userid = JSON.parse(
      //   localStorage.getItem("pgis_user")
      // ).userName;
      const res = await getList()
      this.menuData = res.data
      if (this.menuData.length > 0) {
        this.menuData.forEach((ele) => {})
        this.$bus.$emit('menu', this.menuData[0])
        this.$nextTick().then(() => {
          this.$refs.treeBox.setCurrentKey(this.menuData[0].name)
        })
      } else {
        sessionStorage.removeItem('fullPath')
      }
      if (this.menuData.length == 1) {
        sessionStorage.removeItem('fullPath')
      }
    }
  },
  created() {},
  mounted() {
    this.findList()
    this.$bus.$on('resourceMenu', (data) => {
      this.add(data)
    })
    this.$bus.$on('getTotal', (data) => {
      this.total = data
    })
    this.$bus.$on('resourceList', (data) => {
      this.findList()
    })
    // this.$nextTick().then(() => {
    //   if (sessionStorage.getItem("parentId")) {
    //     this.$refs.treeBox.setCurrentKey(sessionStorage.getItem("parentId"));
    //   } else {
    //     const firstNode = document.querySelector(".el-tree-node");
    //     firstNode.click();
    //   }
    // });
  }
}
</script>
<style>
.layout .el-tree-node__expand-icon.expanded {
  font-size: 18px;
}
.layout .el-tree-node__content > .el-tree-node__expand-icon {
  font-size: 18px;
}
</style>
<style scoped>
.flmc {
  width: 190px;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
  font-family: "Microsoft YaHei";
  font-style: normal;
  font-weight: 400;
  font-size: 17px;
  line-height: 30px;
  letter-spacing: 0.08em;
  /* color: #374c64; */
  height: 30px;
}

/* .showLogo
  .el-tree--highlight-current
  /deep/
  .el-tree-node.is-checked
  > .el-tree-node__content {
  background-color: rgb(255, 255, 255);
  color: rgb(64, 158, 255);
}
.showLogo
  .el-tree--highlight-current
  /deep/
  .el-tree-node.is-current
  > .el-tree-node__content {
  background-color: rgb(255, 255, 255);
  color: rgb(64, 158, 255);
} */

.panelMenu {
  overflow-y: auto;
  border: 1px solid #ebeef5;
  background-color: #fff;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
  font-size: 12px;
  height: calc(100vh - 145px);
  max-height: calc(100vh - 145px);
}

::-webkit-scrollbar {
  /*对水平流动条有效*/
  width: 8px;
  /*对垂直流动条有效*/
  height: 8px;
  /*对水平流动条有效*/
}

/*定义滚动条的轨道颜色、内阴影及圆角*/
::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(233, 220, 220, 0.3);
  background-color: rgb(240, 235, 235);
  border-radius: 3px;
}

/*定义滑块颜色、内阴影及圆角*/
::-webkit-scrollbar-thumb {
  border-radius: 7px;
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  background-color: #989898;
}

/*定义右下角汇合处的样式*/
::-webkit-scrollbar-corner {
  background: khaki;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  padding-right: 15px;
  height: 40px;
  line-height: 40px;
}

.bg-content {
  /* style="margin-left: -40px" */

  font-size: 12px;
  width: 76px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-align: left;
}

.menu {
  width: 90px;
  z-index: 99999;
  position: absolute;
  border-radius: 4px;
  border: 1px solid #999999;
  background-color: #ffff;
}

.menu_item:hover {
  /* background-color: #005397; */
  color: white;
  cursor: pointer;
}

.menu_item {
  line-height: 20px;
  text-align: center;
  padding: 7px;
  width: 90px;
  border-bottom: 1px solid #999999;
}

.flmc .title {
  display: -webkit-box;
  overflow: hidden;
  white-space: normal !important;
  text-overflow: ellipsis;
  word-wrap: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-break: break-all;
}

.sz {
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 18px;
  /* color: rgba(32, 36, 40, 0.6); */
}
</style>
