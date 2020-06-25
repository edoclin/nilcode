<template>
    <div>
        <el-row>
            <el-col :xs="{span: 24}" :sm="{span: 8}">
                <div class="grid-content bg-purple"><img :src="logo" style="width: 30%;margin-top: 4%"/></div>

            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 8}" style="margin-top: 2%">
                <div class="grid-content bg-purple">

                    <el-autocomplete
                            prefix-icon="el-icon-search"
                            style="text-align: center;"
                            class="inline-input"
                            v-model="state1"
                            :fetch-suggestions="querySearch"
                            placeholder="请输入文章/分类/标签"
                            @select="handleSelect"
                            :trigger-on-focus="false">
                        <template slot-scope="{ item }">
                            {{item.value}}
                            <span style="float: right" v-show="item.type !== ''"><el-button size="mini" :type="getType(item.type)">{{ item.type }}</el-button></span>
                        </template>
                    </el-autocomplete>
                </div>


            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 8}" style="margin: 2% 0%">
                <div class="grid-content bg-purple">
                    <el-button type="text" @click="toTarget('/index')">首页</el-button>
                    <el-button type="text" @click="toTarget('/archive')">归档</el-button>
                    <el-button type="text" @click="toTarget('/links')">链接</el-button>
                    <el-button type="text" @click="toTarget('/about')">关于</el-button>

                </div>

            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "GlobalNavigation",
        data() {
            return {
                logo: require('@/assets/logo.png'),
                state1: '',
                state2: ''
            }
        },
        methods: {
            querySearch(queryString, cb) {
                if (queryString != null && queryString !== '') {
                    this.axios.get('blog/autoComplete/' + queryString).then(value => {
                        if (value.data.code === 2001) {
                            cb(JSON.parse(value.data.data));
                        } else {
                            cb([{'value': '无结果', 'type': ''}])
                        }
                    });
                }


            },
            handleSelect(item) {
                if (item.type === '文章') {
                    this.$router.push(`/detail/${item.data}`);
                    return;
                }

                if (item.type === '分类') {
                    this.$router.push(`/search/classifications/${item.value}`);
                    return;
                }


            },
            toTarget(router) {
                this.$router.push(router)
            },
            getType(typeName) {
                if (typeName === '标签') {
                    return 'success'
                }
                if (typeName === '文章') {
                    return 'primary'
                }
                if (typeName === '分类') {
                    return ''
                }
            }
        }
    }
</script>

<style scoped>

</style>
