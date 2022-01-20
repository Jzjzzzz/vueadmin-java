<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>VueAdmin后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ userInfo.username
              }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link to="/userCenter"> 个人中心 </router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="https://space.bilibili.com/13491144">视频讲解</el-link>
          <el-link href="http://markerhub.com">网站</el-link>
        </div>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <div style="margin: 0 15px">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from "./inc/SideMenu";
import Tabs from "./inc/Tabs";
export default {
  name: "Home",
  components: {
    SideMenu,
    Tabs,
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: "",
      },
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((response) => {
        this.userInfo = response.data.data;
      });
    },
    logout() {
      this.$axios.post("/logout").then((response) => {
        localStorage.clear();
        sessionStorage.clear();
        this.$store.commit("resetState");
        this.$router.push("/login");
      });
    },
  },
};
</script>

<style scoped>
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.el-container {
  padding: 0;
  margin: 0;
  height: 100vh;
}
.el-header {
  background-color: #17b3a3;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  line-height: 200px;
}

.el-main {
  color: #333;
  padding: 0;
}
a {
  text-decoration: none;
}
</style>