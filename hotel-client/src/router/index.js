import Vue from 'vue'
import Router from 'vue-router'
import BackMain from '@/components/BackMain'
import CustomerMain from '@/components/CustomerMain'
import Login from '@/components/Login'
import UserManage from '@/views/system/user_manage/UserManage'
import TypeManage from '@/views/system/type_manage/TypeManage'
import RoomManage from '@/views/system/room_manage/RoomManage'
import OrderManage from '@/views/system/order_manage/OrderManage'
import Order from '@/views/client/Order'
import Home from '@/views/client/Home'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/system',
      name: 'BackMain',
      component: BackMain,
      redirect: '/system/user-manage',
      children: [
        {
          path: 'user-manage',
          name: 'UserManage',
          component: UserManage
        },
        {
          path: 'type-manage',
          name: 'TypeManage',
          component: TypeManage
        },
        {
          path: 'room-manage',
          name: 'RoomManage',
          component: RoomManage
        },
        {
          path: 'order-manage',
          name: 'OrderManage',
          component: OrderManage
        },
      ]
    },    
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/customer',
      name: 'CustomerMain',
      component: CustomerMain,
      redirect: '/customer/home',
      children: [
        {
          path: 'home',
          name: 'Home',
          component: Home
        },
        {
          path: 'order',
          name: 'Order',
          component: Order
        },
      ]
    },
  ]
})
