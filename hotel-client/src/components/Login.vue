<template>
    <div class="login-container">
        <el-form :model="loginForm" :rules="rules2"
         status-icon
         ref="loginForm" 
         label-position="left" 
         label-width="0px" 
         class="demo-ruleForm login-page">
            <h3 class="title">系统登录</h3>
            <el-form-item prop="username">
                <el-input type="text" 
                    v-model="loginForm.username" 
                    auto-complete="off" 
                    placeholder="用户名"
                ></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input type="password"  v-model="loginForm.password" auto-complete="off" placeholder="密码"
                ></el-input>
            </el-form-item>
            <el-form-item prop="isAdmin">
              <el-radio v-model="loginForm.isAdmin" label="1">我是顾客</el-radio>
              <el-radio v-model="loginForm.isAdmin" label="2">我是管理员</el-radio>
            </el-form-item>
            <el-form-item style="width:100%;">
                <el-button type="primary" style="width:100%;" @click="login">登录</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
export default {
    data(){
        return {
            loginForm: {
                name: '',
                password: '',
                isAdmin: '1',
            },
            rules2: {
                username: [{required: true, message: '请输入账号', trigger: 'blur'}],
                password: [{required: true, message: '请输入密码', trigger: 'blur'}],
            },
        }
    },
    methods: {
        login(){
            // console.log(this.$route);
            console.log(this.$token.type);
            this.$token.type = "admin";
            console.log(this.$token.type);
            // this.token.type = "customer";

        }
    }
};
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