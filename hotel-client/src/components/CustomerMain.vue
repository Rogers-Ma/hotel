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
          <el-dropdown-item command="personal-detail"><a>个人信息</a></el-dropdown-item>
          <el-dropdown-item command="password-change"><a>修改密码</a></el-dropdown-item>
          <el-dropdown-item command="logout">注销登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu>
    <div>
      <el-dialog title="个人信息" :visible.sync="showCustomerInfo" width="40%">
        <el-form :rules="rules" ref="customerInfo" :model="customerInfo">
          <el-form-item label="姓名" :label-width="formLabelWidth" style="margin-right:30px" prop="name">
            <el-input autocomplete="off" v-model="customerInfo.name" size="small"></el-input>
          </el-form-item>
          <el-form-item label="手机号" :label-width="formLabelWidth" style="margin-right:30px" prop="telephone">
            <el-input autocomplete="off" v-model="customerInfo.telephone" size="small"></el-input>
          </el-form-item>
          <el-form-item label="余额" :label-width="formLabelWidth" style="margin-right:30px" prop="balance">
            
            <el-row>
              <el-col :span="4" ><div style="text-align: left;"><span>￥{{customerInfo.balance}}</span></div></el-col>
              <el-col :span="8"><el-input type="number" placeholder="请输入充值金额" v-model="delta" autocomplete="off"></el-input></el-col>
              <el-col :span="6"><el-button @click="recharge">充值</el-button></el-col>
            </el-row>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="showCustomerInfo = false">取 消</el-button>
          <el-button type="primary" @click="changeCustomerInfo('customerInfo')">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    
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
  <el-main>
    <router-view/>
  </el-main>
</el-container>
  

</template>

<script>
export default {
    data() {
      var validateOldPassword = (rule, value, callback) => {
        if (value !== this.$token.getUser().password) {
          callback(new Error('密码错误'));
        } else {
          callback();
        }
      };
    
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.passwordChange.password1) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        user: {},
        delta: 0,
        isLogin: false,
        showCustomerInfo: false,
        customerInfo: {
          name: '',
          telephone: '',
          balance: 0
        },
        formLabelWidth: '80px',
        showPasswordChange: false,
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
            {validator: validatePass2, trigger: 'blur'}
          ],
          name: [
            {required: true, message: '姓名不能为空', trigger: 'blur'}
          ],
          telephone: [
            {required: true, message: '手机号不能为空', trigger: 'blur'}
          ],
          balance: [
            {required: true, message: '余额不能为空', trigger: 'blur'}
          ]
        }
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
            break
          }
          case 'personal-detail': {
            this.delta = 0
            this.showCustomerInfo = true
            let customer = this.$token.getUser()
            this.customerInfo.name = customer.realName
            this.customerInfo.telephone = customer.telephone
            this.customerInfo.balance = customer.balance
            break
          }
          case 'password-change': {
            this.passwordChange = {}
            this.showPasswordChange = true
            break
          }
        }
      },
      showMessage(message,type="error") {
        this.$notify({
          title: "提示",
          message: message,
          position: 'bottom-right',
          type: type,
          // 弹窗停留时间
          duration: 1000
        });
      },
      changePassword(passwordChange) {
        this.$refs[passwordChange].validate((valid) => {
          if (valid) {
            let customer = this.$token.getUser()
            customer.password = this.passwordChange.password1
            this.axios.patch("customer", customer)
            .then(
              res=>{
                this.showPasswordChange = false
                this.showMessage(res.data.message, res.data.code);
                this.$token.setUser(customer)
              },
              error=>{
                this.showPasswordChange = false
                this.showMessage("服务器未启动")
              }
            );
          } else {
              return;
            }
        })
      },
      changeCustomerInfo (customerInfo) {
        this.$refs[customerInfo].validate((valid) => {
          if (valid) {
            let customer = this.$token.getUser()
            customer.realName = this.customerInfo.name
            customer.telephone = this.customerInfo.telephone
            customer.balance = this.customerInfo.balance
            this.axios.patch("customer", customer)
            .then(
              res=>{
                this.showCustomerInfo = false
                this.showMessage(res.data.message, res.data.code);
                this.$token.setUser(customer)
              },
              error=>{
                this.showCustomerInfo = false
                this.showMessage("服务器未启动")
              }
            );
          } else {
              return;
            }
        })
      },
      recharge() {
        this.customerInfo.balance = parseFloat(this.customerInfo.balance) + parseFloat(this.delta);
        this.delta = 0;
      }
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