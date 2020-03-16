import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import axios from 'axios'
import Token from './api/token'
import qs from 'qs'

Vue.prototype.$qs = qs
Vue.prototype.axios = axios
Vue.use(ElementUI)
Vue.use(Token)
Vue.config.productionTip = false

axios.defaults.timeout = 1000
axios.defaults.baseURL = 'http://localhost:8088'

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
