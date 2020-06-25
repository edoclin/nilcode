<template>
    <div>
        <el-row :gutter="0">
            <!--<el-col :xs="{span: 24}" :sm="{span: 5}">

                <el-link type="success">点击量</el-link>

                <el-card class="box-card" style="margin: 10% auto;width: 85%;" shadow="hover">
                    <div v-for="(item, index) in topNine" :key="index" style="margin-bottom: 2%">
                        <el-link @click="goToDetail(item.blogId)"> {{item.blogName}}</el-link>
                    </div>
                </el-card>
            </el-col>-->
            <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 16, offset: 4 }" >
                <el-row>
                    <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" v-for="(item, index) in articles"
                            :key="index">
                        <el-card shadow="hover"
                                 style="width: 90%;margin: 2% auto;;background-color: #fdedec;border-radius: 2%;text-align: left">
                            <!-- <img style="width: 100%;border-radius: 1%; height: 150px;"
                                  :src="item.coverImg"
                                  class="image">-->
                            <div style="padding: 1%;">
                                <el-link type="danger" style=""
                                         @click="goToDetail(item.blogId)">
                                    <strong style="font-size: medium">{{item.blogTitle}}</strong>
                                </el-link>
                                <br>
                                <br>
                                <el-breadcrumb separator="/" style="font-size: small">
                                    <el-breadcrumb-item v-for="(innerItem, innerIndex) in item.classifications"
                                                        :key="innerIndex"
                                                        :to="{ path: `/search/classifications/${innerItem}`, }"
                                                        style="margin-bottom: 2%">
                                        {{innerItem}}
                                    </el-breadcrumb-item>
                                    <el-breadcrumb-item v-show="false"></el-breadcrumb-item>
                                </el-breadcrumb>
                                <strong style="font-size: small">标签 :</strong> <el-tag :type="tagType[randomTagType()]" v-show="innerItem1 !== ''"
                                        v-for="(innerItem1, innerIndex1) in item.tags" :key="innerIndex1" size="small"
                                        style="margin-right: 3%; margin-bottom: 2%"
                                        @click="searchByTag(innerItem1)">
                                    <el-link style="font-size: small" :underline="false">{{innerItem1}}</el-link>
                                </el-tag>
                                <br>
                                <strong style="font-size: small">时间 :</strong> <span style="float:bottom;font-size: small;color: #909399">{{item.publishedDate.replace('-', '年').replace('-', '月')}}日</span>

                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-pagination
                        small
                        layout="prev, pager, next"
                        :total="total"
                        :hide-on-single-page="true"
                        @current-change="handleChangePage"/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 4}" v-if="!DeviceIsMobile" style="position: fixed;">
                    <el-link type="info">分类</el-link>
                    <br>
                    <br>
                    <el-card shadow="hover">
                        <el-badge :value="item.relativeCount" v-for="(item, index2) in classifications" :key="index2"
                                  style="margin: 2% 3%">
                            <el-tag :type="tagType[randomTagType()]" size="medium"
                                    @click="searchByClassification(item.className)">
                                <el-link style="font-size: small" :underline="false">{{item.className}}</el-link>
                            </el-tag>
                        </el-badge>
                    </el-card>
                    <br>
                    <br>
                    <el-link type="info">标签</el-link>
                    <br>
                    <br>
                    <el-card v-if="tags.length !== 0 && tags.length !== 1" shadow="hover">
                        <el-tag v-show="item.tagName !== ''" :type="tagType[randomTagType()]" size="medium"
                                v-for="(item, index3) in tags" :key="index3"
                                style="margin: 2% 3%"
                        @click="searchByTag(item.tagName)">
                            <el-link style="font-size: x-small" :underline="false">{{item.tagName}}</el-link>
                        </el-tag>
                    </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data() {
            return {
                total: 0,
                articles: [],
                tagType: ['success', 'warning', 'info', 'danger', ''],
                fullscreenLoading: true,
                deep: 0,
                classifications: [],
                tags: [],
                topNine: [],
                DeviceIsMobile: true,
            }
        },
        created() {
            this.DeviceIsMobile = this.isMobile();
        },
        mounted() {
            this.getBlog();
            this.getClassificationsAndTags();
            this.getTopNine()
        },
        methods: {
            handleChangePage(page) {
                this.axios.get('blog/index/' + page).then(value => {
                    if (value.data.code === 2001) {
                        this.articles = JSON.parse(value.data.data);
                    }
                });
            },
            getTopNine() {
                this.axios.get('top-nine/all').then(value => {
                    if (value.data.code === 2001) {
                        this.topNine = JSON.parse(value.data.data)
                    }
                })
            },
            getBlog() {
                this.axios.get('blog/total').then(value => {
                    this.total = parseInt(value.data.data);
                });
                this.axios.get('blog/index/1').then(value => {
                    if (value.data.code === 2001) {
                        this.articles = JSON.parse(value.data.data);
                    }
                });
            },
            getClassificationsAndTags() {
                this.axios.get('classifications/allRoot').then(value => {
                    if (value.data.code === 2001) {
                        this.classifications = JSON.parse(value.data.data)
                    }
                });

                this.axios.get('tags/all').then(value => {
                    if (value.data.code === 2001) {
                        this.tags = JSON.parse(value.data.data)
                    }
                })
            },
            randomTagType() {
                return Math.floor(Math.random() * 5);
            },
            searchByTag(tagName) {
                this.$router.push(`/search/tags/${tagName}`)
            },
            searchByClassification(classificationName) {
                this.$router.push(`/search/classifications/${classificationName}`)
            },
            goToDetail(blogId) {
                this.$router.push(`/detail/${blogId}`)
            },
            isMobile() {
                return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i) != null;
            },
        }
    }
</script>

<style scoped>

</style>
