<template>
  <section class="table contentListTable">
    <div>
      <div class="contentListTable_bj panelMenu">
        <div class="contentListTable_bj_breadcrumb">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '' }" style="margin-top: -3px">
              <a href="/" @click.prevent="handleClick(searchParams.bucketName)">
                <img
                  src="@/assets/images/ljt.png"
                  width="15"
                  alt=""
                  style="vertical-align: middle"
                />
                <span style="margin-left: 7px;font-size: 17px;line-height: 13px;">
                  {{ Chinese }}</span
                >
              </a>
            </el-breadcrumb-item>
            <el-breadcrumb-item
              v-for="(item, index) in breadcrumbItems"
              :key="index"
              :to="{ path: '' }"
            >
              <a href="/" @click.prevent="handleClick1(item, index)" style="font-size: 17px;">{{
                item.name
              }}</a>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="table_button">
          <el-row :gutter="2" type="flex" class="row-bg" justify="end">
            <el-col :span="6"
              ><div class="grid-content bg-purple">
                <el-dropdown>
                  <el-button type="primary">
                    上 传 <i class="el-icon-upload2"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-upload
                        style="height: 40px"
                        action=""
                        :on-success="updataZipSuccess"
                        :on-error="updataZipError"
                        :http-request="beforeZipUpload"
                        enctype="multipart/form-data"
                        :file-list="fileList"
                        :multiple="false"
                        accept=".zip"
                      >
                        zip压缩包
                      </el-upload>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-upload
                        style="height: 30px"
                        action=""
                        :on-success="updataFileSuccess"
                        :on-error="updataFileError"
                        :http-request="beforeUpload"
                        enctype="multipart/form-data"
                        :file-list="fileList"
                        accept="*"
                        :multiple="false"
                      >
                        文件
                      </el-upload>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div></el-col
            >
            <!-- <el-col :span="6"
              ><div class="grid-content bg-purple-light">
                <el-button type="success"
                  >下 载 <i class="el-icon-download"></i
                ></el-button></div
            ></el-col> -->
            <el-col :span="6"
              ><div class="grid-content bg-purple">
                <el-button type="success" @click="previewAll"
                  >预 览 <i class="el-icon-view"></i
                ></el-button></div
            ></el-col>
            <el-col :span="8"
              ><div class="grid-content bg-purple-light">
                <el-button type="primary" @click="createFolder"
                  >新建文件夹 <i class="el-icon-plus"></i
                ></el-button></div
            ></el-col>
            <el-col :span="4"
              ><div class="grid-content bg-purple">
                <el-button type="danger" @click="deleteAll"
                  >删 除 <i class="el-icon-delete"></i
                ></el-button></div
            ></el-col>
          </el-row>
        </div>
        <div class="classB">
          <el-table
            :data="tableData"
            size="mini"
            style="width: 100%"
            :row-style="{ height: $px2rem('140px') }"
            :cell-style="{ padding: '0' }"
            :header-cell-style="{
              background: '#FAFAFA',
              color: '#202428;',
              padding: '0',
              fontSize: $px2rem('18px'),
            }"
            :header-row-style="{ height: '0' }"
            @selection-change="listSelectionChange"
            @row-click="rowClick"
          >
            <el-table-column type="selection" width="80" />
            <el-table-column prop="name1" label="名称" show-overflow-tooltip>
              <template slot-scope="scope">
                <img
                  v-if="scope.row.type.split('/')[0] === 'application'"
                  src="@/assets/images/tableFile.png"
                  class="sjzyzttp"
                  alt=""
                />
                <img
                  v-if="!scope.row.type"
                  src="@/assets/images/folder.png"
                  class="sjzyzttp"
                  alt=""
                />
                <img
                  v-if="scope.row.type.split('/')[0] === 'image'"
                  src="@/assets/images/imgfile.png"
                  class="sjzyzttp"
                  alt=""
                />
                <span> {{ scope.row.name1 }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column prop="layerType" label="图片">
              <template slot-scope="scope">
                {{scope.row}}
              </template>
            </el-table-column> -->
            <!-- <el-table-column
              prop="layerType"
              label="类型"
            >
              <template slot-scope="scope">
                <img
                  v-if="scope.row.layerType == '1'"
                  title="点"
                  src="@/assets/images/dot.png"
                  class="sjzyzttp"
                  alt=""
                />
                <img
                  v-if="scope.row.layerType == '2'"
                  title="线"
                  src="@/assets/images/line.png"
                  class="sjzyzttp"
                  alt=""
                />
                <img
                  v-if="scope.row.layerType == '3'"
                  title="面"
                  src="@/assets/images/fill.png"
                  class="sjzyzttp"
                  alt=""
                />
              </template>
            </el-table-column> -->
            <el-table-column prop="size" label="大小" show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="lastTime"
              label="上传时间"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  v-if="
                    scope.row.type && scope.row.type.split('/')[0] === 'image'
                  "
                  type="text"
                  class="sjzybgxq"
                  style="color: #67c23a"
                  @click.stop="openDetails(scope.row, scope.$index)"
                  >预览</el-button
                >
                <el-button
                  type="text"
                  class="sjzybgxq"
                  @click.stop="download(scope.row)"
                  >下载</el-button
                >
                <el-button
                  type="text"
                  class="sjzybgxq"
                  style="color: red"
                  @click.stop="deleteFile(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog
      title="图片预览"
      :visible.sync="previewDialog"
      width="40%"
      :close-on-click-modal="false"
      :before-close="handleClose"
    >
      <!-- <div style="text-align: center; padding: 30px">
        <img
          id="oneImg"
          :src="previewImgUrl"
          v-if="previewDialog"
          alt=""
          width="100%"
          height="100%"
          style="cursor: pointer"
          @click="copyImageAddress(previewImgUrl)"
        />
      </div> -->
      <div class="image-preview">
        <el-row :gutter="24">
          <el-col :span="20" class="imgnamelocation">
            <div :title="images[currentImageIndex]">
              地址：{{ images[currentImageIndex] }}
              <el-button
                type="text"
                @click="copyImageAddress(images[currentImageIndex])"
                >复制</el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <div class="grid-content bg-purple">
            <el-button
              @click="prevImage"
              :disabled="currentImageIndex == 0"
              class="prev-button"
              ><i class="el-icon-caret-left" title="上一张"></i
            ></el-button>
          </div>
          <div class="grid-content bg-button">
            <!-- <div class="imgname" :title="item.name1">
                名称：{{ item.name1 }}
              </div> -->
            <img
              :src="images[currentImageIndex]"
              alt="预览图片"
              width="100%"
              height="100%"
              title="点击实现图片地址复制！"
              style="cursor: pointer"
              v-if="previewDialog"
              @click="copyImageAddress(images[currentImageIndex])"
            />
          </div>
          <div class="grid-content bg-purple">
            <el-button
              @click="nextImage"
              :disabled="images.length - 1 == currentImageIndex"
              class="next-button"
              ><i
                class="el-icon-caret-right"
                title="下一张"
                :disabled="currentImageIndex == images.length - 1"
              ></i
            ></el-button>
          </div>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="previewDialog = false"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="批量图片预览"
      :visible.sync="dialogVisible"
      width="90%"
      :close-on-click-modal="false"
    >
      <div class="batchPreview">
        <el-row :gutter="20" class="row-bg" justify="start">
          <el-col :span="4" v-for="(item, index) in tabularData" :key="index"
            ><div class="grid-content bg-purple imgGrid">
              <div class="imgname" :title="item.name1">
                名称：{{ item.name1 }}
              </div>
              <el-row :gutter="24">
                <el-col :span="19">
                  <div
                    class="imgname"
                    :title="
                      imgUrl + '/' + searchParams.bucketName + '/' + item.name
                    "
                  >
                    地址：{{
                      imgUrl + "/" + searchParams.bucketName + "/" + item.name
                    }}
                  </div>
                </el-col>
                <el-col
                  :span="4"
                  style="position: absolute; top: -8px; right: 7px"
                >
                  <el-button
                    type="text"
                    @click="
                      copyImageAddress(
                        imgUrl + '/' + searchParams.bucketName + '/' + item.name
                      )
                    "
                    >复制</el-button>
                </el-col>
              </el-row>
              <div class="imgname" :title="item.name1"></div>
              <div
                class="ytp"
                v-if="item.type && item.type.split('/')[0] === 'image'"
              >
                <img
                  :id="item.name"
                  :src="
                    imgUrl + '/' + searchParams.bucketName + '/' + item.name
                  "
                  width="100%"
                  height="100%"
                  alt=""
                  title="点击实现图片地址复制！"
                  style="cursor: pointer"
                  @click="
                    copyImageAddress(
                      imgUrl + '/' + searchParams.bucketName + '/' + item.name
                    )
                  "
                />
              </div>
              <div v-else class="wtp">
                <img src="@/assets/images/wtp.png" alt="" />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="创建文件夹"
      :visible.sync="folderVisible"
      width="30%"
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
        <el-form-item label="文件夹名称" prop="name">
          <el-input v-model="ruleForm.name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >立即创建</el-button
          >
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </section>
</template>
<script>
import { getUserResources } from '@/api/user'
import {
  queryBucket,
  getUrl,
  uploadFile,
  downfile,
  downloadZip,
  deleteFile,
  deleteAll,
  uploadZip,
  createFolder
} from '@/api/system'
export default {
  components: {},
  filters: {
    ellipsis(value, limit) {
      if (!value) return ''
      else if (value.length > limit) {
        return value.slice(0, limit) + '...'
      } else {
        return value
      }
    }
  },
  data() {
    return {
      menu: {},
      ruleForm: {
        name: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入文件夹名称', trigger: 'blur' }
        ]
      },
      folderVisible: false,
      inputData: [],
      tableLabel: [],
      checkAll: false,
      checkedCities: [],
      isIndeterminate: false,
      isChecked: false,
      tableData: [],
      temporaryTableData: [],
      dialogVisible: false,
      isImport: false,
      isList: true,
      authorityData: [],
      // userName: JSON.parse(localStorage.getItem("pgis_user")).userName,
      searchParams: {
        bucketName: '',
        prefix: ''
      },
      breadcrumbItems: [],
      Chinese: '',
      zipParams: {
        bucketName: '',
        prefix: ''
      },
      fileParams: {
        file: null,
        bucketName: '',
        prefix: ''
      },
      deleteParams: {
        bucketName: '',
        list: []
      },
      previewImgUrl: '',
      imgUrl: '',
      shouldShowColumn: false,
      previewDialog: false,
      total: 0,
      tids: '',
      isShow: false,
      isOpen: false,
      fileList: [],
      tabularData: [],
      currentImageIndex: null,
      images: []
    }
  },
  computed: {
    isShowPoppver() {
      return function (val, limit) {
        if (val && val.length >= limit) {
          return false
        } else {
          return true
        }
      }
    }
  },
  watch: {
    menu: {
      handler(value) {},
      deep: true // 深度监听
      // immediate: true,// 初次监听
    }
  },
  beforeRouteEnter(to, from, next) {
    next()
  },
  destroyed() {},
  created() {},
  mounted() {
    this.getImgUrl()
    this.findList()
    // this.authority();
    // if (this.authData) {
    if (this.$route.path == '/table/index') {
      this.$bus.$emit('findListMenu')
    }
    this.$bus.$on('menu', (data) => {
      console.log('data', data)
      this.searchParams.bucketName = data.name
      this.Chinese = data.systemName
      this.searchParams.prefix = ''
      this.findList()
      this.breadcrumbItems = []
    })
    this.$bus.$on('deletemenu', () => {
      this.searchParams.pageNo = 1
      this.findList()
    })
    this.$bus.$on('notClassified', () => {
      this.isImport = true
    })
  },
  methods: {
    handleClose() {
      this.previewDialog = false
      // this.images = [];
    },
    prevImage() {
      // console.log("this.currentImageIndex", this.currentImageIndex);
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--
      } else {
        // this.currentImageIndex = this.images.length - 1;
      }
    },
    nextImage() {
      if (this.currentImageIndex < this.images.length - 1) {
        this.currentImageIndex++
      } else {
        this.currentImageIndex = 0
      }
    },
    // 点击实现图片地址复制
    copyImageAddress(data) {
      const textArea = document.createElement('textarea')
      textArea.value = data
      // 避免在页面上显示textarea
      textArea.style.position = 'fixed'
      textArea.style.top = 0
      textArea.style.left = 0
      textArea.style.width = '2em'
      textArea.style.height = '2em'
      textArea.style.padding = 0
      textArea.style.border = 'none'
      textArea.style.outline = 'none'
      textArea.style.boxShadow = 'none'
      textArea.style.background = 'transparent'

      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      try {
        document.execCommand('copy')
        this.$message({
          message: '图片地址已复制到剪贴板！',
          type: 'success'
        })
      } catch (err) {
        this.$message({
          message: '无法复制图片地址!',
          type: 'error'
        })
      }
      document.body.removeChild(textArea)
    },
    // 创建文件夹
    createFolder() {
      this.$set(this.ruleForm, 'name', '')
      this.folderVisible = true
    },
    // 文件夹提交
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.ruleForm.bucketName = this.searchParams.bucketName
          if (this.searchParams.prefix) {
            this.ruleForm.name =
              this.searchParams.prefix + '/' + this.ruleForm.name + '/'
          } else {
            this.ruleForm.name = this.ruleForm.name + '/'
          }
          const res = await createFolder({
            prefix: this.ruleForm.name,
            bucketName: this.searchParams.bucketName
          })
          if (res && res.code == '0') {
            this.folderVisible = false
            this.findList()
          } else {
            this.folderVisible = false
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 批量预览
    previewAll() {
      if (this.tabularData.length > 0) {
        this.dialogVisible = true
      } else {
        this.$message({
          type: 'error',
          message: '请先选择要预览的内容！'
        })
      }
    },
    // 全选删除
    async deleteAll() {
      if (this.deleteParams.list.length > 0) {
        this.$confirm('是否确定删除？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async () => {
            const res = await deleteAll(this.deleteParams)
            if (res.code == 0) {
              this.$message({
                message: '删除成功！',
                type: 'success'
              })
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
      } else {
        this.$message({
          type: 'error',
          message: '请选择要删除的内容！'
        })
      }
    },
    updataFileSuccess(res) {
      this.$message({
        type: 'success',
        message: '上传成功',
        showClose: true,
        offset: 80
      })
      this.fileList = []
    },
    updataZipSuccess() {
      this.$message({
        type: 'success',
        message: '上传成功',
        showClose: true,
        offset: 80
      })
      this.fileList = []
    },

    // 上传失败
    updataZipError() {
      this.$message({
        type: 'error',
        message: '上传失败',
        showClose: true,
        offset: 80
      })
      this.fileList = []
    },
    // 上传失败
    updataFileError() {
      this.$message({
        type: 'error',
        message: '上传失败',
        showClose: true,
        offset: 80
      })
      this.fileList = []
    },
    // 上传的文件改变时（覆盖原来的文件）
    updataFileChange(file, fileList) {
      if (fileList.length > 0) {
        this.fileList = [fileList[fileList.length - 1]]
      }
    },
    beforeZipUpload(file, fileList) {
      const isLt2M = file.size / (1024 * 1024) > 10
      if (isLt2M) {
        this.$message.error('上传头像图片大小不能超过 10MB!')
      } else {
        this.uploadZip(file.file)
      }
      return isLt2M
    },
    // 上传方法
    async uploadZip(file) {
      // console.log('this.breadcrumbItems',this.breadcrumbItems);
      const loading = this.$loading({
        lock: true,
        text: '上传中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
      const form = new FormData()
      // 文件对象
      form.append('file', file)
      form.append('bucketName', this.searchParams.bucketName)
      form.append(
        'prefix',
        this.breadcrumbItems.length > 0
          ? this.breadcrumbItems[this.breadcrumbItems.length - 1].url
          : ''
      )
      const res = await uploadZip(form)
      if (res && res.code == 0) {
        loading.close()
        this.fileList = []
        this.findList()
      } else {
        this.$message({
          message: res.msg,
          type: 'warning'
        })
      }
    },
    beforeUpload(file, fileList) {
      // this.ifShow = false;
      // this.isShow = false;
      // if (file.size / (1024 * 1024) > this.fileSize.split('MB')[0]) {
      //   // 限制文件大小
      //   this.$message.warning('当前限制文件大小不能大于' + this.fileSize)
      //   return false
      // }
      // const suffix = this.getFileType(file.file.name) // 获取文件后缀名
      // const suffixArray = ['xlsx', 'csv', 'xls'] // 限制的文件类型，根据情况自己定义 .xlsx,.csv,.xls
      // if (suffixArray.indexOf(suffix) === -1) {
      //   this.$message({
      //     message: '文件格式错误',
      //     type: 'error',
      //     duration: 2000
      //   })
      //   // this.isShow = true;
      //   this.fileList = []
      //   return suffixArray
      // } else {
      // const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / (1024 * 1024) > 10
      if (isLt2M) {
        this.$message.error('上传头像图片大小不能超过 10MB!')
      } else {
        this.uploadFile(file.file)
      }
      return isLt2M
    },
    // 上传方法
    async uploadFile(file) {
      const loading = this.$loading({
        lock: true,
        text: '上传中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
      const form = new FormData()
      // 文件对象
      form.append('file', file)
      form.append('bucketName', this.searchParams.bucketName)
      form.append('prefix', this.searchParams.prefix)
      const res = await uploadFile(form)
      if (res && res.code == 0) {
        loading.close()
        this.fileList = []
        this.findList()
      } else {
        this.$message({
          message: res.msg,
          type: 'warning'
        })
      }
    },
    // 获取前缀
    async getImgUrl() {
      const res = await getUrl({})
      if (res.code == 0) {
        this.imgUrl = res.data
      }
    },
    // 获取用户权限
    async authority(item) {
      localStorage.removeItem('authData')
      const res = await getUserResources({
        // id: JSON.parse(localStorage.getItem("pgis_user")).id,
      })
      if (res.code == 0) {
        this.authorityData = JSON.parse(res.result)
        this.authorityData.forEach((ele) => {
          this.authData.push(ele.code)
        })
        localStorage.setItem('authData', JSON.stringify(this.authData))
      } else {
        this.$message({
          message: res.msg,
          type: 'warning'
        })
      }
    },

    // 移入
    mouseOver(e, index) {
      this.isShow = true
      if (index == '1') {
        e.currentTarget.className = 'imgClass'
      } else if (index == '2') {
        e.currentTarget.className = 'imgClass2'
      } else {
        e.currentTarget.className = 'imgClass1'
      }
    },
    // 移出
    mouseLeave(e, index) {
      if (index == '1') {
        e.currentTarget.className = 'imgnoClass'
      } else if (index == '2') {
        e.currentTarget.className = 'imgnoClass2'
      } else if (index == '3') {
        e.currentTarget.className = 'imgnoClass1'
      }
    },
    // 表格全选事件
    listSelectionChange(val) {
      this.deleteParams.list.length = 0
      this.deleteParams.bucketName = this.searchParams.bucketName
      val.forEach((ele) => {
        if (ele.size) {
          this.deleteParams.list.push({ recursive: false, prefix: ele.name })
        } else {
          this.deleteParams.list.push({ recursive: true, prefix: ele.name })
        }
      })
      this.tabularData = val
    },
    rowClick(a, b, c) {
      // console.log("a", a);
      // console.log("b", b);
      // console.log("c", c);
      this.currentImageIndex = this.tableData.findIndex(
        (item) => item.name1 === a.name1
      )
      this.previewImgUrl = ''
      this.zipParams.prefix = a.name
      // console.log('this.searchParams.prefixthis.searchParams.prefix',this.searchParams.prefix);
      if (!a.size) {
        this.searchParams.prefix = a.name
        this.findList()
        if (this.isOpen == true) {
          this.breadcrumbItems.push({ name: a.name1, url: a.name })
          this.isOpen = false
        }
      } else if (a.type) {
        this.openDetails(a, this.currentImageIndex)
      }
    },
    handleClick(data) {
      console.log('datatdadsa', data)
      this.searchParams.bucketName = data
      this.searchParams.prefix = ''
      this.breadcrumbItems = []
      this.findList()
    },
    handleClick1(data, index) {
      // 保持 bucketName 不变
      this.searchParams.prefix = data.url
      if (index > 0) {
        this.breadcrumbItems.splice(index + 1)
        // console.log("this.breadcrumbItemstest11", this.breadcrumbItems);
      } else if (index == 0) {
        this.breadcrumbItems = []
        this.breadcrumbItems.push(data)
      }
      this.findList()
    },
    // 预览
    async openDetails(row, index) {
      this.currentImageIndex = index
      if (row.type.split('/')[0] == 'image') {
        this.previewDialog = true
        this.previewImgUrl =
          this.imgUrl + '/' + this.searchParams.bucketName + '/' + row.name
      } else {
        this.$message({
          message: '非图片类型不支持查看！',
          type: 'warning'
        })
      }
    },

    // 列表上面的下载
    download(row) {
      // console.log("row", row);
      if (row.size) {
        this.downloadFile(row)
      } else {
        this.downloadZip(row)
      }
    },
    // 文件夹下载
    async downloadZip(row) {
      const loading = this.$loading({
        lock: true,
        text: '文件夹下载中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
      try {
        // 发送请求到服务器获取 ZIP 文件
        const response = await downloadZip({
          bucketName: this.searchParams.bucketName,
          prefix: row.name
        })

        if (response) {
          // 创建一个指向Blob对象的URL
          const url = window.URL.createObjectURL(new Blob([response]))
          // 创建一个下载链接
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', row.name1 + '.zip') // 设置下载文件的名称
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
    // 文件下载
    async downloadFile(row) {
      const loading = this.$loading({
        lock: true,
        text: '文件下载中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.5)'
      })
      try {
        const res = await downfile({
          bucketName: this.searchParams.bucketName,
          prefix: row.name
        })
        if (res) {
          const url = window.URL.createObjectURL(new Blob([res]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', row.name1) // 设置下载文件的名称和扩展名
          document.body.appendChild(link)
          link.click()
          window.URL.revokeObjectURL(url) // 释放URL对象
          loading.close()
        }
      } catch (error) {
        console.error('下载失败:', error)
        loading.close()
      }
    },
    // 删除接口
    async deleteFile(row) {
      // console.log("row删除", row);
      if (row.size) {
        this.$confirm('是否确定删除？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async () => {
            const res = await deleteFile({
              fileName: row.name,
              bucketName: this.searchParams.bucketName
            })
            if (res.code == 0) {
              this.$message({
                message: '删除成功！',
                type: 'success'
              })
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
      } else {
        this.deleteParams.list.length = 0
        this.deleteParams.bucketName = this.searchParams.bucketName
        this.deleteParams.list.push({ recursive: true, prefix: row.name })
        this.deleteAll()
      }
    },
    // 列表方法
    async findList(data) {
      this.images.length = 0
      const res = await queryBucket(this.searchParams)
      if (res && res.code == '0') {
        this.tableData = res.data
        this.isOpen = true
        if (this.tableData) {
          this.tableData.forEach((ele, index) => {
            if (!ele.size) {
              if (ele.name.split('/')[ele.name.split('/').length - 1]) {
                ele.name1 = ele.name.split('/')[ele.name.split('/').length - 1]
              } else {
                ele.name1 = ele.name.split('/')[ele.name.split('/').length - 2]
              }
            } else {
              if (ele.name.split('/')[ele.name.split('/').length - 1]) {
                ele.name1 = ele.name.split('/')[ele.name.split('/').length - 1]
              } else {
                ele.name1 = ele.name.split('/')[ele.name.split('/').length - 2]
              }
              if (ele.type.split('/')[0] === 'image') {
                this.images.push(
                  this.imgUrl +
                    '/' +
                    this.searchParams.bucketName +
                    '/' +
                    ele.name
                )
              }
              // console.log('this.images',this.images);
            }
          })
        }
      } else {
      }
    }
  }
}
</script>
<style scoped lang="scss">
.batchPreview{
  padding: 20px;
  height: 500px;
  overflow-y: auto;
}
.prev-button {
  position: absolute;
  top: 50%;
}
.bg-button {
  margin: 0 auto;
  width: 60%;
}
.next-button {
  position: absolute;
  top: 50%;
  right: 0;
}
.image-preview img {
}
.el-breadcrumb__inner,
.el-breadcrumb__inner a {
  font-size: 20px;
}
.table {
  width: 100%;

  .el-table::before {
    height: 0;
  }
  .table_button {
    width: 420px;
    float: right;
  }
  .sjzyyhm {
    margin-left: 5px;
    width: 130px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  .sjzyrow {
    font-size: 12px;
    width: 100%;
    margin-top: 5px;
    padding-left: 10px;
    padding-right: 10px;
    padding-bottom: 10px;
  }

  .sjzybgxq {
    font-size: 18px;
  }
  .wtp {
    width: 90%;
    // height: 17vh;
    height: 180px;
    margin: 0 auto;
    line-height: 280px;
    text-align: center;
    overflow: hidden;
  }
  .ytp {
    width: 90%;
    height: 180px;
    margin: 0 auto;
    box-sizing: border-box;
    overflow: hidden;
  }
  .imgname {
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 200px;
    margin-bottom: 10px;
  }
  .imgnamelocation {
    font-size: 14px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 600px;
    text-align: center;
    margin-bottom: 10px;
    margin-top: 10px;
  }
  .imgGrid {
    border: 1px solid #9ea9b4;
    padding: 20px;
    margin-bottom: 20px;
  }
  .contentListTable_bj {
    width: 99%;
    height: 100%;
    background: #ffffff;
    padding: 26px;
    margin: 9px;
  }

  .el-table__row {
    .cell {
      .el-tooltip {
        width: 180px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }
    }
  }
  .el-card__body {
    padding: 25px;
  }
  .remark {
    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    margin-top: 3px;
    color: #959595;
    text-overflow: -o-ellipsis-lastline;
    overflow: hidden; //溢出内容隐藏
    text-overflow: ellipsis; //文本溢出部分用省略号表示
    display: -webkit-box; //特别显示模式
    -webkit-box-orient: vertical; //盒子中内容竖直排列
    -webkit-line-clamp: 2; //行数
    line-clamp: 2;
    height: 38px;
    line-height: 19px;
    padding-left: 10px;
    padding-right: 10px;
  }

  .sjzytype {
    width: 22px;
    vertical-align: middle;
  }

  .bg-content {
    font-size: 12px;
    width: 100%;
    overflow: hidden;
    padding-left: 10px;
  }
  .panelMenu {
    max-height: calc(100vh - 82px);
    height: calc(100vh - 82px);
  }
}

.list {
  width: 40px;
  height: 33.04px;
  background: #ffffff;
  border: 1px solid #9ea9b4;
  border-bottom-left-radius: 4px;
  border-top-left-radius: 4px;
  padding-top: 7px;
  margin-top: -8px;
  cursor: pointer;

  img {
    vertical-align: middle;
    display: block;
    margin: 0 auto;
    width: 20px;
  }
}
</style>
<style>
.contentListTable .el-loading-spinner {
  font-size: 10px;
}

.contentListTable .el-loading-mask .el-loading-spinner .el-loading-text {
  font-size: 8px;
}

.el-loading-parent--relative {
  z-index: 1;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.el-row {
  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}
.el-button--primary {
  background-color: #005397;
  border-color: #005397;
}

.el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #005397 !important;
  color: #fff;
}

.active {
  display: none;
}
</style>
<style lang="scss">
/**
改变边框颜色
 */
.table .el-table--border,
.el-table--group {
  border: 1.4px solid #eff2f5 !important;
}

/**
改变表格内竖线颜色
 */
.table .el-table--border td,
.el-table--border th,
.el-table__body-wrapper .el-table--border.is-scrolling-left ~ .el-table__fixed {
  border: 1.4px solid #eff2f5 !important;
}

/**
改变表格内行线颜色
 */
.table .el-table td,
.el-table th.is-leaf {
  border: 1.4px solid #eff2f5 !important;
  font-size: 18px !important;
}

.table .el-table thead tr th {
  border: 1.4px solid #eff2f5 !important;
  font-family: "PingFang SC" !important;
  font-style: normal !important;
  font-weight: 600 !important;
  font-size: 18px !important;
  line-height: 18px;
  color: #202428 !important;
  height: 58px;
}

.sjzyzttp {
  width: 24px;
  vertical-align: middle;
}

/* g改变表头字体颜色 */
.table .el-table thead {
  background: #fafafa !important;
  border: 1.4px solid #eff2f5 !important;
}

.table .input-with-select .el-input__inner {
  background: rgba(255, 255, 255, 0.4) !important;
  border: 1.4px solid #d9d9d9 !important;
  height: 42px;
}

.table .sczdinput .el-input__inner {
  padding-right: 50px;
}

.table .el-dialog__body {
  padding: 0;
}

.table .xzfx .el-dialog__body {
  padding: 0;
}

.table .el-dialog__header {
  text-align: left;
  padding-left: 20px;
  padding: 2px 8px 8px 8px;
  line-height: 45px;
  background: #ffffff;
  border-bottom: 1.4px solid #eff2f5;
}

.table .el-dialog__headerbtn .el-dialog__close {
  font-size: 25px;
}

.table .el-dropdown-menu__item {
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 400;
  font-size: 18px !important;
  text-align: center;
  padding: 0 !important;
}

.table .el-dropdown-menu__item:hover {
  background: #f5f5f5 !important;
  color: #202428;
}

.table .search .el-input__suffix {
  right: 11px !important;
  -webkit-transition: all 0.3s;
  transition: all 0.3s;
  cursor: pointer;
}

tr.current-row > td,
.el-table__body tr:hover > td {
  background: #ddefff;
  border: 1.4px solid #eff2f5;
}

.table .classB {
  overflow-y: auto;
  height: calc(100vh - 270px);
  max-height: calc(100vh - 270px);
  margin-top: 50px;
  cursor: pointer;
  width: 100%;
}
.contentListTable_bj_breadcrumb {
  height: 40px;
}
.cardList {
  overflow-y: auto;
  overflow-x: hidden;
}

.ellipsis {
  display: -webkit-box;
  overflow: hidden;
  white-space: normal !important;
  text-overflow: ellipsis;
  word-wrap: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.table .el-popover {
  max-height: 120px;
  overflow: auto;
}

.test {
  height: 50px;
  transition: all 0.4s;
  -moz-transition: all 0.4s;
  -webkit-transition: all 0.4s;
  -o-transition: all 0.4s;
  // background: url("../../assets/images/dzt.png");
  margin-top: -2px;
  border-radius: 8px 8px 0px 0px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
}

.test:hover {
  background-size: 120% 120%;
}

.test1 {
  height: 50px;
  transition: all 0.4s;
  -moz-transition: all 0.4s;
  -webkit-transition: all 0.4s;
  -o-transition: all 0.4s;
  // background: url("../../assets/images/Frame 350.png");
  margin-top: -2px;
  border-radius: 8px 8px 0px 0px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
}

.test1:hover {
  background-size: 120% 120%;
}

.test2 {
  height: 50px;
  transition: all 0.4s;
  -moz-transition: all 0.4s;
  -webkit-transition: all 0.4s;
  -o-transition: all 0.4s;
  // background: url("../../assets/images/mzt.png");
  margin-top: -2px;
  border-radius: 8px 8px 0px 0px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
}

.test2:hover {
  background-size: 120% 120%;
}

.test3 {
  height: 50px;
  transition: all 0.4s;
  -moz-transition: all 0.4s;
  -webkit-transition: all 0.4s;
  -o-transition: all 0.4s;
  // background: url("../../assets/images/sb.png");
  margin-top: -2px;
  border-radius: 8px 8px 0px 0px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
}

.test3:hover {
  background-size: 120% 120%;
}

.contentListTable .el-badge__content.is-fixed {
  right: 6px !important;
  top: -2px;
  width: 20px;
  font-size: 12px;
}

.el-input-group__append button.el-button,
.el-input-group__append div.el-select .el-input__inner,
.el-input-group__append div.el-select:hover .el-input__inner,
.el-input-group__prepend button.el-button,
.el-input-group__prepend div.el-select .el-input__inner,
.el-input-group__prepend div.el-select:hover .el-input__inner {
  background-color: none;
}

.table .el-row {
  &:last-child {
  }
}

.table .el-col {
  border-radius: 4px;
}

.table .grid-content {
  border-radius: 4px;
}

.table .el-icon-s-operation {
  color: #66b1ff;
  font-size: 18px;
  margin-top: 3px;
  margin-left: 6px;
}

.table .el-popover {
  overflow: hidden;
  background: none;
  border: none;
}
</style>
