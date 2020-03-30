const cache = {
  customer: null,
  admin: null
}

export const isEmpty = function (obj) {
  if (obj === undefined || obj == null) {
    return true
  }
  if ((Array.isArray(obj) || typeof obj === 'string') && obj.length === 0) {
    return true
  }
  return false
}

export const getCustomer = function () {
  if (cache.customer === null) {
    const customer = localStorage.getItem('$customer')
    if (!isEmpty(customer)) {
      cache.customer = JSON.parse(customer)
    }
  }
  return cache.customer
}

export const setCustomer = function (customer) {
  cache.customer = customer
  localStorage.setItem('$customer', JSON.stringify(customer))
}

export const getAdmin = function () {
  if (cache.admin === null) {
    const admin = localStorage.getItem('$admin')
    if (!isEmpty(admin)) {
      cache.admin = JSON.parse(admin)
    }
  }
  return cache.admin
}

export const setAdmin = function (admin) {
  cache.admin = admin
  localStorage.setItem('$admin', JSON.stringify(admin))
}

export default {
  getAdmin,
  setAdmin,
  getCustomer,
  setCustomer,
  isEmpty,
  isCustomerLogin () {
    return !isEmpty(getCustomer())
  },

  isAdminLogin () {
    return !isEmpty(getAdmin())
  },

  customerLogout () {
    setCustomer(null)
  },

  adminLogout () {
    setAdmin(null)
  }
}
