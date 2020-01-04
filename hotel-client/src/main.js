import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import axios from 'axios'
import Token from './api/defined'

Vue.prototype.Token = Token;
Vue.prototype.axios = axios
Vue.use(ElementUI)
Vue.config.productionTip = false

axios.defaults.timeout = 1000
axios.defaults.baseURL = 'http://localhost:8088'

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
  
})
