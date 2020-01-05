
import Token from './token.js' 

export default {
  install(Vue) {
    Vue.prototype.$token = Token;
  },
  Token
};
