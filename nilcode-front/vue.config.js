const CompressionPlugin = require('compression-webpack-plugin')


module.exports = {
    // 关闭 map 文件生成
    productionSourceMap: false,
    pages: {
        index: {
            // page 的入口
            entry: 'src/main.js',
            // 模板来源
            template: 'public/index.html',
            // 在 dist/index.html 的输出
            filename: 'index.html',
            // 当使用 title 选项时，
            // template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
            title: 'Nil Code',
            // 在这个页面中包含的块，默认情况下会包含
            // 提取出来的通用 chunk 和 vendor chunk。
            chunks: ['chunk-vendors', 'chunk-common', 'index']
        },
    },
    configureWebpack: config => {
        return {
            plugins: [
                new CompressionPlugin({
                    algorithm: 'gzip',
                    test: /\.(js|css|woff|ttf)$/,// 匹配文件名
                    threshold: 10240, // 对超过10k的数据压缩
                    deleteOriginalAssets: false, // 不删除源文件
                    minRatio: 0.8 // 压缩比
                })
            ],
            externals: {
                // CDN资源 其中键是项目中引用的, 值是所引用资源的名字
                'vue': 'Vue',
                'vue-router': 'VueRouter',
                'ElementUI': 'ELEMENT',
                'axios': 'axios',
            }
        }
    },
};


