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
      reserveMessage: [],
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
      this.axios.get("type")
      .then(
        res=>{
          this.reserveMessage = res.data.body;
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
