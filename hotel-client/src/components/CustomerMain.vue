<template>
<el-container>
  <el-header>
    
    <el-menu
      :default-active="'home'"
      class="el-menu-demo"
      mode="horizontal"
      background-color="#B3C0D1"
      text-color="#fff"
      active-text-color="#ffd04b"
      router
      style="height: 100%; width: 100%"
      >
      <img style="float: left" height="100%" src="static\image\logo.png"/>
      <el-menu-item index="home">主页</el-menu-item>
      <el-menu-item index="order">订单管理</el-menu-item>
      <el-dropdown v-if="isLogin" @command="handleCommand" style="float: right">
        <span style="height: 100%">
          {{user.name}}
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="password-change"><a>修改密码</a></el-dropdown-item>
          <el-dropdown-item command="logout">注销登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu>
  </el-header>
  <el-main>
    <router-view/>
  </el-main>
</el-container>
  

</template>

<script>
export default {
    data() {
      return {
        user: {},
        isLogin: false,
      }  
    },
    mounted(){
      this.initData()
    },
    activated() {
      this.initData()
    },
    methods: {
      initData(){
        this.isLogin = this.$token.isLogin();
        if (this.isLogin) {
          this.user = this.$token.getUser();
        }else{
          this.$router.push({name: 'Login'})
        }
      },
      handleCommand(command) {
        switch (command) {
          case 'logout': {
            this.$token.logout();
            this.isLogin = false;
            this.user = {};
            this.$router.push({
              name: 'Login'
            });
            break;
          }
          case 'personal-detail': {
            this.$router.push({ name: 'Profile' });
            break;
          }
          case 'password-change': {
            this.showPasswordChange = true;
            break;
          }
        }
      },
    }
}
</script>

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