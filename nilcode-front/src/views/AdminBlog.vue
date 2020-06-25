<template>
    <div>
        <el-table
                :data="tableData"
                border
                style="width: 100%"
                :default-sort="{prop: 'name', order: 'descending'}"
                v-loading="loading"
                highlight-current-row>
            <el-table-column
                    prop="blogTitle"
                    label="标题"
                    sortable
                    fixed
                    width="300"/>
            <el-table-column
                    prop="publishedDate"
                    sortable
                    label="发布日期"
                    width="150"/>

            <el-table-column
                    prop="clickCount"
                    label="点击量"
                    sortable
                    width="120"/>

            <el-table-column
                    prop="status"
                    label="状态"
                    :filters="[{ text: '已发布', value: '已发布' }, { text: '未发布', value: '未发布' }]"
                    :filter-method="filterTag"
            >
                <template slot-scope="scope">
                    <el-tag
                            :type="scope.row.status === '已发布' ? 'success' : 'danger'"
                            disable-transitions>{{scope.row.status}}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    prop="classifications"
                    label="分类"
                    sortable
            />
            <el-table-column
                    fixed="right"
                    width="200"
                    label="操作">
                <template slot-scope="scope">
                    <el-button @click="handleClickToView(scope.row)" type="text" size="small" style="color: #67c23a">查看
                    </el-button>
                    <el-button type="text" size="small" @click="handleClickToEdit(scope.row)" style="color: #ebb563">
                        编辑
                    </el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small" style="color: #f57272">删除
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
    </div>
</template>

<script>
    export default {
        name: "AdminBlog",
        mounted() {
            this.getBlog();
        },

        data() {
            return {
                tableData: [],
                loading: true,
                total: 0,
            }
        },
        methods: {
            handleChangePage(page) {
                this.axios.get('blog/admin/' + page).then(value => {
                    if (value.data.code === 2001) {
                        this.tableData = JSON.parse(value.data.data);
                    }
                    this.loading = false;
                });
            },
            getBlog() {
                this.axios.get('blog/admin/1').then(value => {
                    if (value.data.code === 2001) {
                        this.tableData = JSON.parse(value.data.data);
                    }
                    this.loading = false;
                });
                this.axios.get('blog/total').then(value => {
                    if (value.data.code === 2001) {
                        this.total = parseInt(value.data.data)
                    }
                })
            },
            handleClickToView(row) {
                let routeData = this.$router.resolve({path: `/detail/${row.blogId}`});
                window.open(routeData.href, '_blank');
            },
            handleClickToEdit(row) {
                localStorage.setItem('updateBlogId', row.blogId);
                this.$router.push({path: '/admin/main/edit'});
            },

            handleDelete(row) {
                this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.axios.get('blog/del/' + row.blogId).then(value => {
                        if (value.data.code === 2002) {
                            this.tableData.splice(this.tableData.indexOf(row), 1);
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                        } else {
                            this.$message.error('服务器繁忙');
                        }
                    });

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            filterTag(value, row) {
                return row.status === value;
            },
        },

    }
</script>

<style scoped>

</style>
