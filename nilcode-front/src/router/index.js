// 详情页 & 后台管理 加载 markdown & qrcode2 插件耗时较长
const Detail =  () => import(/* webpackChunkName: 'chunk.long.js' */ '@/views/Detail');
const AdminEditor =  () => import(/* webpackChunkName: 'chunk.long.js' */ '@/views/AdminEditor');
// 访客
const Index =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/Index');
const About =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/About');
const Archive =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/Archive');
const Links =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/Links');
const Search =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/Search');
const NotFound =  () => import(/* webpackChunkName: 'chunk.view.js' */ '@/views/NotFound');
// 管理
const AdminLogin =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminLogin');
const Admin =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/Admin');
const AdminMain =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminMain');
const MainIndex =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminMainIndex');
const AdminFiles =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminFiles');
const AdminBlog =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminBlog');
const AdminType =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminClassificationAndTag');
const AdminLinks =  () => import(/* webpackChunkName: 'chunk.admin.js' */ '@/views/AdminLinks');

export default ({
    routes: [
        {
            path: '/',
            component: Index,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/index',
            component: Index,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/about',
            component: About,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/links',
            component: Links,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/archive',
            component: Archive,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/detail/:id',
            component: Detail,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/search/:type/:name',
            component: Search,
            meta: {
                keepLive: true
            },
        },
        {
            path: '/admin',
            meta: {
                keepLive: true
            },
            component: Admin,
            children: [
                {
                    path: 'login',
                    component: AdminLogin
                },
                {
                    path: 'main',
                    component: AdminMain,
                    children: [
                        {
                            path: '',
                            component: MainIndex
                        },
                        {
                            path: 'dashboard',
                            component: MainIndex
                        },
                        {
                            path: 'edit',
                            component: AdminEditor
                        },
                        {
                            path: 'files',
                            component: AdminFiles
                        },
                        {
                            path: 'blog',
                            component: AdminBlog
                        },
                        {
                            path: 'classificationsAndTags',
                            component: AdminType
                        },
                        {
                            path: 'links',
                            component: AdminLinks
                        },
                    ]
                },
            ]
        },
        {
            path: "/404",
            component: NotFound
        }, {
            path: "*",
            redirect: "/404"
        }

    ]
})
