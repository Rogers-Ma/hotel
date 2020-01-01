<template>
  <el-container style="height: 700px; solid ">
    
    <el-header style="text-align: left; font-size: 15px">
      <b><span style="font-size: 20px">酒店后台管理</span></b>
      <span style="margin-left: 1250px">注销</span>
    </el-header>
    <el-container>
      <el-aside width="150px" >
        <el-row class="tac">
          <el-col >
            <el-menu
              router
              style="text-align: left"
              class="el-menu-vertical-demo"
              background-color="#545c64"
              text-color="#fff"
              active-text-color="#ffd04b"
              >

              <el-menu-item index="user-manage">
                <i class="el-icon-user-solid"></i>
                <span slot="title">客户管理</span>
              </el-menu-item>
              
              <el-menu-item index="type-manage">
                <i class="el-icon-s-management"></i>
                <span>房间分类管理</span>
              </el-menu-item>
              
              <el-menu-item index="room-manage">
                <i class="el-icon-s-home"></i>
                <span slot="title">房间管理</span>
              </el-menu-item>

              <el-menu-item index="order-manage">
                <i class="el-icon-s-goods"></i>
                <span slot="title">订单管理</span>
              </el-menu-item>

            </el-menu>
          </el-col>
        </el-row>
    </el-aside>
    <el-main>
      <router-view/>
    </el-main>

    </el-container>
  </el-container>
</template>

<script>
export default {
  data(){
    return {

    }
  },
  mounted() {
  },
  methods: {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  
  .el-aside {
    background-color: #B3C0D1;
    color: #333;
  }
</style>
