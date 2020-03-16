<template>
  <div class="login-container">
    <el-form :model="loginForm" :rules="rules2"
      status-icon
      ref="loginForm"
      label-position="left"
      label-width="0px"
      class="demo-ruleForm login-page">
        <h3 class="title">系统登录</h3>
        <el-form-item prop="name">
            <el-input type="text"
                v-model="loginForm.name"
                auto-complete="off"
                placeholder="用户名"
            ></el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input type="password"  v-model="loginForm.password" auto-complete="off" placeholder="密码"
            ></el-input>
        </el-form-item>
        <el-form-item prop="isAdmin">
          <el-radio v-model="isAdmin" label="1">我是顾客</el-radio>
          <el-radio v-model="isAdmin" label="2">我是管理员</el-radio>
        </el-form-item>
        <el-form-item style="width:100%;">
            <el-button type="primary" style="width:100%;" @click="login">登录</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  data () {
    return {
      loginForm: {
        name: '',
        password: ''
      },
      isAdmin: '1',
      rules2: {
        name: [{required: true, message: '请输入账号', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    login () {
      if (this.isAdmin === '1') {
        this.axios.post('customer-login', this.loginForm)
          .then(
            res => {
              if (res.data.body != null) {
                this.$token.setCustomer(res.data.body)
                this.$router.push({name: 'CustomerMain'})
              } else {
                this.showMessage(res.data.message, res.data.code)
              }
            },
            error => {
              console.log(error)
            }
          )
      } else if (this.isAdmin === '2') {
        this.axios.post('admin-login', this.loginForm)
          .then(
            res => {
              if (res.data.body != null) {
                this.$token.setAdmin(res.data.body)
                this.$router.push({name: 'BackMain'})
              } else {
                this.showMessage(res.data.message, res.data.code)
              }
            },
            error => {
              console.log(error)
            }
          )
      }
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
    }
  }
}
</script>

<style scoped>
.login-container {
    width: 100%;
    height: 100%;
}
.login-page {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
}
label.el-checkbox.rememberme {
    margin: 0px 0px 15px;
    text-align: left;
}
</style>
