<template>
    <div>
        <el-container>
            <el-header height="0%">
                <el-row>
                    <el-col :xs="{span: 24}" :sm="{span: 8}">
                        <img :src="logo" width="30%" style="float: left">
                    </el-col>
                </el-row>
            </el-header>
            <el-divider/>
            <el-container>
                <el-aside width="auto">

                    <el-menu style="float: left" class="el-menu-vertical-demo"
                             @select="handleSelect"
                             :collapse="true">

                        <el-menu-item index="/admin/main/dashboard">
                            <i class="el-icon-menu"/>
                            <span slot="title">仪表盘</span>
                        </el-menu-item>

                        <el-menu-item index="/admin/main/edit" route="edit">
                            <i class="el-icon-edit"/>
                            <span slot="title">发布文章</span>
                        </el-menu-item>

                        <el-menu-item index="/admin/main/blog">
                            <i class="el-icon-folder-opened"/>
                            <span slot="title">文章管理</span>
                        </el-menu-item>

                        <el-menu-item index="/admin/main/files">
                            <i class="el-icon-upload"/>
                            <span slot="title">文件管理</span>
                        </el-menu-item>

                        <el-menu-item index="评论管理">
                            <i class="el-icon-notebook-2"/>
                            <span slot="title">评论管理</span>
                        </el-menu-item>

                        <el-menu-item index="/admin/main/classificationsAndTags">
                            <i class="el-icon-collection-tag"/>
                            <span slot="title">分类/标签</span>
                        </el-menu-item>
                        <el-menu-item index="/admin/main/links">
                            <i class="el-icon-link"/>
                            <span slot="title">链接管理</span>
                        </el-menu-item>
                        <el-menu-item index="系统设置">
                            <i class="el-icon-setting"/>
                            <span slot="title">系统设置</span>
                        </el-menu-item>
                        <el-menu-item @click="logout">
                            <i class="el-icon-close"/>
                            <span slot="title">退出登录</span>
                        </el-menu-item>

                    </el-menu>
                </el-aside>
                <el-main>
                    <router-view/>
                </el-main>
            </el-container>
            <el-footer></el-footer>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "AdminMain",
        data() {
            return {
                logo: require('@/assets/logo.png'),
                isCollapse: true,
            }
        },
        methods: {
            handleSelect(key) {
                this.$router.push(key)
            },
            handleClose(key, keyPath) {
                window.console.log(key, keyPath);
            },
            logout() {
                this.axios.get('https://www.nilcode.cn:8443/logout').then(value => {
                    if (value.data.code === 2009) {
                        localStorage.removeItem('userInfo');
                        this.$router.push('/admin/login')
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }
</style>
