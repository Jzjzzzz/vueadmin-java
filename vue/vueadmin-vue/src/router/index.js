import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
import User from '../views/sys/User'
import Role from '../views/sys/Role'
import Menu from '../views/sys/Menu'
import axios from 'axios'
import store from '../store'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/index',
        name: 'Index',
        component: Index
      },
      {
        path: '/userCenter',
        name: 'userCenter',
        component: () => import('../views/UserCenter.vue')
      },
      // {
      //   path: '/sys/users',
      //   name: 'SysUser',
      //   component: User
      // },
      // {
      //   path: '/sys/roles',
      //   name: 'SysRole',
      //   component: Role
      // },
      // {
      //   path: '/sys/menus',
      //   name: 'SysMenu',
      //   component: Menu
      // }
    ]
  },

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {

  let hasRoute = store.state.menus.hasRoute
  if (!hasRoute) {
    axios.get('/sys/menu/nav', {
      headers: {
        Authorization: localStorage.getItem("token")
      }
    }).then(response => {
      //拿到menuList
      store.commit("setMenuList", response.data.data.nav)
      //拿到用户权限
      store.commit("setPermList", response.data.data.authoritys)
      //动态绑定路由
      let newRoutes = router.options.routes
      response.data.data.nav.forEach(menu => {
        if (menu.children) {
          menu.children.forEach(e => {
            //转成路由
            let route = menuToRoute(e)
            //把路由添加到路由管理中
            if (route) {
              newRoutes[0].children.push(route)
            }
          })
        }
      })
      router.addRoutes(newRoutes)

      hasRoute = true
      store.commit("changeRouteStatus",hasRoute)
    })
  }


  next()
})
//导航转成路由
const menuToRoute = (menu) => {
  if (!menu.component) {
    return null
  }
  let route = {
    name: menu.name,
    path: menu.path,
    meta: {
      icon: menu.icon,
      title: menu.title
    }
  }
  route.component = () => import('@/views/' + menu.component + '.vue')
  return route
}

export default router
