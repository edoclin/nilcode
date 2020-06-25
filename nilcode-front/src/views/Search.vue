<template>
    <div>
        <el-row :gutter="10">
            <el-col :xs="{span: 24}" :sm="{span: 16, offset: 4}">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>{{this.$route.params.type === 'classifications' ? '分类' : '标签'}}
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>{{this.$route.params.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </el-col>
        </el-row>
        <br>
        <el-row>
            <el-col :xs="{span: 24}" :sm="{span: 16, offset: 4}">
                <el-row :gutter="10">
                    <el-col :xs="{span: 24}" :sm="{span: 8}"

                            v-for="(item, index) in articles"
                            :key="index">
                        <el-card shadow="hover"
                                 style="width: 80%;margin: 5% auto;min-height: 200px; min-width: 200px;background-color: #ecf0f1;border-radius: 4%">
                            <img style="width: 100%;border-radius: 5%;height: 150px"
                                 :src="item.coverImg"
                                 class="image">
                            <div style="padding: 2%;">
                                <br>
                                <span><el-link type="danger"
                                               @click="goToDetail(item.blogId)">{{item.blogTitle}}</el-link></span>

                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <div v-show="false">{{name}} + {{type}}</div>
    </div>
</template>

<script>
    export default {
        name: "Search",
        data() {
            return {
                articles: [],
            }
        },
        mounted() {
            window.console.log(this.$route.params.type);
            window.console.log(this.$route.params.name);

            this.axios.get(`blog/search/${this.$route.params.type}/${this.$route.params.name}`).then(value => {
                this.articles = JSON.parse(value.data.data);
            })
        },
        computed: {
            name() {
                this.change();
                return this.$route.params.name;
            },
            type() {
                this.change();
                return this.$route.params.type;
            }
        },
        methods: {
            goToDetail(blogId) {
                this.$router.push(`/detail/${blogId}`)
            },
            change() {
                this.axios.get(`blog/search/${this.$route.params.type}/${this.$route.params.name}`).then(value => {
                    this.articles = JSON.parse(value.data.data);
                })
            },

        }
    }
</script>

<style scoped>

</style>
