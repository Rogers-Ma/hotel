<template>
  <div>
    <div>
        <el-row v-for="(item,index) of orderMessage" :gutter="4" :key="index">
          <el-tag type="success" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); width: 60%; height: 100%">
              
              <el-col :span="5">
                <h1>{{item.room.type.name}}</h1>
              </el-col>
              
              <el-col :span="5" style="text-align: left">
                <h4>入住日期 : {{item.startDate}}</h4>
                <h4>退房日期 : {{item.endDate}}</h4>
              </el-col>

              <el-col :span="5" style="text-align: left">
                <h4>消费 : {{item.price}}</h4>
              </el-col>

              <el-col :span="5" style="line-height: 95px">
                <el-button v-if="item.state === 0" @click="reserve(item)" round>取消订单</el-button>
                <el-button v-if="item.state === 1" type="info" round disabled>已入住</el-button>
                <el-button v-if="item.state === 2" type="info" round disabled>已退房</el-button>
                <el-button v-if="item.state === 3" type="info" round disabled>已取消</el-button>
              </el-col>
          </el-tag>
        </el-row>
    </div>
    <div>
      <el-dialog title="入住时间" :visible.sync="dialogFormVisible">
        <el-form :model="orderInfo">
          <div class="block">
            <el-date-picker
              v-model="orderInfo.date"
              type="daterange"
              range-separator="至"
              start-placeholder="入住时间"
              end-placeholder="退房时间">
            </el-date-picker>
          </div>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data(){
    return {
      orderMessage: [],
      dialogFormVisible: false,
      orderInfo: {
        typeId: 123,
        customerId: 1233,
        date: []
      },
      formLabelWidth: "80px",
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.axios.get("order", {params : {customerId : this.$token.getUser().id}})
      .then(
        res=>{
          this.orderMessage = res.data.body;
        },
        error=>{
          this.showMessage("服务器启动异常");
        }
      )
    },

    reserve(item) {
      this.orderInfo.typeId = item.id;
      this.orderInfo.customerId = this.$token.getUser().id;
      this.dialogFormVisible = true
    },
    submitForm() {
      this.axios.post("/order",this.$qs.stringify(this.orderInfo))
      .then(
        res=>{
          this.initData();
          this.dialogFormVisible = false;
          this.showMessage(res.data.message, res.data.code);
        },
        error=>{
          this.showMessage("服务器未启动");
        }
      )
    },
    showMessage(message, type="error") {
      this.$notify({
        title: "提示",
        message: message,
        position: 'bottom-right',
        type: type,
        // 弹窗停留时间
        duration: 1000
      });
    },
  }
}
</script>
