<template>
  <div>
    <div>
        <el-row v-for="(item,index) of reserveMessage" :gutter="4" :key="index">
          <el-tag type="success" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); width: 60%; height: 100%">
              <el-col :span="10">
                <h1>{{item.name}}</h1>
              </el-col>
              <el-col :span="10" style="text-align: left">
                <h4>剩余数量: {{item.count}}</h4>
                <h4>价格: {{item.price}}</h4>
              </el-col>

              <el-col :span="4" style="line-height: 95px">
                <el-button v-if="item.count > 0" @click="reserve(item)" round>预定</el-button>
                <el-button v-if="item.count === 0" type="info" round disabled>满房</el-button>
              </el-col>
          </el-tag>
          <br/><br/>
        </el-row>
    </div>
    <div>
      <el-dialog title="入住时间" :visible.sync="dialogFormVisible">
        <el-form :model="orderInfo">
          <div class="block">
            <el-date-picker
                  v-model="orderInfo.date1"
                  type="date"
                  placeholder="入住日期"
                  :picker-options="pickerOptions0">
            </el-date-picker>
            <el-date-picker
                  v-model="orderInfo.date2"
                  type="date"
                  placeholder="退房日期"
                  :picker-options="pickerOptions1">
            </el-date-picker>
          </div>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
          <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      pickerOptions0: {
        disabledDate: (time) => {
          if (this.orderInfo.date2 !== '') {
            return time.getTime() < Date.now() - 1 || time.getTime() >= this.orderInfo.date2
          } else {
            return time.getTime() < Date.now()
          }
        }
      },
      pickerOptions1: {
        disabledDate: (time) => {
          return time.getTime() <= this.orderInfo.date1 || time.getTime() < Date.now()
        }
      },
      reserveMessage: [],
      dialogFormVisible: false,
      orderInfo: {
        typeId: '',
        customerId: '',
        date1: '',
        date2: ''
      },
      formLabelWidth: '80px'
    }
  },
  mounted () {
    this.initData()
  },
  methods: {
    initData () {
      this.axios.get('type')
        .then(
          res => {
            this.reserveMessage = res.data.body
          },
          error => {
            console.log(error)
            this.showMessage('服务器启动异常')
          }
        )
    },

    reserve (item) {
      this.orderInfo.typeId = item.id
      this.orderInfo.customerId = this.$token.getCustomer().id
      this.orderInfo.date1 = ''
      this.orderInfo.date2 = ''
      this.dialogFormVisible = true
    },
    submitForm () {
      this.axios.post('/order', this.$qs.stringify(this.orderInfo))
        .then(
          res => {
            this.initData()
            this.dialogFormVisible = false
            this.showMessage(res.data.message, res.data.code)
          },
          error => {
            console.log(error)
            this.showMessage('服务器未启动')
          }
        )
    },
    showMessage (message, type = '') {
      this.$notify({
        title: '提示',
        message: message,
        position: 'bottom-right',
        type: type,
        // 弹窗停留时间
        duration: 1000
      })
    }
  }
}
</script>
