<template>
    <div>
        <el-tabs v-model="activeName" @tab-click="handleTagsClick">
            <el-tab-pane label="链接管理" name="链接管理">
                <el-table
                        :data="tableData"
                        border
                        style="width: 100%"
                        :default-sort="{prop: 'name', order: 'descending'}"
                        v-loading="loading"
                        highlight-current-row>
                    <el-table-column
                            prop="linkName"
                            label="名称"
                            sortable
                            fixed
                            width="300"/>

                    <el-table-column
                            prop="linkType"
                            label="分类"
                            sortable
                            width="150"
                            :filters="[{ text: '工具', value: '工具' }, { text: '文档', value: '文档' }, { text: '其他', value: '其他' }]"
                            :filter-method="filterType"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                    :type="getType(scope.row.linkType)"
                                    disable-transitions>{{scope.row.linkType}}
                            </el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="linkUrl"
                            sortable
                            label="链接"
                    />


                    <el-table-column
                            fixed="right"
                            width="200"
                            label="操作">

                        <template slot-scope="scope">
                            <el-button @click="handleClick(scope.row)" type="text" size="small" style="color: #ebb563">
                                编辑
                            </el-button>
                            <el-button @click="handleDelete(scope.row)" type="text" size="small" style="color: #f57272">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        small
                        layout="prev, pager, next"
                        :total="total"
                        :hide-on-single-page="true"
                        @current-change="handleChangePage"/>

            </el-tab-pane>
            <el-tab-pane label="添加链接" name="添加链接">
                <el-row :gutter="10">
                    <el-col :xs="{span: 24}" :sm="{span: 16, offset: 4}">
                        <el-form ref="form" :model="form" label-width="80px">
                            <el-form-item label="链接名称">
                                <el-input v-model="form.linkName"/>
                            </el-form-item>
                            <el-form-item label="链接地址">
                                <el-input v-model="form.linkUrl"/>
                            </el-form-item>

                            <el-form-item label="类型选择">
                                <el-select v-model="form.linkType" placeholder="请选择类型">
                                    <el-option label="工具" value="工具"/>
                                    <el-option label="文档" value="文档"/>
                                    <el-option label="其他" value="其他"/>
                                </el-select>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" @click="handleSubmit">{{form.buttonName}}</el-button>
                            </el-form-item>
                        </el-form>

                    </el-col>
                </el-row>

            </el-tab-pane>
        </el-tabs>


    </div>
</template>

<script>
    export default {
        name: "AdminLinks",
        data() {
            return {
                loading: false,
                tableData: [],
                total: 0,
                activeName: '链接管理',
                form: {
                    buttonName: '立即创建',
                },
                tempForm: {},
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            handleChangePage(page) {
                this.axios.get('/links/all/' + page).then(value => {
                    if (value.data.code === 2001) {
                        this.tableData = JSON.parse(value.data.data)
                    } else {
                        this.$message.error("服务器繁忙");
                    }

                })
            },
            initData() {
                this.axios.get('/links/total').then(value => {
                    this.total = parseInt(value.data.data);
                });
                this.axios.get('/links/all/1').then(value => {
                    if (value.data.code === 2001) {
                        this.tableData = JSON.parse(value.data.data)
                    } else {
                        this.$message.error("服务器繁忙");
                    }

                })
            },
            handleClick(form) {
                this.form = form;
                this.form.oldUrl = form.linkUrl;
                this.tempForm = form;
                this.form.buttonName = '保存修改';
                this.activeName = '添加链接'
            },
            handleTagsClick(tab, event) {
                window.console.log(tab, event);
            },
            handleDelete(form) {
                this.form = form;
                let params = new URLSearchParams();
                params.append('linkUrl', this.form.linkUrl);
                this.$confirm('此操作将永久删除该链接, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.axios.post('links/del', params).then(value => {
                        if (value.data.code === 2002) {
                            this.tableData.splice(this.tableData.indexOf(form), 1);
                            this.$message.success('删除成功');
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            filterType(value, row) {
                return row.linkType === value;
            },
            getType(linkType) {
                if (linkType === '工具') {
                    return 'success';
                }
                return linkType === '文档' ? 'danger' : 'warning'
            },
            handleSubmit() {
                let params = new URLSearchParams();
                params.append('linkName', this.form.linkName);
                params.append('linkType', this.form.linkType);
                params.append('linkUrl', this.form.linkUrl);
                params.append('oldUrl', this.form.oldUrl);

                this.axios.post('links/saveOrUpdate', params).then(value => {
                    if (value.data.code === 2003) {
                        if (this.form.buttonName === '立即创建') {
                            this.$message.success('保存成功');
                            this.tableData.push(this.form);
                        } else {
                            window.console.log(this.tempForm);
                            this.$message.success('修改成功');
                            this.tableData.splice(this.tableData.indexOf(this.tempForm), 1);
                            this.tableData.push(this.form);
                            this.activeName = '链接管理';

                        }
                        this.form.buttonName = '立即创建';
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
