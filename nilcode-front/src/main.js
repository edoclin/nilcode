import Vue from 'vue'
import App from './App.vue'
import routerConfig from './router';
import VueRouter from 'vue-router';
import axios from 'axios';
import VueClipboard from 'vue-clipboard2';
import config from './config'

axios.defaults.withCredentials = true;
Vue.config.productionTip = true;
routerConfig.mode = 'history';

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
};

const instance = axios.create({
    baseURL: config.axios.baseUrl,
    timeout: config.axios.timeOut,
});

Vue.prototype.axios = instance;

const router = new VueRouter(routerConfig);

router.beforeEach((to, from, next) => {
    /**
     * todo: 1. 请求后端一个被保护了的 url 如果需要登录 移除localstorage, 无需登录则继续
     *       2. 判断 localstorage 如果存在则放行 不存在跳转至登录页
     */
    let userInfo = localStorage.getItem('userInfo');
    if (to.path === '/admin/login') {
        window.console.log('放行login')
    } else if (to.path.startsWith('/admin')) {
        // todo:
        instance.get('users/check').then(value => {
            if (value.data.code === 4011) {
                localStorage.removeItem('userInfo')
            }
        });

        if (userInfo === null || userInfo === undefined || userInfo === '') {
            next({path: '/admin/login'})
        }
    }
    next();
});
Vue.use(VueRouter);
Vue.use(VueClipboard);

new Vue({
    render: h => h(App),
    router: router,
}).$mount('#app');

