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
                <el-button v-if="item.state === 0" @click="cancelOrder(item)" round>取消订单</el-button>
                <el-button v-if="item.state === 1" type="info" round disabled>已入住</el-button>
                <el-button v-if="item.state === 2" type="info" round disabled>已退房</el-button>
                <el-button v-if="item.state === 3" type="info" round disabled>已取消</el-button>
              </el-col>
          </el-tag>
        </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data(){
    return {
      orderMessage: [],
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
    cancelOrder(item){
      this.axios.patch("order", item)
      .then(
        res=>{
          this.initData();
          showMessage(res.data.message, res.data.code);
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
