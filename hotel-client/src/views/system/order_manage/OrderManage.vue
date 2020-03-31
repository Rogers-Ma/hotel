<template>
  <div style="text-align: left;">
    <!-- 搜索框 -->
    <div style="text-align: center;">
      <el-row :gutter="20">
        <el-col :span="3"><el-input size="small" v-model="searchData.realName" placeholder="请输入客户姓名" @change="search" clearable></el-input></el-col>
        <el-col :span="3">
          <el-select v-model="searchData.state" placeholder="请选择订单状态" size="small" @change="search" clearable>
            <el-option
              v-for="item in states"
              :key="item.state"
              :label="item.label"
              :value="item.state">
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
      <el-row>
        <el-col>
          <el-button type="warning" size="small" @click="showAdd">添加</el-button>
          <el-button type="warning" size="small" @click="search">刷新</el-button>
        </el-col>
      </el-row>
      <br>
    </div>

    <!-- 表格 -->
    <div>
      <el-table
        :data="tableData"
        border>
        <el-table-column prop="customer.realName" label="客户姓名"></el-table-column>
        <el-table-column prop="customer.telephone" label="手机号"></el-table-column>
        <el-table-column prop="room.type.name" label="房间类型"></el-table-column>
        <el-table-column prop="room.number" label="房间号"></el-table-column>
        <el-table-column prop="startDate" label="入住时间"></el-table-column>
        <el-table-column prop="endDate" label="退房时间"></el-table-column>
        <el-table-column prop="price" label="入住费用"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="small" v-if="scope.row.state === 0" @click="checkIn(scope.$index)">入住</el-button>
            <el-button size="small" v-if="scope.row.state === 1" @click="checkOut(scope.$index)">退房</el-button>
            <el-tag :type="'success'" v-if="scope.row.state === 2" disable-transitions>已退房</el-tag>
            <el-tag :type="'error'" v-if="scope.row.state === 3" disable-transitions>用户已取消</el-tag>
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
        <el-form-item prop="date1" label="入住日期" :label-width="formLabelWidth" style="margin-right:30px">
          <el-date-picker
            v-model="formData.date1"
            type="date"
            placeholder="入住日期"
            :picker-options="pickerOptions0"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
            size="small">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="date2" label="退房日期" :label-width="formLabelWidth" style="margin-right:30px">
          <el-date-picker
            v-model="formData.date2"
            type="date"
            placeholder="退房日期"
            :picker-options="pickerOptions1"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
            size="small">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="typeId" label="房间类型" :label-width="formLabelWidth" style="margin-right:30px">
          <el-select v-model="formData.typeId" placeholder="请选择房间类型" size="small">
            <el-option
              v-for="item in types"
              :key="item.id"
              :label="item.name"
              :value="item.id"
              :disabled="item.count<1"
              >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="用户名" :label-width="formLabelWidth" style="margin-right:30px">
          <el-input v-model="formData.name" autocomplete="off" size="small"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="add" size="small">确定</el-button>
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
    var validateName = (rule, value, callback) => {
      this.axios.get('customer-manage', {params: {name: value}})
        .then(
          res => {
            if (res.data.body === null) {
              callback(new Error('用户名错误'))
            } else {
              this.formData.customerId = res.data.body.id
              callback()
            }
          },
          error => {
            console.log(error)
            callback(new Error('用户名错误'))
          }
        )
    }
    return {
      pickerOptions0: {
        disabledDate: (time) => {
          if (this.formData.date2 != null && this.formData.date2 !== '') {
            return time.getTime() < Date.now() - (24 * 3600 * 1000) || time.getTime() >= new Date(this.formData.date2) - 24 * 3600 * 1000
          } else {
            return time.getTime() < Date.now() - 24 * 3600 * 1000
          }
        }
      },
      pickerOptions1: {
        disabledDate: (time) => {
          if (this.formData.date1 != null && this.formData.date1 !== '') {
            return time.getTime() <= new Date(this.formData.date1) || time.getTime() < Date.now() - (24 * 3600 * 1000)
          } else {
            return time.getTime() < Date.now() - 24 * 3600 * 1000
          }
        }
      },
      types: [],
      states: [
        {
          state: 0,
          label: '待使用'
        },
        {
          state: 1,
          label: '已入住'
        },
        {
          state: 2,
          label: '已退房'
        },
        {
          state: 3,
          label: '已取消'
        }
      ],
      searchData: {},
      pageInfo: {
        pageNo: 1,
        pageSize: 10
      },
      pageSizes: [
        10,
        20
      ],
      countLine: 0,
      formLabelWidth: '80px',
      dialogFormVisible: false,
      formData: {},
      rules: {
        date1: [
          {required: true, message: '请选择入住日期', trigger: 'blur'}
        ],
        date2: [
          {required: true, message: '请选择退房日期', trigger: 'blur'}
        ],
        typeId: [
          {required: true, message: '请输入房间类型', trigger: 'change'}
        ],
        name: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {validator: validateName, message: '用户名不存在', trigger: 'blur'}
        ]
      },
      tableData: []
    }
  },
  mounted () {
    this.refreshTable()
  },
  methods: {
    refreshTable () {
      this.pageInfo.condition = this.searchData
      this.axios.get('/order-manage', {params: this.pageInfo})
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
    showAdd () {
      this.dialogState = 'add'
      this.formData = {}
      this.setCustomerInfo()
      this.setTypeInfo()
      this.dialogFormVisible = true
    },
    setCustomerInfo () {

    },
    setTypeInfo () {
      this.axios.get('type')
        .then(
          res => {
            this.types = res.data.body
          },
          error => {
            console.log(error)
            this.showMessage('服务器启动异常')
          }
        )
    },
    add () {
      this.$refs.formData.validate((valid) => {
        if (valid) {
          this.dialogFormVisible = false
          delete this.formData.name
          this.axios.post('order', this.$qs.stringify(this.formData)).then(
            res => {
              this.showMessage(res.data.message, res.data.code)
              this.refreshTable()
            },
            error => {
              console.log(error)
            }
          )
        }
      })
    },
    search () {
      this.refreshTable()
    },
    reset () {
      this.searchData.realName = ''
      this.searchData.state = ''
      this.refreshTable()
    },
    checkIn (index) {
      this.axios.patch('/order-manage', this.tableData[index])
        .then(
          response => {
            this.showMessage(response.data.message, response.data.code)
            this.refreshTable()
          },
          error => {
            console.log(error)
            this.showMessage('访问服务器异常')
          }
        )
    },
    checkOut (index) {
      console.log(JSON.stringify(this.tableData[index]))
      this.axios.patch('/order-manage', this.tableData[index])
        .then(
          response => {
            this.showMessage(response.data.message, response.data.code)
            this.refreshTable()
          },
          error => {
            console.log(error)
            this.showMessage('访问服务器异常')
          }
        )
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

    confirmWarning (message) {
      return this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
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
