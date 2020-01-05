
const cache = {
  token: null,
  user: null
};


export const isEmpty = function (obj) {
  if (obj == undefined || obj == null) {
    return true;
  }
  if ((Array.isArray(obj) || typeof obj === 'string') && obj.length === 0) {
    return true;
  }
  return false;
};

export const getToken = function () {
  if (cache.token === null) {
    cache.token = localStorage.getItem('$token');
  }
  return cache.token;
};

export const setToken = function (token) {
  cache.token = token;
  localStorage.setItem('$token', cache.token);
};

export const getUser = function () {
  if (cache.user === null) {
    const user = localStorage.getItem('$user');
    if (!isEmpty(user)) {
      cache.user = JSON.parse(user);
    }
  }
  return cache.user;
};

export const setUser = function (user) {
  cache.user = user;
  localStorage.setItem('$user', JSON.stringify(user));
};




export default {
  // parseToken,
  // getTokenBody,
  getToken,
  setToken,
  getUser,
  setUser,

  isLogin() {
    return !isEmpty(getToken()) && !isEmpty(getUser());
  },
  logout() {
    setToken(null);
    setUser(null);
  }
};