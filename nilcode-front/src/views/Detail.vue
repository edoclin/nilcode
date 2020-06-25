<template>
    <div style="text-align: left">
        <el-row>
            <el-col :xs="{span: 24}" :sm="{span: 6}" style="position: fixed;">
                <el-link style="margin: 0 40%" type="success" @click="showDialog" v-if="showButton">查看目录</el-link>
                <div v-show="dialog" id="toc"/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 14, offset: 6}">
                <el-breadcrumb separator="/" style="font-size: small">
                    <el-breadcrumb-item v-for="(innerItem, innerIndex) in blog.classifications"
                                        :key="innerIndex"
                                        :to="{ path: `/search/classifications/${innerItem}`, }">
                        {{innerItem}}
                    </el-breadcrumb-item>

                    <el-breadcrumb-item v-show="true" >
                        <span style="color: #f57272">{{blog.clickCount}} 次点击</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>
                <MarkdownPreview :initialValue="blog.content" data-toc="#toc"/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 4, offset: 20}" v-show="showQRCode"
                    style="text-align: center;position: fixed;margin-top: 1%">
                <div id="qrcode"/>
                <br>
                <el-link type="success" style="float: left;margin-left: 3%">
                    扫码继续浏览
                </el-link>
                <br>
                <div v-show="false">{{id}}</div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import {MarkdownPreview} from 'vue-meditor'
    import 'toc-helper/css/toc-helper.min.css'
    import TocHelper from 'toc-helper'
    import QRCode from 'qrcodejs2'

    export default {
        name: "Detail",
        components: {
            MarkdownPreview,
        },
        data() {
            return {
                blogQRCode: require('@/assets/QRCode.png'),
                dialog: false,
                showButton: true,
                showQRCode: true,
                blog: {},
            };
        },
        computed: {
            id() {
                this.changeContent(this.$route.params.id);
                return this.$route.params.id
            }
        },
        mounted() {
            this.checkDevice();
            this.$nextTick(function () {
                this.qrcode();
            })
        },
        created() {
            window.addEventListener('resize', this.getSize);
            this.getSize()
        },
        methods: {
            load(id) {
                this.axios.get(`blog/byId/${id}`).then(value => {
                    if (value.data.code === 2001) {
                        this.blog = JSON.parse(value.data.data);
                    }
                });
            },
            checkDevice() {
                if (this.isMobile()) {
                    this.showButton = false;
                    this.showQRCode = false;
                }
            },
            showDialog() {
                this.dialog = true;
                this.showButton = false;
                new TocHelper().reset();
            },

            isMobile() {
                return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
            },
            getUrl() {
                return "" + window.location.href;
            },
            qrcode() {
                new QRCode('qrcode', {
                    width: 100,
                    height: 100,        // 高度
                    text: this.getUrl(),   // 二维码内容
                    background: '#f0f',   // 背景色
                    foreground: '#ff0',    // 前景色
                    correctLevel: QRCode.CorrectLevel.M
                })
            },
            getSize() {
                if (window.innerWidth <= 768) {
                    this.showButton = false;
                    this.showQRCode = false;
                } else {
                    this.showButton = true;
                    this.showQRCode = true;
                }
            },
            changeContent(id) {
                this.load(id)
            }
        },

    };
</script>

<style scoped>

</style>
