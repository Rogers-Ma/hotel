<template>
  <el-container style="height: 700px; solid ">

    <el-header style="font-size: 15px">
      <span style="float: left; font-size: 20px"><b>酒店后台管理</b></span>
      <el-dropdown @command="handleCommand" style="float: right">
        <span >
          {{$token.getAdmin().name}}
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="password-change"><a>修改密码</a></el-dropdown-item>
          <el-dropdown-item command="logout">注销登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <div>
        <el-dialog title="修改密码" :visible.sync="showPasswordChange" width="30%">
          <el-form :model="passwordChange" ref="passwordChange" :rules="rules">
            <el-form-item label="旧密码" :label-width="formLabelWidth" prop="oldPassword" style="margin-right:30px">
              <el-input autocomplete="off" v-model="passwordChange.oldPassword" size="small"></el-input>
            </el-form-item>
            <el-form-item label="新密码" :label-width="formLabelWidth" prop="password1" style="margin-right:30px">
              <el-input autocomplete="off" v-model="passwordChange.password1" size="small"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" :label-width="formLabelWidth" prop="password2" style="margin-right:30px" disabled>
              <el-input autocomplete="off" v-model="passwordChange.password2" size="small"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="showPasswordChange = false" size="small">取 消</el-button>
            <el-button type="primary" @click="changePassword('passwordChange')" size="small">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-header>
    <el-container>
      <el-aside width="150px" >
        <el-row class="tac">
          <el-col >
            <el-menu
              :default-active="defaultActive"
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
  data () {
    var validateOldPassword = (rule, value, callback) => {
      if (value !== this.$token.getAdmin().password) {
        callback(new Error('密码错误'))
      } else {
        callback()
      }
    }
    var validatePassword2 = (rule, value, callback) => {
      if (value !== this.passwordChange.password1) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      defaultActive: this.getPath(),
      admin: {},
      isLogin: false,
      showPasswordChange: false,
      formLabelWidth: '80px',
      passwordChange: {},
      rules: {
        oldPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {validator: validateOldPassword, trigger: 'blur'}
        ],
        password1: [
          {required: true, message: '请输入新密码', trigger: 'blur'}
        ],
        password2: [
          {required: true, message: '确认新密码', trigger: 'blur'},
          {validator: validatePassword2, trigger: 'blur'}
        ]
      }
    }
  },
  mounted () {
    this.initData()
  },
  activated () {
    this.initData()
    console.log(this.getPath())
  },
  methods: {
    initData () {
      if (this.isLogin) {
        this.$router.push({name: 'Login'})
      }
    },
    getPath () {
      return this.$route.path.split('/')[this.$route.path.split('/').length - 1]
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
    changePassword (passwordChange) {
      this.$refs[passwordChange].validate((valid) => {
        if (valid) {
          let admin = this.$token.getAdmin()
          admin.password = this.passwordChange.password1
          this.axios.patch('admin', admin)
            .then(
              res => {
                this.showPasswordChange = false
                this.showMessage(res.data.message, res.data.code)
                this.$token.setAdmin(admin)
              },
              error => {
                console.log(error)
                this.showPasswordChange = false
                this.showMessage('服务器未启动')
              }
            )
        } else {
          return null
        }
      })
    },
    handleCommand (command) {
      switch (command) {
        case 'logout': {
          this.$token.adminLogout()
          this.isLogin = false
          this.user = {}
          this.$router.push({
            name: 'Login'
          })
          break
        }
        case 'personal-detail': {
          break
        }
        case 'password-change': {
          this.passwordChange = {}
          this.showPasswordChange = true
          break
        }
      }
    }
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
