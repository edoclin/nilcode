<template>
    <div>
        <el-row>
            <el-col :xs="{span: 24}" :sm="{span: 12}" style="margin-bottom: 2%">
                <el-card class="box-card" style="text-align: left;min-height: 300px;width: 95%;margin: 0 auto">
                    <div slot="header" class="clearfix">
                        <span>分类列表</span>
                        <el-button type="primary" icon="el-icon-edit" circle style="float: right" size="small"
                                   @click="handleAddClassification"/>
                    </div>
                    <el-cascader
                            value="classId"
                            @change="handleChangeType"
                            style="width: 100%" size="small"
                            placeholder="分类列表"
                            :options="classifications"
                            filterable/>
                </el-card>
            </el-col>

            <el-col :xs="{span: 24}" :sm="{span: 12}" style="text-align: left">
                <!--                <el-button type="primary" disabled="" size="mini">全部标签</el-button>-->

                <el-card class="box-card" style="text-align: left;min-height: 300px;width: 95%;margin: 0 auto">
                    <div slot="header" class="clearfix">
                        <span>标签列表</span>
                        <el-button type="warning" icon="el-icon-edit" circle style="float: right" size="small"/>

                    </div>
                    <el-tag style="margin: 2% 1%;"
                            size="small"
                            :key="index"
                            v-show="item.tagName !== ''"
                            v-for="(item, index) in tags"
                            closable
                            :disable-transitions="false"
                            @close="handleClose(item)"
                            type="success">
                        {{item.tagName}}
                    </el-tag>
                </el-card>

            </el-col>


        </el-row>
    </div>
</template>

<script>
    export default {
        name: "AdminType",
        data() {
            return {
                tags: [
                ],
                classifications: [],
                blog: {
                    type: ''
                }
            }
        },
        mounted() {
            this.getClassificationsAndTags();
        },
        methods: {
            getClassificationsAndTags() {
                this.axios.get('classifications/all').then(value => {
                    if (value.data.code === 2001) {
                        this.classifications = JSON.parse(value.data.data);
                    }
                });
                this.axios.get('tags/all').then(value => {
                    if (value.data.code === 2001) {
                        this.tags = JSON.parse(value.data.data);
                    }
                });
            },
            handleAddClassification() {
                this.$prompt('请输入分类, 如果存在父级分类, 使用 双斜杠("//") 分割. 若该分类存在则为 "删除" 操作. 仅支持三级分类', '添加/删除分类', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /[\u4e00-\u9fa5a-zA-Z]/,
                    inputErrorMessage: '格式不正确'
                }).then(({value}) => {

                    let params = new URLSearchParams();
                    params.append('className', value);
                    this.axios.post('classifications/saveOrRemove', params).then(result => {
                        let type = '';
                        let message = '';
                        if (result.data.code === 2007) {
                            type = 'success';
                            message = '添加分类成功';

                        } else if (result.data.code === 2006) {
                            type = 'success';
                            message = '移除分类成功';
                        } else if (result.data.code === 4002) {
                            type = 'error';
                            message = '存在子分类';
                        } else {
                            type = 'error';
                            message = '服务器繁忙';
                        }
                        this.$message({
                            classifications: type,
                            message: message + ': ' + value
                        });
                    });

                }).catch(() => {
                    this.$message({
                        classifications: 'info',
                        message: '取消输入'
                    });
                });
            },
            handleChangeType(type) {
                this.blog.type = '';
                type.forEach(value => {
                    this.blog.type += value + '/';
                });
            },
            handleClose(tag) {
                this.$confirm('删除该标签以及取消关联相关的文章, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.axios.get('tags/del/' + tag.tagId).then(value => {
                        if (value.data.code === 2002) {
                            this.tags.splice(this.tags.indexOf(tag), 1);
                            this.$message({
                                type: 'success',
                                message: '删除成功'
                            });
                        } else {
                            this.$message({
                                type: 'error',
                                message: '服务器繁忙'
                            });
                        }
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        }
    }
</script>

<style scoped>

</style>
