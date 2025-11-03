<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <div
        style="
          border: 1px solid #ebeef5;
          background-color: #fff;
          color: #303133;
          -webkit-transition: 0.3s;
          transition: 0.3s;
          text-align: left;
          padding-left: 20px;
        "
      >
        <router-link key="expand" class="sidebar-logo-link" to="">
          <img
            v-if="logo"
            src="@/assets/images/file.png"
            class="sidebar-logo"
          />
          <h1 class="sidebar-title">{{ title }}</h1>
          <img
            src="@/assets/images/tjfl.png"
            class="zj-logo"
            title="创建应用"
            @click="addicon()"
          />
        </router-link>
      </div>
    </transition>
    <el-dialog
      title="创建应用"
      :visible.sync="dialogVisible"
      width="35%"
      :modal-append-to-body="true"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="应用名称" prop="name">
          <el-row>
            <el-col :span="20">
              <el-input
                v-model="ruleForm.name"
                maxlength="15"
                clearable
              ></el-input>
            </el-col>
            <!-- <el-col :span="4">
              <el-tooltip placement="top">
                <div slot="content">
                  桶名规则桶名长度必须在3 ~ 63个字符之间。<br />
                  桶名只能由小写字母、数字、“。”和“-”组成。<br />
                  桶名不能包含两个相邻的句点。<br />
                  桶名不能格式化为IP地址(例如192.168.5.4)。<br />
                  桶名不能以前缀xn--开头。<br />
                  桶名不能以后缀-s3alias结尾。<br />
                  此后缀为接入点别名保留。<br />
                  分区内的桶名必须是唯一的。
                </div>
                <i class="el-icon-question"></i>
              </el-tooltip>
            </el-col> -->
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >立即创建</el-button
          >
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { addSystem } from '@/api/system'
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: '应用名称',
      // logo: "@/assets/images/file.png",
      logo: '',
      dialogVisible: false,
      ruleForm: {
        name: ''
      },
      rules: {
        name: [{ required: true, message: '请输入应用名称', trigger: 'blur' },
          {
            // validator: this.validateName,
            trigger: 'blur'
          }] // onkeyup="value = value.replace(/[^a-z]/g,'')"
      }
    }
  },
  methods: {
    validateName(rule, value, callback) {
      const regex = /^[a-z0-9.-]{3,63}$/
      const noAdjacentDots = value.indexOf('..') === -1
      const notIpAddress = !/(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/.test(value)
      const noXnPrefix = !value.startsWith('xn--')
      const noS3AliasSuffix = !value.endsWith('-s3alias')
      if (!regex.test(value)) {
        callback(new Error('名称只能由小写字母、数字、“。”和“-”组成，长度必须在3-63个字符之间'))
      } else if (!noAdjacentDots) {
        callback(new Error('不能包含两个相邻的句点'))
      } else if (!notIpAddress) {
        callback(new Error('不能格式化为IP地址'))
      } else if (!noXnPrefix) {
        callback(new Error('不能以前缀xn——开头'))
      } else if (!noS3AliasSuffix) {
        callback(new Error('不能以后缀-s3alias结尾'))
      } else {
        callback()
      }
    },
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          const res = await addSystem(this.ruleForm.name)
          if (res && res.code == '0') {
            this.dialogVisible = false
            this.$bus.$emit('resourceList')
          } else {
            this.dialogVisible = false
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    addicon() {
      this.dialogVisible = true
      this.ruleForm = {}
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 74px;
  line-height: 74px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    background: #ffff;
    font-family: "Microsoft YaHei";
    font-style: normal;
    font-weight: 700;
    font-size: 18px;
    line-height: 24px;
    letter-spacing: 0.08em;
    color: #374c64;

    & .sidebar-logo {
      vertical-align: middle;
      margin-right: 12px;
    }
    & .zj-logo {
      vertical-align: middle;
      margin-left: 50%;
      // margin-left: 100px;
      color: #000;
      width: 20px;
    }
    & .sidebar-title {
      // display: inline-block;
      // margin: 0;
      // font-weight: 600;
      // line-height: 74px;
      // font-size: 18px;
      // font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      // vertical-align: middle;
      // font-family: "Microsoft YaHei";
      // font-weight: 700;
      // letter-spacing: 0.08em;
      // color: #374c64;
      display: inline-block;
      margin: 0;
      line-height: 74px;
      font-family: "PingFang SC";
      font-style: normal;
      font-weight: 600;
      font-size: 22px;
      color: #202428;
    }
  }
  &.collapse {
    .sidebar-logo {
      margin-right: 12px;
    }
  }
}
</style>
