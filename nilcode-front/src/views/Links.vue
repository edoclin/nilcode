<template>
    <div >
        <el-row>
            <el-col :xs="{span: 24}" :sm="{span: 16, offset: 4}">
                <el-collapse v-model="activeName" accordion @change="change">
                    <el-collapse-item title="工具" name="工具">
                        <el-row :gutter="10">
                            <el-col :xs="{span: 24}" style="text-align: left;margin-left: 20%;" v-for="(item, index) in links" :key="index">
                                <el-link type="success" style="font-size: large;margin-right: 20%" disabled>#</el-link>
                                <el-link type="success" target="_blank" style="font-size: large" :href="item.linkUrl">{{item.linkName}}</el-link>
                            </el-col>
                        </el-row>
                    </el-collapse-item>
                    <el-collapse-item title="文档" name="文档">
                        <el-row :gutter="10">
                            <el-col :xs="{span: 24}" style="text-align: left;margin-left: 20%;" v-for="(item, index) in links" :key="index">
                                <el-link type="success" style="font-size: large;margin-right: 20%" disabled>#</el-link>
                                <el-link type="success" target="_blank" style="font-size: large" :href="item.linkUrl">{{item.linkName}}</el-link>
                            </el-col>
                        </el-row>
                    </el-collapse-item>
                    <el-collapse-item title="其他" name="其他">
                        <el-row :gutter="10">
                            <el-col :xs="{span: 24}" style="text-align: left;margin-left: 20%;" v-for="(item, index) in links" :key="index">
                                <el-link type="success" style="font-size: large;margin-right: 20%" disabled>#</el-link>
                                <el-link type="success" target="_blank" style="font-size: large" :href="item.linkUrl">{{item.linkName}}</el-link>
                            </el-col>
                        </el-row>
                    </el-collapse-item>
                </el-collapse>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Links",
        data() {
            return {
                'activeName': '',
                links: [],
                nowType: '',
            }
        },
        methods: {
            change(activeName) {

                if (activeName === '') {
                    this.links.length = 0;
                    return;
                }
                if (this.nowType !== activeName) {
                    this.links.length = 0;
                    this.nowType = activeName
                }
                this.axios.get('links/linkType/' + activeName).then(value => {
                    if (value.data.code === 2001) {
                        this.links = JSON.parse(value.data.data)
                    }

                })

            }
        }
    }
</script>

<style scoped>

</style>
