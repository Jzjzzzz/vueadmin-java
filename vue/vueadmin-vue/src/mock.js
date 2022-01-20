const Mock = require('mockjs')

const Random = Mock.Random

let Result = {
    code: 200,
    msg: '操作成功',
    data: null
}

Mock.mock('/captcha', 'get', () => {
    Result.data = {
        token: Random.string(32),
        captchaImg: Random.dataImage('100x40', 'p7n5w')
    }
    return Result
})

Mock.mock('/login', 'post', () => {
    //无法在header中传入数jwt

    return Result
})

Mock.mock('/sys/userInfo', 'get', () => {
    Result.data = {
        id: '1',
        username: 'test',
        avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
    }
    return Result
})

Mock.mock('/logout', 'post', () => {

    return Result
})

Mock.mock('/sys/menu/nav', 'get', () => {
    let nav = [
        {
            name: "SysManga",
            title: "系统管理",
            icon: "el-icon-s-operation",
            path: "",
            component: "",
            children: [
                {
                    name: "SysUser",
                    title: "用户管理",
                    icon: "el-icon-s-custom",
                    path: "/sys/users",
                    component: "sys/User",
                    children: [],
                },
                {
                    name: "SysRole",
                    title: "角色管理",
                    icon: "el-icon-rank",
                    path: "/sys/roles",
                    component: "sys/Role",
                    children: [],
                },
                {
                    name: "SysMenu",
                    title: "菜单管理",
                    icon: "el-icon-menu",
                    path: "/sys/menus",
                    component: "sys/Menu",
                    children: [],
                },
            ],
        },
        {
            name: "SysTools",
            title: "系统工具",
            icon: "el-icon-s-tools",
            path: "",
            component: "",
            children: [
                {
                    name: "SysDict",
                    title: "数字字典",
                    icon: "el-icon-s-order",
                    path: "/sys/dicts",
                    component: "",
                    children: [],
                },
            ],
        },
    ]
    let authoritys = []
    Result.data = {
        nav: nav,
        authoritys: authoritys
    }
    return Result
})