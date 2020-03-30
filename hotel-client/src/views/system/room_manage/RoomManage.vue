<template>
  <div style="text-align: left;">
    <!-- 搜索框 -->
    <div style="text-align: center;">
      <el-row :gutter="10">
        <el-col :span="3"><el-input size="small" v-model="searchData.number" placeholder="房屋编号" @change="search" clearable></el-input></el-col>
        <el-col :span="3">
         <el-select v-model="searchData.typeId" placeholder="请选择房间类型" size="small" @change="search" clearable>
            <el-option
              v-for="item in types"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2"><el-button size="small" type="warning" @click="search">查询</el-button></el-col>
        <el-col :span="1"><el-button size="small" type="warning" @click="reset">重置</el-button></el-col>
      </el-row>
    </div>

    <!-- 添加 -->
    <div>
      <br>
      <el-row :gutter="10">
        <el-col :span="1.5"><el-button size="small" type="warning" @click="add">添加</el-button></el-col>
        <el-col :span="1.5"><el-button size="small" type="warning" @click="refreshTable">刷新</el-button></el-col>
      </el-row>
      <br>
    </div>

    <!-- 表格 -->
    <div>
      <el-table
        :data="tableData"
        border
        width = "800px"
        >
        <el-table-column
          prop="number"
          label="房间编号"
          width="330%">
        </el-table-column>

        <el-table-column
          prop="type.name"
          label="房间类型"
          width="330%">
        </el-table-column>

        <el-table-column
          prop="state"
          label="状态"
          width="300%"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag :type="'success'" v-show="scope.row.state === 0" disable-transitions>空闲</el-tag>
            <el-tag :type="'success'" v-show="scope.row.state === 1" disable-transitions>已占用</el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="330%">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit"   size="small" circle @click="edit(scope.$index)"></el-button>
            <el-button icon="el-icon-delete" size="small" circle @click="deleteById(scope.$index)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

   <!-- 分页 -->
    <div class="block" style="margin-bottom: 0px">
      <el-pagination
        @size-change="onPageSizeChange"
        @current-change="onPageChange"
        @prev-click="prevClick"
        @next-click="nextClick"
        :current-page="pageInfo.pageNo"
        :page-sizes="pageSizes"
        :page-size="pageInfo.pageSize"
        :total="countLine">
        layout="total, sizes, prev, next, jumper"
      </el-pagination>
    </div>

  <!--弹窗  -->
    <el-dialog title="房间类型信息" width="30%" v-if="dialogFormVisible" :visible.sync="dialogFormVisible">
      <el-form ref="formData" :rules="rules" :model="formData">
        <el-form-item prop="number" label="房间编号" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.number" autocomplete="off" size="small"></el-input>
        </el-form-item>
        <el-form-item prop="typeId" label="类型" :label-width="formLabelWidth" style="margin-right:30px">
          <el-select v-model="formData.typeId" placeholder="请选择房间类型" size="small">
            <el-option
              v-for="item in types"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Search from '@/components/Search'
export default {
  components: {
    Search
  },
  data () {
    return {
      dialogState: '',
      types: [],
      searchData: {
        number: '',
        typeId: ''
      },
      pageInfo: {
        pageNo: 1,
        pageSize: 10
      },
      pageSizes: [
        10, 20
      ],
      rules: {
        number: [
          {required: true, message: '请输入房间编号', trigger: 'blur'}
        ],
        typeId: [
          {required: true, message: '请选择房间类型', trigger: 'change'}
        ]
      },
      countLine: 0,
      formLabelWidth: '80px',
      dialogFormVisible: false,
      formData: {},
      tableData: []
    }
  },

  mounted () {
    this.refreshTable()
  },
  methods: {
    refreshTable () {
      this.axios.get('/type-manage')
        .then(
          response => {
            this.types = response.data.body
          },
          error => {
            console.log(error)
            this.showMessage('服务器未启动')
            return null
          }
        )

      this.pageInfo.condition = this.searchData
      this.axios.get('/room-manage', {params: this.pageInfo})
        .then(
          response => {
            this.tableData = response.data.body.content
            this.countLine = response.data.body.totalElements
          },
          error => {
            console.log(error)
            this.showMessage('服务器未启动')
          }
        )
    },
    add () {
      this.dialogState = 'add'
      this.formData = {}
      this.formData.state = 0
      this.dialogFormVisible = true
    },
    search () {
      this.refreshTable()
    },
    reset () {
      this.searchData.number = ''
      this.searchData.typeId = ''
      this.refreshTable()
    },
    showMessage (message, type = 'error') {
      this.$notify({
        title: '提示',
        message: message,
        position: 'bottom-right',
        type: type,
        // 弹窗停留时间
        duration: 1000
      })
    },
    edit (index) {
      this.dialogState = 'edit'
      this.formData = this.tableData[index]
      this.dialogFormVisible = true
    },
    deleteById (index) {
      this.confirmWarning('此操作将永久删除该项, 是否继续?').then(
        () => {
          this.axios.delete('/room-manage', {params: {'id': this.tableData[index].id}})
            .then(
              response => {
                this.refreshTable()
                if (response.data.message !== '') {
                  this.showMessage(response.data.message, response.data.code)
                }
              },
              error => {
                console.log(error)
                this.showMessage('删除失败')
              }
            )
        }
      )
    },
    confirmWarning (message) {
      return this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    },
    submitForm () {
      switch (this.dialogState) {
        case 'add':
          this.$refs.formData.validate((valid) => {
            if (valid) {
              this.axios.post('/room-manage', this.formData)
                .then(
                  response => {
                    this.refreshTable()
                    if (response.data.message !== '') {
                      this.showMessage(response.data.message, response.data.code)
                    }
                  },
                  error => {
                    console.log(error)
                    this.showMessage('服务器异常')
                  }
                )
              this.dialogFormVisible = false
            }
          })
          break
        case 'edit':
          this.$refs.formData.validate((valid) => {
            if (valid) {
              this.axios.patch('/room-manage', this.formData)
                .then(
                  response => {
                    this.refreshTable()
                    if (response.data.message !== '') {
                      this.showMessage(response.data.message, response.data.code)
                    }
                  },
                  error => {
                    console.log(error)
                    this.showMessage('修改失败')
                  }
                )
              this.dialogFormVisible = false
            }
          })
          break
      }
    },
    onPageChange (val) {
      this.pageTo(val)
      this.refreshTable()
    },
    onPageSizeChange (val) {
      this.pageInfo.pageSize = val
      this.refreshTable()
    },
    pageTo (pageNo) {
      this.pageInfo.pageNo = pageNo
      this.refreshTable()
    },
    prevClick (pageNo) {
      this.pageNo = pageNo - 1
      this.refreshTable()
    },
    nextClick (pageNo) {
      this.pageNo = pageNo + 1
      this.refreshTable()
    }
  }
}
</script>

<style scoped>
.el-checkbox.is-checked{
  border-inline-color: crimson;
  border-color: #E6A23C;
}
</style>
