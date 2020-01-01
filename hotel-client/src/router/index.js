import Vue from 'vue'
import Router from 'vue-router'
import BackMain from '@/components/BackMain'
import Login from '@/components/Login'
import UserManage from '@/views/system/user_manage/UserManage'
import TypeManage from '@/views/system/type_manage/TypeManage'
import RoomManage from '@/views/system/room_manage/RoomManage'
import OrderManage from '@/views/system/order_manage/OrderManage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'BackMain',
      component: BackMain,
      children: [
        {
          path: '/user-manage',
          name: 'UserManage',
          component: UserManage
        },
        {
          path: '/type-manage',
          name: 'TypeManage',
          component: TypeManage
        },
        {
          path: '/room-manage',
          name: 'RoomManage',
          component: RoomManage
        },
        {
          path: '/order-manage',
          name: 'OrderManage',
          component: OrderManage
        },
      ]
    },    
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
  ]
})
