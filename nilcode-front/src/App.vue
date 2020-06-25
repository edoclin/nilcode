<template>
    <div id="app">

        <el-container>
            <el-header height="" v-show="$route.meta.keepLive">
                <GlobalNavigation/>
            </el-header>
            <el-main style="min-height: 380px">
                <router-view/>
            </el-main>
            <el-footer height="0%" v-show="$route.meta.keepLive">
                <Footer/>
            </el-footer>
        </el-container>

    </div>
</template>

<script>

    import GlobalNavigation from "@/views/GlobalNavigaton";
    import Footer from "@/views/Footer";

    export default {
        name: 'app',
        components: {
            Footer,
            GlobalNavigation
        }
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
    }
</style>
