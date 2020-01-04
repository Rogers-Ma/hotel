<template>
  <el-container style="height: 700px; solid ">
    
    <el-header style="font-size: 15px">
      <span style="float: left; font-size: 20px"><b>酒店后台管理</b></span>
      <el-dropdown v-if="isLogin" @command="handleCommand">
        <a href="javascript:void(0)">
          <span class="user-name">
            {{"你好：" + Token.getToken().name}}
            <img src="/static/images/system/avator.jpg" />
          </span>
        </a>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="personal-detail">个人信息</el-dropdown-item>
          <el-dropdown-item command="password-change">
            <a>修改密码</a>
          </el-dropdown-item>
          <el-dropdown-item command="help">
            <a>帮助</a>
          </el-dropdown-item>

          <el-dropdown-item command="logout">注销登录</el-dropdown-item>
          
        </el-dropdown-menu>
      </el-dropdown>
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
              default-active="defaultActive"
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
      defaultActive:'user-manage'
    }
  },
  mounted() {
    if(this.Token.getToken() === null){
      this.$router.push({name: "Login"});
    }
  },
  activated(){},
  methods: {}
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
